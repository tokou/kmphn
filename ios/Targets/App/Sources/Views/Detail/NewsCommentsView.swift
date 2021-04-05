import SwiftUI
import Hackernews

// one way (probably not the best) to remove ambiguity between Identifiable's id and Content's id
extension NewsDetailComment.Content {
    var identifier: ItemId { id }
}

extension NewsDetailComment : Identifiable {
    public var id: String {
        switch self {
        case is NewsDetailComment.Loading:
            return "0"
        case let c as NewsDetailComment.Content:
            return "\(c.identifier)"
        default: fatalError("Unhandled type of comment")
        }
    }
}

struct NewsCommentsView<Header>: View where Header: View {
    let comments: [NewsDetailComment]
    let onCommentClicked: (ItemId) -> ()
    let onUserClicked: (UserId) -> ()
    let onLinkClicked: (String, Bool) -> ()
    let header: () -> Header

    var body: some View {
        List {
            header().listRowInsets(.init())
            CommentTree(comments: comments, padding: 0, onCommentClicked: onCommentClicked, onUserClicked: onUserClicked, onLinkClicked: onLinkClicked)
        }
        .listStyle(PlainListStyle())
        .buttonStyle(PlainButtonStyle())
    }
}

private struct CommentTree: View {
    let comments: [NewsDetailComment]
    let padding: CGFloat
    let onCommentClicked: (ItemId) -> ()
    let onUserClicked: (UserId) -> ()
    let onLinkClicked: (String, Bool) -> ()

    var body: some View {
        ForEach(comments) { comment in
            CommentRow(
                comment: comment,
                padding: padding,
                onCommentClicked: onCommentClicked,
                onUserClicked: onUserClicked,
                onLinkClicked: onLinkClicked
            )
            if let c = comment as? NewsDetailComment.ContentExpanded {
                CommentTree(
                    comments: c.children,
                    padding: padding + 16,
                    onCommentClicked: onCommentClicked,
                    onUserClicked: onUserClicked,
                    onLinkClicked: onLinkClicked
                )
            }
        }
    }
}

struct CommentRow: View {
    let comment: NewsDetailComment
    let padding: CGFloat
    let onCommentClicked: (ItemId) -> ()
    let onUserClicked: (UserId) -> ()
    let onLinkClicked: (String, Bool) -> ()

    var body: some View {
        switch comment {
        case is NewsDetailComment.Loading:
            Text("...")
                .listRowInsets(.init())
                .padding(.leading, padding)
        case let c as NewsDetailComment.ContentCollapsed:
            Button(action: { onCommentClicked(c.id) }) {
                Text("\(c.user) \(c.time) \(c.childrenCount)")
            }
            .listRowInsets(.init())
            .padding(.leading, padding)
        case let c as NewsDetailComment.ContentExpanded:
            Button(action: { onCommentClicked(c.id) }) {
                VStack {
                    Text("\(c.user) \(c.time)")
                    RichTextView(text: c.text, onLinkClicked: onLinkClicked)
                }
            }
            .listRowInsets(.init())
            .padding(.leading, padding)
        default: fatalError("Unhandled type of comment")
        }
    }
}

struct NewsCommentsView_Previews: PreviewProvider {
    static var previews: some View {
        NewsCommentsView(
            comments: [],
            onCommentClicked: { _ in },
            onUserClicked: { _ in },
            onLinkClicked: { _, _ in }
        ) {
            Text("Header")
        }
    }
}

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
            CommentRowView(
                comment: comment,
                padding: padding,
                onCommentClicked: onCommentClicked,
                onUserClicked: onUserClicked,
                onLinkClicked: onLinkClicked
            )
            .listRowInsets(.init())
            if let c = comment as? NewsDetailComment.ContentExpanded {
                CommentTree(
                    comments: c.children,
                    padding: padding + commentPaddingStep,
                    onCommentClicked: onCommentClicked,
                    onUserClicked: onUserClicked,
                    onLinkClicked: onLinkClicked
                )
            }
        }
    }
}

struct NewsCommentsView_Previews: PreviewProvider {
    static var previews: some View {
        NewsCommentsView(
            comments: [
                NewsDetailComment.Loading(),
                NewsDetailComment.ContentCollapsed(id: 1, user: "user", time: "now", isOp: false, isSelected: false, childrenCount: "12"),
                NewsDetailComment.ContentExpanded(id: 2, user: "user", time: "now", isOp: true, isSelected: true, children: [
                    NewsDetailComment.ContentCollapsed(id: 1, user: "user", time: "now", isOp: false, isSelected: false, childrenCount: "12"),
                    NewsDetailComment.Loading()
                ], text: [NewsDetailText.Plain(text: "Hello world")])
            ],
            onCommentClicked: { _ in },
            onUserClicked: { _ in },
            onLinkClicked: { _, _ in }
        ) {
            Text("Header")
                .padding(16)
        }
    }
}

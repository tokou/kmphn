import SwiftUI
import Hackernews

struct CommentRowView: View {
    let comment: NewsDetailComment
    let padding: CGFloat
    let onCommentClicked: (ItemId) -> ()
    let onUserClicked: (UserId) -> ()
    let onLinkClicked: (String, Bool) -> ()

    var body: some View {
        switch comment {
        case is NewsDetailComment.Loading:
            CommentPaddingView(padding: padding) {
                VStack(spacing: 0) {
                    CommentLoaderView()
                        .frame(maxWidth: .infinity, alignment: .leading)
                    Divider()
                }
            }
        case let collapsed as NewsDetailComment.ContentCollapsed:
            CommentPaddingView(padding: padding) {
                SelectionView(isSelected: collapsed.isSelected) {
                    VStack(spacing: 0) {
                        CommentCollapsedView(
                            comment: collapsed,
                            onUserClicked: onUserClicked,
                            onCommentClicked: onCommentClicked
                        )
                        .frame(maxWidth: .infinity, alignment: .leading)
                        Divider()
                    }
                }
            }
        case let expanded as NewsDetailComment.ContentExpanded:
            CommentPaddingView(padding: padding) {
                SelectionView(isSelected: expanded.isSelected) {
                    VStack(spacing: 0) {
                        CommentExpandedView(
                            comment: expanded,
                            onCommentClicked: onCommentClicked,
                            onUserClicked: onUserClicked,
                            onLinkClicked: onLinkClicked
                        )
                        .frame(maxWidth: .infinity, alignment: .leading)
                        Divider()
                    }
                }
            }
        default: fatalError("Unhandled type of comment")
        }
    }
}

struct CommentRowView_Previews: PreviewProvider {
    static var previews: some View {
        CommentRowView(comment: NewsDetailComment.Loading(), padding: 32, onCommentClicked: { _ in }, onUserClicked: { _ in }, onLinkClicked: { _, _ in })
            .previewLayout(.sizeThatFits)
    }
}

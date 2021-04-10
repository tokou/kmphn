import SwiftUI
import Hackernews

struct CommentExpandedView: View {
    let comment: NewsDetailComment.ContentExpanded
    let onCommentClicked: (ItemId) -> ()
    let onUserClicked: (UserId) -> ()
    let onLinkClicked: (String, Bool) -> ()

    var body: some View {
        Button(action: { onCommentClicked(comment.id) }) {
            VStack(alignment: .leading, spacing: 16) {
                HStack {
                    CommentHeaderView(comment: comment, onUserClicked: onUserClicked)
                    Spacer()
                }
                .frame(maxWidth:.infinity)
                .contentShape(Rectangle())
                RichTextView(text: comment.text, onLinkClicked: onLinkClicked)
            }
            .padding(16)
        }
        .frame(maxWidth: .infinity, alignment: .leading)
    }
}

struct CommentExpandedView_Previews: PreviewProvider {
    static var previews: some View {
        let comment = NewsDetailComment.ContentExpanded(id: 1, user: "user", time: "now", isOp: true, isSelected: true, children: [], text: [NewsDetailText.Plain(text: "Lorem ipsum")])
        CommentExpandedView(comment: comment, onCommentClicked: { _ in }, onUserClicked: { _ in }, onLinkClicked:  { _, _ in })
            .previewLayout(.sizeThatFits)
    }
}

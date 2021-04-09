import SwiftUI
import Hackernews

struct CommentCollapsedView: View {
    let comment: NewsDetailComment.ContentCollapsed
    let onUserClicked: (UserId) -> ()
    let onCommentClicked: (ItemId) -> ()

    var body: some View {
        Button(action: { onCommentClicked(comment.id) }) {
            HStack {
                CommentHeaderView(comment: comment, onUserClicked: onUserClicked)
                Text(comment.childrenCount)
                    .font(theme.typography.body1)
            }.padding(16)
        }
    }
}

struct CommentCollapsedView_Previews: PreviewProvider {
    static var previews: some View {
        let comment = NewsDetailComment.ContentCollapsed(id: 1, user: "user", time: "now", isOp: true, isSelected: true, childrenCount: "12")
        CommentCollapsedView(comment: comment, onUserClicked: { _ in }, onCommentClicked: { _ in })
            .frame(minWidth: 360, alignment: .leading)
            .previewLayout(.sizeThatFits)
    }
}

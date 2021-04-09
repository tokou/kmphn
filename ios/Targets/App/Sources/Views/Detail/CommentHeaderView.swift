import SwiftUI
import Hackernews

struct CommentHeaderView: View {
    let comment: NewsDetailComment.Content
    let onUserClicked: (UserId) -> ()

    var body: some View {
        HStack {
            CommentUserView(user: comment.user, isOp: comment.isOp, onUserClick: onUserClicked)
            CommentTimeView(time: comment.time)
        }
    }
}

struct CommentHeaderView_Previews: PreviewProvider {
    static var previews: some View {
        let comment = NewsDetailComment.ContentCollapsed(id: 1, user: "user", time: "now", isOp: true, isSelected: true, childrenCount: "12")
        CommentHeaderView(comment: comment, onUserClicked: { _ in })
            .previewLayout(.sizeThatFits)
    }
}

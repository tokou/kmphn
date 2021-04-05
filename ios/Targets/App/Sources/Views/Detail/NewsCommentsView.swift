import SwiftUI
import Hackernews

struct NewsCommentsView: View {
    let comments: [NewsDetailComment]
    let onCommentClicked: (ItemId) -> ()
    let onUserClicked: (UserId) -> ()
    let onLinkClicked: (String, Bool) -> ()

    var body: some View {
        Text("got \(comments.count) comments").frame(maxHeight: .infinity)
    }
}

struct NewsCommentsView_Previews: PreviewProvider {
    static var previews: some View {
        NewsCommentsView(comments: [], onCommentClicked: { _ in }, onUserClicked: { _ in }, onLinkClicked: { _, _ in })
    }
}

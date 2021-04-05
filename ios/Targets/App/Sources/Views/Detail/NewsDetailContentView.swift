import SwiftUI
import Hackernews

struct NewsDetailContentView: View {
    let model: NewsDetailModel
    let onUserClicked: (UserId) -> ()
    let onLinkClicked: (String, Bool) -> ()
    let onCommentClicked: (ItemId) -> ()

    var body: some View {
        switch model {
        case let content as NewsDetailModel.Content:
            List {
                NewsHeaderView(header: content.header, onLinkClicked: onLinkClicked, onUserClicked: onUserClicked)
                    .listRowInsets(.init())
                NewsCommentsView(comments: content.comments, onCommentClicked: onCommentClicked, onUserClicked: onUserClicked, onLinkClicked: onLinkClicked)
                    .listRowInsets(.init())
            }
            .listStyle(PlainListStyle())
            .buttonStyle(PlainButtonStyle())
        case is NewsDetailModel.Loading:
            LoaderView()
        case is NewsDetailModel.Error:
            ErrorView(onClick: {})
        default:
            fatalError()
        }
    }
}

struct NewsDetailContentView_Previews: PreviewProvider {
    static var previews: some View {
        NewsDetailContentView(model: errorStub, onUserClicked: { _ in }, onLinkClicked: { _, _ in }, onCommentClicked: { _ in })
        NewsDetailContentView(model: loadingStub, onUserClicked: { _ in }, onLinkClicked: { _, _ in }, onCommentClicked: { _ in })
        NewsDetailContentView(model: contentStub, onUserClicked: { _ in }, onLinkClicked: { _, _ in }, onCommentClicked: { _ in })
    }
    
    static let errorStub = NewsDetailModel.Error()
    static let loadingStub = NewsDetailModel.Loading()
    static let contentStub = NewsDetailModel.Content(
        header: headerStub,
        comments: commentStubs
    )
    static let headerStub = NewsDetailHeader(id: 2, title: "Title", link: "https://google.com", user: "user", time: "now", commentsCount: "12", points: "42", text: [])
    static let commentStubs: [NewsDetailComment] = [
        .ContentExpanded(id: 1, user: "user", time: "now", isOp: false, isSelected: false, children: [], text: []),
        .ContentCollapsed(id: 2, user: "user", time: "now", isOp: true, isSelected: false, childrenCount: "10"),
        .Loading()
    ]
}

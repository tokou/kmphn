import SwiftUI
import Hackernews

struct NewsDetailContentView: View {
    let model: NewsDetailModel

    var body: some View {
        switch model {
        case let content as NewsDetailModel.Content:
            VStack(spacing: 0) {
                Text("\(content.header.title)")
                Text("\(content.header.user)")
                Text("\(content.header.link ?? "")")
                Text("\(content.header.hnLink)")
                Spacer()
            }
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
        NewsDetailContentView(model: errorStub)
        NewsDetailContentView(model: loadingStub)
        NewsDetailContentView(model: contentStub)
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

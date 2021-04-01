import SwiftUI
import Hackernews

struct NewsDetailView: View {
    private let component: NewsDetail

    @ObservedObject
    private var models: ObservableValue<NewsDetailModel>
    
    init(_ component: NewsDetail) {
        self.component = component
        self.models = ObservableValue(component.models)
    }

    var body: some View {
        let model = models.value
        switch model {
        case is NewsDetailModel.Content:
            // case let content is NewsMainModel.Content causes a segfault
            let content = model as! NewsDetailModel.Content
            VStack {
                HStack {
                    Text("Content")
                    Button("Back") {
                        component.onBack()
                    }
                }
                Text("\(content.header.title)")
                Text("\(content.header.user)")
                Text("\(content.header.link ?? "")")
                Text("\(content.header.hnLink)")
            }
        case is NewsDetailModel.Error:
            Text("Error")
        case is NewsDetailModel.Loading:
            Text("Loading")
        default:
            fatalError()
        }
    }
}

struct NewsDetailView_Previews: PreviewProvider {
    static var previews: some View {
        NewsDetailView(StubNewsDetail())
    }
    
    class StubNewsDetail : NewsDetail {
        let models: Value<NewsDetailModel> = valueOf(contentStub)

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

        func onBack() {}
        func onCommentClicked(id: Int64) {}
        func onLinkClicked(uri: String, forceExternal: Bool) {}
        func onRetry() {}
        func onUserClicked(id: String) {}
    }
}

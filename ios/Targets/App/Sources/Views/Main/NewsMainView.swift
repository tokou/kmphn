import SwiftUI
import Hackernews

extension NewsMainItem : Identifiable {}

struct NewsMainView: View {
    private let component: NewsMain
    
    @ObservedObject
    private var models: ObservableValue<NewsMainModel>

    init(_ component: NewsMain) {
        self.component = component
        self.models = ObservableValue(component.models)
    }

    var body: some View {
        let model = models.value
        switch model {
        case is NewsMainModel.Content:
            // case let content is NewsMainModel.Content causes a segfault
            let content = model as! NewsMainModel.Content
            VStack {
                ForEach(content.items) { item in
                    HStack {
                        Text("\(item.title)\n(\(item.link ?? ""))\n")
                        Button("\(item.comments)") {
                            component.onNewsSecondarySelected(id: item.id)
                        }

                    }
                }
            }
        case is NewsMainModel.Error:
            Text("Error")
        case is NewsMainModel.Loading:
            Text("Loading")
        default:
            fatalError()
        }
    }
}

struct NewsMainView_Previews: PreviewProvider {
    static var previews: some View {
        NewsMainView(StubNewsMain())
    }

    class StubNewsMain : NewsMain {
        let models: Value<NewsMainModel> = valueOf(contentStub)

        static let errorStub = NewsMainModel.Error()
        static let loadingStub = NewsMainModel.Loading()
        static let contentStub = NewsMainModel.Content(
            items: itemStubs,
            isLoadingMore: false,
            isRefreshing: false,
            canLoadMore: true
        )
        static let itemStubs: [NewsMainItem] = [
            .init(id: 1, title: "One", link: nil, user: "user", time: "now", comments: "2", points: "42"),
            .init(id: 2, title: "Two", link: nil, user: "user", time: "now", comments: "12", points: "12"),
            .init(id: 3, title: "Three", link: nil, user: "user", time: "now", comments: "0", points: "112"),
            .init(id: 4, title: "Four", link: nil, user: "user", time: "now", comments: "423", points: "2"),
            .init(id: 5, title: "Five", link: nil, user: "user", time: "now", comments: "52", points: "4")
        ]

        func onLoadMoreSelected() {}
        func onNewsSecondarySelected(id: Int64) {}
        func onNewsSelected(id: Int64, link: String?) {}
        func onRefresh(fromPull: Bool) {}
    }
}

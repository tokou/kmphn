import SwiftUI
import Hackernews

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
        let refresh = { component.onRefresh(fromPull: false) }
        ZStack(alignment: .top) {
            NewsContentView(
                model: model,
                onSelected: component.onNewsSelected,
                onSecondarySelected: component.onNewsSecondarySelected,
                onRefresh: refresh
            ).padding(.top, topBarHeight)
            NewsBarView(onRefresh: refresh)
        }
    }
}

struct NewsMainView_Previews: PreviewProvider {
    static var previews: some View {
        NewsMainView(StubNewsMain(stub: contentStub))
        NewsMainView(StubNewsMain(stub: loadingStub))
        NewsMainView(StubNewsMain(stub: errorStub))
    }

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
    
    class StubNewsMain : NewsMain {
        let models: Value<NewsMainModel>

        init(stub: NewsMainModel = contentStub) {
            models = valueOf(stub)
        }
        
        func onLoadMoreSelected() {}
        func onNewsSecondarySelected(id: ItemId) {}
        func onNewsSelected(id: ItemId, link: String?) {}
        func onRefresh(fromPull: Bool) {}
    }
}

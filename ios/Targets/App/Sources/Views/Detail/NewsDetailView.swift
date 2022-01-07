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
        ZStack(alignment: .top) {
            NewsDetailContentView(
                model: model,
                onUserClicked: component.onUserClicked,
                onLinkClicked: component.onLinkClicked,
                onCommentClicked: component.onCommentClicked
            ).padding(.top, topBarHeight)
            NewsDetailBarView(onBack: component.onBack)
        }
    }
}

struct NewsDetailView_Previews: PreviewProvider {
    static var previews: some View {
        NewsDetailView(StubNewsDetail())
    }
    
    class StubNewsDetail : NewsDetail {
        var models: DecomposeValue<NewsDetailModel> = valueOf(NewsDetailContentView_Previews.contentStub)

        func onBack() {}
        func onCommentClicked(id: ItemId) {}
        func onLinkClicked(uri: String, forceExternal: Bool) {}
        func onRetry() {}
        func onUserClicked(id: String) {}
    }
}

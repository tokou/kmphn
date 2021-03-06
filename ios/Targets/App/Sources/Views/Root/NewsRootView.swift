import SwiftUI
import Hackernews

struct NewsRootView: View {
    @ObservedObject
    private var routerStates: ObservableValue<RouterState<AnyObject, NewsRootChild>>

    init(_ component: NewsRoot) {
        self.routerStates = ObservableValue(component.routerState)
    }

    var body: some View {
        let child = self.routerStates.value.activeChild.instance
        switch child {
        case let main as NewsRootChild.Main:
            NewsMainView(main.component)
        case let detail as NewsRootChild.Detail:
            NewsDetailView(detail.component)
        default: EmptyView()
        }
    }
}

struct NewsRoot_Previews: PreviewProvider {
    static var previews: some View {
        NewsRootView(StubNewsRoot())
            .previewDevice(.init(unicodeScalarLiteral: "iPhone 12 Pro Max"))
        NewsRootView(StubNewsRoot())
            .previewDevice(.init(unicodeScalarLiteral: "iPhone SE (2nd generation)"))
    }
    
    class StubNewsRoot : NewsRoot {
        let routerState: Value<RouterState<AnyObject, NewsRootChild>> =
            simpleRouterState(NewsRootChild.Main(component: NewsMainView_Previews.StubNewsMain()))
    }
}

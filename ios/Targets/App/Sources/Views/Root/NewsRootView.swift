import SwiftUI
import Hackernews

struct NewsRootView: View {
    @ObservedObject
    private var routerStates: ObservableValue<RouterState<AnyObject, NewsRootChild>>

    init(_ component: NewsRoot) {
        self.routerStates = ObservableValue(component.routerState)
    }

    var body: some View {
        let child = self.routerStates.value.activeChild.component
        
        switch child {
        case let main as NewsRootChild.Main:
            NewsMainView(main.component)

        case let detail as NewsRootChild.Detail:
            NewsDetailView(detail.component)
                .transition(
                    .asymmetric(
                        insertion: AnyTransition.move(edge: .trailing),
                        removal: AnyTransition.move(edge: .trailing)
                    )
                )
                .animation(.easeInOut)
            
        default: EmptyView()
        }
    }
}

struct NewsRoot_Previews: PreviewProvider {
    static var previews: some View {
        NewsRootView(StubNewsRoot())
    }
    
    class StubNewsRoot : NewsRoot {
        let routerState: Value<RouterState<AnyObject, NewsRootChild>> =
            simpleRouterState(NewsRootChild.Main(component: NewsMainView_Previews.StubNewsMain()))
    }
}

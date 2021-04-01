import SwiftUI
import Hackernews

let uriHandler: (String) -> () = { url in
    guard let url = URL(string: url) else { return }
    UIApplication.shared.open(url, options: [:], completionHandler: { _ in })
}

struct ContentView: View {
    @State
    private var componentHolder =
        ComponentHolder {
            NewsRootComponent(
                componentContext: $0,
                uriHandler: uriHandler,
                storeFactory: DefaultStoreFactory(),
                api: NewsApiKt.createApi(),
                database: NewsDatabaseKt.createDatabase(driver: DatabaseDriversKt.persistentDatabaseDriver())
            )
        }
    
    var body: some View {
        NewsRootView(componentHolder.component)
            .onAppear { LifecycleRegistryExtKt.resume(self.componentHolder.lifecycle) }
            .onDisappear { LifecycleRegistryExtKt.stop(self.componentHolder.lifecycle) }
    }
}

struct ContentView_Previews: PreviewProvider {
    static var previews: some View {
        ContentView()
    }
}

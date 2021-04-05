import SwiftUI
import Hackernews

struct ContentView: View {
    
    @State
    private var componentHolder = ComponentHolder {
        NewsRootComponent(
            componentContext: $0,
            uriHandler: externalUriHandler,
            storeFactory: DefaultStoreFactory(),
            api: NewsApiKt.createApi(),
            database: NewsDatabaseKt.createDatabase(driver: DatabaseDriversKt.persistentDatabaseDriver())
        )
    }
    
    init(_ uriHandler: @escaping (String) -> ()) {
        self.componentHolder.component.uriHandler = uriHandler
    }
    
    var body: some View {
        NewsRootView(componentHolder.component)
            .onAppear { LifecycleRegistryExtKt.resume(self.componentHolder.lifecycle) }
            .onDisappear { LifecycleRegistryExtKt.stop(self.componentHolder.lifecycle) }
    }
}

let externalUriHandler: (String) -> () = { url in
    guard let url = URL(string: url) else { return }
    UIApplication.shared.open(url, options: [:], completionHandler: { _ in })
}

struct ContentView_Previews: PreviewProvider {
    static var previews: some View {
        ContentView(externalUriHandler)
    }
}

import SwiftUI
import Hackernews

@main
struct HackernewsApp: App {
    
    @State
    var url: URL? = nil

    var body: some Scene {
        WindowGroup {
            ContentView({ url in
                self.url = URL(string: url)!
            })
            .fullScreenCover(item: $url) {
                self.url = nil
            } content: { url in
                SafariView(url: url)
                    .background(theme.colors.primaryVariant.edgesIgnoringSafeArea(.vertical))
            }
        }
    }
}

extension URL: Identifiable {
    public var id: String { absoluteString }
}

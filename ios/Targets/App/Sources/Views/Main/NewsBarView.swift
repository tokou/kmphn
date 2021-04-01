import SwiftUI
import Hackernews

struct NewsBarView: View {
    let onRefresh: () -> ()

    var body: some View {
        HStack {
            Text("Hacker News")
            Button("Refresh", action: onRefresh)
        }
    }
}

struct NewsBarView_Previews: PreviewProvider {
    static var previews: some View {
        NewsBarView(onRefresh: {})
    }
}

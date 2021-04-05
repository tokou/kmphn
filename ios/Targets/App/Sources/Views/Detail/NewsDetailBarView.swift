import SwiftUI
import Hackernews

struct NewsDetailBarView: View {
    let onBack: () -> ()

    var body: some View {
        TopAppBar(
            title: { Spacer() },
            navigation: {
                BarButton(systemName: "chevron.left", action: onBack)
            },
            actions: {
                HStack(spacing: 0) {
                    BarButton(systemName: "square.and.arrow.up", action: {})
                    BarButton(systemName: "bookmark", action: {})
                    BarButton(systemName: "ellipsis", action: {})
                }
            }
        )
    }
}

struct NewsDetailBarView_Previews: PreviewProvider {
    static var previews: some View {
        NewsDetailBarView(onBack: {})
    }
}

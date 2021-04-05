import SwiftUI
import Hackernews

struct NewsBarView: View {
    let onRefresh: () -> ()

    var body: some View {
        TopAppBar(
            title: {
                Text("Hacker News")
                    .font(theme.typography.subtitle1)
                    .foregroundColor(theme.colors.onPrimary)
                    .frame(maxWidth: .infinity)
            },
            navigation: { Spacer().frame(maxWidth: buttonSize * 2) },
            actions: {
                HStack(spacing: 0) {
                    BarButton(systemName: "arrow.clockwise", action: onRefresh)
                    BarButton(systemName: "ellipsis", action: {})
                }
            }
        )
    }
}

struct NewsBarView_Previews: PreviewProvider {
    static var previews: some View {
        NewsBarView(onRefresh: {})
    }
}

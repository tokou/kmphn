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
                    .padding(.leading, 55)
            },
            navigation: { EmptyView() },
            actions: {
                Button(action: onRefresh) {
                    Image(systemName: "arrow.2.circlepath")
                        .foregroundColor(theme.colors.onPrimary.mediumOpacity())
                        .padding(16)
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

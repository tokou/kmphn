import SwiftUI
import Hackernews

let buttonSize: CGFloat = 44

struct NewsBarView: View {
    let onRefresh: () -> ()

    var body: some View {
        TopAppBar(
            title: {
                Text("Hacker News")
                    .font(theme.typography.subtitle1)
                    .foregroundColor(theme.colors.onPrimary)
                    .frame(maxWidth: .infinity)
                    .padding(.leading, buttonSize * 2)
            },
            navigation: { EmptyView() },
            actions: {
                HStack(spacing: 0) {
                    Button(action: onRefresh) {
                        Image(systemName: "arrow.clockwise")
                            .foregroundColor(theme.colors.onPrimary)
                    }.frame(minWidth: buttonSize, minHeight: buttonSize)
                    Button(action: {}) {
                        Image(systemName: "ellipsis")
                            .foregroundColor(theme.colors.onPrimary)
                    }.frame(minWidth: buttonSize, minHeight: buttonSize)
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

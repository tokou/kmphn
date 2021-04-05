import SwiftUI
import Hackernews

struct NewsDetailBarView: View {
    let onBack: () -> ()

    var body: some View {
        TopAppBar(
            title: { Spacer() },
            navigation: {
                Button(action: onBack) {
                    Image(systemName: "chevron.left")
                        .foregroundColor(theme.colors.onPrimary)
                }.frame(minWidth: buttonSize, minHeight: buttonSize)
            },
            actions: {
                HStack(spacing: 0) {
                    Button(action: {}) {
                        Image(systemName: "square.and.arrow.up")
                            .foregroundColor(theme.colors.onPrimary)
                    }.frame(minWidth: buttonSize, minHeight: buttonSize)
                    Button(action: {}) {
                        Image(systemName: "bookmark")
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

struct NewsDetailBarView_Previews: PreviewProvider {
    static var previews: some View {
        NewsDetailBarView(onBack: {})
    }
}

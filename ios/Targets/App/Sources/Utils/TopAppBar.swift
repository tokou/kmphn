import SwiftUI

let topBarHeight: CGFloat = 44

struct TopAppBar<Title, Navigation, Actions> : View where Title : View, Navigation : View, Actions : View {
    let title: () -> Title
    let navigation: () -> Navigation
    let actions: () -> Actions

    var body: some View {
        HStack(spacing: 0) {
            navigation()
            title()
            actions()
        }
        .frame(maxWidth: .infinity)
        .frame(height: topBarHeight)
        .background(theme.colors.primary)
        .background(theme.colors.primaryVariant.edgesIgnoringSafeArea(.top).shadow(radius: 10))
    }
}

struct TopAppBar_Previews: PreviewProvider {
    static var previews: some View {
        VStack {
            TopAppBar<Text, EmptyView, EmptyView>(
                title: {
                    Text("Hacker News")
                        .font(theme.typography.subtitle1)
                        .foregroundColor(theme.colors.onPrimary)
                },
                navigation: { EmptyView() },
                actions: { EmptyView() }
            )
            Spacer()
        }
        .previewDevice(.init(unicodeScalarLiteral: "iPhone 12 Pro Max"))
        VStack {
            TopAppBar<Text, EmptyView, EmptyView>(
                title: { Text("Hacker News") },
                navigation: { EmptyView() },
                actions: { EmptyView() }
            )
            Spacer()
        }
        .previewDevice(.init(unicodeScalarLiteral: "iPhone 8"))
    }
}

import SwiftUI
import Hackernews

let commentPaddingStep: CGFloat = 16

struct CommentPaddingView<Content>: View where Content: View {
    let padding: CGFloat
    let content: () -> Content
    
    var body: some View {
        HStack {
            content()
                .background(theme.colors.background)
                .padding(.leading, padding)
        }
        .background(theme.colors.onBackground.opacity(0.05))
    }
}

struct CommentPaddingView_Previews: PreviewProvider {
    static var previews: some View {
        CommentPaddingView(padding: 32) {
            Text("Hello")
        }
        .previewLayout(.sizeThatFits)
    }
}

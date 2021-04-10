import SwiftUI
import Hackernews

let commentPaddingStep: CGFloat = 16

struct CommentPaddingView<Content>: View where Content: View {
    let padding: CGFloat
    let content: () -> Content
    
    var body: some View {
        HStack(spacing: 0) {
            let steps = Int(padding / commentPaddingStep)
            ForEach(0..<steps) { i in
                Rectangle()
                    .foregroundColor(theme.colors.onBackground.opacity(0.05))
                    .frame(width: commentPaddingStep - 1)
                    .padding(.leading, 1)
            }
            content()
        }
    }
}

struct CommentPaddingView_Previews: PreviewProvider {
    static var previews: some View {
        CommentPaddingView(padding: 16 * 5) {
            Text("Hello").padding(16)
        }
        .frame(maxHeight: 48)
        .previewLayout(.sizeThatFits)
    }
}

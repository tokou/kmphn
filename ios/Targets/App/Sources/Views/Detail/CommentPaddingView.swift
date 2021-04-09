import SwiftUI
import Hackernews

struct CommentPaddingView<Content>: View where Content: View {
    let padding: CGFloat
    let content: () -> Content
    
    var body: some View {
        content()
            .padding(.leading, padding)
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

import SwiftUI
import Hackernews

struct CommentTimeView: View {
    let time: String
    
    var body: some View {
        HStack(spacing: 4) {
            Image(systemName: "hourglass")
            Text(time)
                .font(theme.typography.body1)
        }
        .opacity(mediumOpacity)
    }
}

struct CommentTimeView_Previews: PreviewProvider {
    static var previews: some View {
        CommentTimeView(time: "3 hours")
            .previewLayout(.sizeThatFits)
    }
}

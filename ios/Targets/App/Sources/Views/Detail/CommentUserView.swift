import SwiftUI
import Hackernews

struct CommentUserView: View {
    let user: String
    let isOp: Bool
    let onUserClick: (UserId) -> ()
    
    var body: some View {
        let color = isOp ? theme.colors.primary : Color.clear
        let textColor = isOp ? theme.colors.onPrimary : theme.colors.primary.mediumOpacity()
        let padding: CGFloat = isOp ? 4 : 0

        HStack(spacing: 4) {
            if (!isOp) {
                Image(systemName: "person.fill")
                    .foregroundColor(theme.colors.primary.mediumOpacity())
            }
            Text(user)
                .font(theme.typography.body1)
                .foregroundColor(textColor)
                .padding(padding)
                .background(color)
        }
    }
}

struct CommentUserView_Previews: PreviewProvider {
    static var previews: some View {
        CommentUserView(user: "user", isOp: true, onUserClick: { _ in })
            .previewLayout(.sizeThatFits)
        CommentUserView(user: "user", isOp: false, onUserClick: { _ in })
            .previewLayout(.sizeThatFits)
    }
}

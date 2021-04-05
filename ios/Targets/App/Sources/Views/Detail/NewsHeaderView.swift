import SwiftUI
import Hackernews

struct NewsHeaderView: View {
    let header: NewsDetailHeader
    let onLinkClicked: (String, Bool) -> ()
    let onUserClicked: (UserId) -> ()

    var body: some View {
        VStack(alignment: .leading, spacing: 0) {
            VStack(alignment: .leading, spacing: 0) {
                Text("\(header.title)")
                    .font(theme.typography.subtitle1)
                    .foregroundColor(theme.colors.onPrimary)
                    .frame(maxWidth: .infinity, alignment: .leading)
                Spacer()
                    .frame(height: 14)
                HStack(spacing: 12) {
                    Text(header.points)
                        .padding(4)
                        .background(theme.colors.primaryVariant.mediumOpacity())
                    Button(header.user) { onUserClicked(header.user) }
                    Text(header.time)
                    HStack(spacing: 4) {
                        Image(systemName: "message")
                        Text(header.commentsCount)
                    }
                }
                .font(theme.typography.body2)
                Spacer()
                    .frame(height: 14)
                if let link = header.link {
                    HStack(spacing: 4) {
                        Image(systemName: "arrow.up.right.square")
                        Button(link) { onLinkClicked(link, false) }
                            .lineLimit(1)
                    }
                    Spacer()
                        .frame(height: 8)
                }
                HStack(spacing: 4) {
                    Image(systemName: "arrow.up.right.square")
                    Button(header.hnLink) { onLinkClicked(header.hnLink, true) }
                        .lineLimit(1)
                }
            }
            .padding(16)
            .foregroundColor(theme.colors.onPrimary.mediumOpacity())
            .buttonStyle(PlainButtonStyle())
            if let text = header.text {
                RichText(text: text, onLinkClicked: onLinkClicked)
                    .padding(16)
                    .frame(maxWidth: .infinity, alignment: .leading)
                    .background(theme.colors.surface.mediumOpacity())
                Spacer()
                    .frame(height: 8)
            }
        }
        .background(theme.colors.primary)
    }
}

struct NewsHeaderView_Previews: PreviewProvider {
    static let headerStub = NewsDetailHeader(id: 1, title: "Title", link: "https://hello.com", user: "user", time: "1 hour", commentsCount: "12", points: "42", text: [])
    
    static var previews: some View {
        NewsHeaderView(header: headerStub, onLinkClicked: { _, _ in }, onUserClicked: { _ in })
            .previewLayout(.fixed(width: 360, height: 260))
    }
}

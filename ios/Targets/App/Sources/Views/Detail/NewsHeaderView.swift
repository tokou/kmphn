import SwiftUI
import Hackernews

struct NewsHeaderView: View {
    let header: NewsDetailHeader
    let onLinkClicked: (String, Bool) -> ()
    let onUserClicked: (UserId) -> ()

    var body: some View {
        VStack(alignment: .leading, spacing: 0) {
            VStack(alignment: .leading, spacing: 0) {
                if let title = header.title {
                    Text(title)
                        .font(theme.typography.subtitle1)
                        .foregroundColor(theme.colors.onPrimary)
                        .frame(maxWidth: .infinity, alignment: .leading)
                    Spacer()
                        .frame(height: 14)
                }
                HStack(spacing: 12) {
                    if let points = header.points {
                        Text(points)
                            .padding(4)
                            .background(theme.colors.primaryVariant.mediumOpacity())
                    }
                    Button(header.user) { onUserClicked(header.user) }
                    Text(header.time)
                    if let count = header.commentsCount {
                        HStack(spacing: 4) {
                            Image(systemName: "message")
                            Text(count)
                        }
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
                RichTextView(text: text, onLinkClicked: onLinkClicked)
                    .padding(16)
                    .background(theme.colors.surface.mediumOpacity())
                Spacer()
                    .frame(height: 8)
            }
        }
        .background(theme.colors.primary)
    }
}

struct NewsHeaderView_Previews: PreviewProvider {
    static let headerStub = NewsDetailHeader(id: 1, title: "Title", link: "https://hello.com", user: "user", time: "1 hour", commentsCount: "12", points: "42", text: nil)
    static let commentStub = NewsDetailHeader(id: 1, title: nil, link: nil, user: "user", time: "1 hour", commentsCount: "12", points: "42", text: [NewsDetailText.Plain(text: "NewsDetailHeader(id: 1, title: Title link: https://hello.com, user: user, time: 1 hour, commentsCount: 12, points: 42, text: nil)")])

    static var previews: some View {
        Group {
            NewsHeaderView(header: headerStub, onLinkClicked: { _, _ in }, onUserClicked: { _ in })
                .previewLayout(.fixed(width: 360, height: 260))
            NewsHeaderView(header: commentStub, onLinkClicked: { _, _ in }, onUserClicked: { _ in })
                .previewLayout(.fixed(width: 360, height: 260))
        }
    }
}

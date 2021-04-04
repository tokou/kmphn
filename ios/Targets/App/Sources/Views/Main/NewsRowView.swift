import SwiftUI
import Hackernews

struct NewsRowView: View {
    let item: NewsMainItem
    let onItemClick: Callback
    let onLinkClick: Callback
    
    @State var height: CGFloat = 100

    var body: some View {
        HStack(spacing: 0) {
            LeftColumnView(item: item, onItemClick: onLinkClick)
                .modifier(HeightModifier())
            RightColumnView(item: item, onItemClick: onItemClick)
                .background(veryLightGray.opacity(0.2))
                .modifier(HeightModifier())
                .frame(height: height)
        }
        .onPreferenceChange(ViewHeightKey.self) {
            self.height = $0
        }
    }
}

private struct LeftColumnView: View {
    let item: NewsMainItem
    let onItemClick: Callback

    var body: some View {
        Button(action: { onItemClick(item) }) {
            VStack(alignment: .leading, spacing: 0) {
                Text(item.title)
                    .fixedSize(horizontal: false, vertical: true)
                    .font(theme.typography.subtitle1)
                Spacer()
                    .frame(height: 16)
                    .frame(maxWidth: .infinity)
                HStack(spacing: 0) {
                    Text(item.user)
                        .lineLimit(1)
                        .font(theme.typography.subtitle2)
                        .foregroundColor(theme.colors.onSurface.mediumOpacity())
                    Spacer()
                        .frame(width: 16)
                    Text(item.time)
                        .lineLimit(1)
                        .font(theme.typography.subtitle2)
                        .foregroundColor(theme.colors.onSurface.mediumOpacity())
                }
                if let link = item.link {
                    Spacer()
                        .frame(height: 8)
                    Text(link)
                        .lineLimit(1)
                        .font(theme.typography.subtitle2)
                        .foregroundColor(theme.colors.onSurface.mediumOpacity())
                }
            }
            .padding(16)
            .frame(maxWidth: .infinity)
        }
    }
}

private struct RightColumnView: View {
    let item: NewsMainItem
    let onItemClick: Callback

    var body: some View {
        Button(action: { onItemClick(item) }) {
            VStack {
                Text(item.comments)
                    .lineLimit(1)
                    .font(theme.typography.subtitle2)
                    .foregroundColor(theme.colors.primary.mediumOpacity())
                Spacer()
                Text(item.points)
                    .lineLimit(1)
                    .font(theme.typography.subtitle2)
                    .foregroundColor(theme.colors.onSurface.mediumOpacity())
            }
            .padding(.vertical, 16)
            .frame(minWidth: 64)
        }
    }
}

struct NewsRowView_Previews: PreviewProvider {
    static func item(
        id: Int64 = 1,
        title: String = "enkiTS: A C and C++ Task Scheduler for creating parallel programs",
        link: String? = "https://mmartinfahy.medium.com/my-experience-releasing-3-failed-saas-products-44e61cbde424",
        user: String = "user",
        time: String = "now",
        comments: String = "12",
        points: String = "42"
    ) -> NewsMainItem {
        NewsMainItem(id: id, title: title, link: link, user: user, time: time, comments: comments, points: points)
    }

    static var previews: some View {
        VStack {
            NewsRowView(item: item(title: "My experience releasing failed SaaS products"), onItemClick: { print($0) }, onLinkClick: { print($0) })
            Divider()
            NewsRowView(item: item(link: nil, comments: "1234"), onItemClick: { print($0) }, onLinkClick: { print($0) })
        }
        .previewLayout(.sizeThatFits)
    }
}

struct RightColumnView_Previews: PreviewProvider {
    
    static var previews: some View {
        RightColumnView(item: NewsRowView_Previews.item(), onItemClick: { print($0) })
            .previewLayout(.fixed(width: 64, height: 100))
        RightColumnView(item: NewsRowView_Previews.item(comments: "1000", points: "0"), onItemClick: { print($0) })
            .previewLayout(.fixed(width: 64, height: 100))
    }
}

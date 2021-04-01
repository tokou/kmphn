import SwiftUI
import Hackernews

struct NewsRowView: View {
    let item: NewsMainItem
    let onItemClick: Callback
    let onLinkClick: Callback
    
    var body: some View {
        HStack {
            Text("\(item.title)\n(\(item.link ?? ""))\n")
                .onTapGesture { onLinkClick(item) }
            Button("\(item.comments)") { onItemClick(item) }
        }
    }
}

struct NewsRowView_Previews: PreviewProvider {
    static let itemStub = NewsMainItem(id: 1, title: "Title", link: "link.com", user: "user", time: "now", comments: "12", points: "42")
    
    static var previews: some View {
        NewsRowView(item: itemStub, onItemClick: { _ in }, onLinkClick: { _ in })
    }
}

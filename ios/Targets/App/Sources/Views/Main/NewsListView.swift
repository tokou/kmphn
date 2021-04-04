import SwiftUI
import Hackernews

struct NewsListView: View {
    let items: [NewsMainItem]
    let onLinkClick: Callback
    let onItemClick: Callback

    var body: some View {
        List {
            ForEach(items) { item in
                NewsRowView(item: item, onItemClick: onItemClick, onLinkClick: onLinkClick)
                    .listRowInsets(.init())
            }
        }
        .listStyle(PlainListStyle())
        .buttonStyle(PlainButtonStyle())
    }
}

struct NewsListView_Previews: PreviewProvider {
    static let itemStubs = [NewsRowView_Previews.item()]
    
    static var previews: some View {
        NewsListView(items: itemStubs, onLinkClick: { _ in }, onItemClick: { _ in })
    }
}

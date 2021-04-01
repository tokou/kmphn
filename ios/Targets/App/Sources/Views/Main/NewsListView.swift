import SwiftUI
import Hackernews

struct NewsListView: View {
    let items: [NewsMainItem]
    let onLinkClick: Callback
    let onItemClick: Callback

    var body: some View {
        List(items) { item in
            NewsRowView(item: item, onItemClick: onItemClick, onLinkClick: onLinkClick)
        }
        .buttonStyle(PlainButtonStyle())
    }
}

struct NewsListView_Previews: PreviewProvider {
    static let itemStubs = [NewsRowView_Previews.itemStub]
    
    static var previews: some View {
        NewsListView(items: itemStubs, onLinkClick: { _ in }, onItemClick: { _ in })
    }
}

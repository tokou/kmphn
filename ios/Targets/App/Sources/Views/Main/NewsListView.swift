import SwiftUI
import Hackernews

struct NewsListView: View {
    let items: [NewsMainItem]
    let onLinkClick: Callback
    let onItemClick: Callback
    let canLoadMore: Bool
    let isLoadingMore: Bool
    let onLoadMore: () -> ()

    var body: some View {
        ScrollView {
            LazyVStack(spacing: 0) {
                ForEach(items) { item in
                    NewsRowView(item: item, onItemClick: onItemClick, onLinkClick: onLinkClick)
                    Divider()
                }
                if canLoadMore {
                    LoadMoreView(isLoadingMore: isLoadingMore, onLoadMore: onLoadMore)
                }
            }
        }
        .buttonStyle(PlainButtonStyle())
    }
}

struct NewsListView_Previews: PreviewProvider {
    static let itemStubs = [NewsRowView_Previews.item()]
    
    static var previews: some View {
        NewsListView(items: itemStubs, onLinkClick: { _ in }, onItemClick: { _ in }, canLoadMore: true, isLoadingMore: false, onLoadMore: {})
    }
}

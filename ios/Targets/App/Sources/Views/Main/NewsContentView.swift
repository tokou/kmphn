import SwiftUI
import Hackernews

extension NewsMainItem : Identifiable {}

typealias Callback = (NewsMainItem) -> ()

struct NewsContentView: View {
    let model: NewsMainModel
    let onSelected: (ItemId, String?) -> ()
    let onSecondarySelected: (ItemId) -> ()
    let onRefresh: () -> ()
    let onLoadMore: () -> ()

    var body: some View {
        switch model {
        case let content as NewsMainModel.Content:
            NewsListView(
                items: content.items,
                onLinkClick: { onSelected($0.id, $0.link) },
                onItemClick: { onSecondarySelected($0.id) },
                canLoadMore: content.canLoadMore,
                isLoadingMore: content.isLoadingMore,
                onLoadMore: onLoadMore
            )
        case is NewsMainModel.Loading:
            LoaderView()
        case is NewsMainModel.Error:
            ErrorView(onClick: onRefresh)
        default:
            fatalError()
        }
    }
}

struct NewsContentView_Previews: PreviewProvider {
    static func preview(_ model: NewsMainModel) -> NewsContentView {
        NewsContentView(
            model: model,
            onSelected: { _, _ in },
            onSecondarySelected: { _ in },
            onRefresh: {},
            onLoadMore: {}
        )
    }
    
    static var previews: some View {
        preview(NewsMainView_Previews.contentStub)
        preview(NewsMainView_Previews.loadingStub)
        preview(NewsMainView_Previews.errorStub)
    }
}

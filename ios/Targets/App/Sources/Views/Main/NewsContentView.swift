import SwiftUI
import Hackernews

extension NewsMainItem : Identifiable {}

typealias ItemId = Int64
typealias Callback = (NewsMainItem) -> ()

struct NewsContentView: View {
    let model: NewsMainModel
    let onSelected: (ItemId, String?) -> ()
    let onSecondarySelected: (ItemId) -> ()
    let onRefresh: () -> ()
    
    var body: some View {
        switch model {
        case is NewsMainModel.Content:
            // case let content is NewsMainModel.Content causes a segfault
            let content = model as! NewsMainModel.Content
            NewsListView(
                items: content.items,
                onLinkClick: { onSelected($0.id, $0.link) },
                onItemClick: { onSecondarySelected($0.id) }
            )
        case is NewsMainModel.Loading:
            LoaderView()
        case is NewsMainModel.Error:
            ErrorView(text: "Error", onClick: onRefresh)
        default:
            fatalError()
        }
    }
}

struct NewsContentView_Previews: PreviewProvider {
    static func preview(_ model: NewsMainModel) -> NewsContentView {
        return NewsContentView(model: model, onSelected: { _, _ in }, onSecondarySelected: { _ in }, onRefresh: {})
    }
    
    static var previews: some View {
        preview(NewsMainView_Previews.contentStub)
        preview(NewsMainView_Previews.loadingStub)
        preview(NewsMainView_Previews.errorStub)
    }
}

import SwiftUI

struct LoadMoreView: View {
    let isLoadingMore: Bool
    let onLoadMore: () -> ()
    
    var body: some View {
        ZStack {
            if isLoadingMore {
                LoaderView()
            } else {
                Button("Load More", action: onLoadMore)
                    .font(theme.typography.subtitle1)
                    .frame(maxWidth: .infinity, maxHeight: .infinity)
                    .foregroundColor(theme.colors.onSurface.mediumOpacity())
            }
        }
        .background(veryLightGray.opacity(0.2))
        .frame(height: 64)
    }
}

struct LoadMoreView_Previews: PreviewProvider {
    static var previews: some View {
        LoadMoreView(isLoadingMore: false, onLoadMore: {})
            .previewLayout(.sizeThatFits)
        LoadMoreView(isLoadingMore: true, onLoadMore: {})
            .previewLayout(.sizeThatFits)
    }
}

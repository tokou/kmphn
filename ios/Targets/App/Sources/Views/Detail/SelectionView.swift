import SwiftUI
import Hackernews

struct SelectionView<Content> : View where Content : View {
    let isSelected: Bool
    let content: () -> Content

    var body: some View {
        let selectionWidth: CGFloat? = isSelected ? 4 : 0
        let selection = Rectangle()
            .frame(width: selectionWidth, height: nil, alignment: .leading)
            .foregroundColor(theme.colors.primary)
        content()
            .overlay(selection, alignment: .leading)
    }
}

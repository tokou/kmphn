import SwiftUI

struct ViewHeightKey: PreferenceKey {
    typealias Value = CGFloat
    static var defaultValue: Value { 0 }
    static func reduce(value: inout Value, nextValue: () -> Value) {
        value = max(value, nextValue())
    }
}

struct HeightModifier : ViewModifier {
    private var heightView: some View {
        GeometryReader { geometry in
            Color.clear.preference(
                key: ViewHeightKey.self,
                value: geometry.frame(in: .local).size.height
            )
        }
    }
    
    func body(content: Content) -> some View {
        content.background(heightView)
    }
}

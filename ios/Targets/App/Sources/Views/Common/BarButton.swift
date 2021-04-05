import SwiftUI

let buttonSize: CGFloat = 44

struct BarButton: View {
    let systemName: String
    let action: () -> ()

    var body: some View {
        Button(action: action) {
            Image(systemName: systemName)
                .foregroundColor(theme.colors.onPrimary)
        }
        .frame(minWidth: buttonSize, minHeight: buttonSize)
    }
}

struct BarButton_Previews: PreviewProvider {
    static var previews: some View {
        BarButton(systemName: "square.and.arrow.up", action: {})
    }
}

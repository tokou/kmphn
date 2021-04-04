import SwiftUI
import Hackernews

struct ErrorView: View {
    let text: String
    let actionTitle: String = "Retry"
    let onClick: () -> ()
    
    var body: some View {
        VStack {
            Text("Error")
            Button(actionTitle, action: onClick)
        }.frame(maxWidth: .infinity, maxHeight: .infinity)
    }
}

struct ErrorView_Previews: PreviewProvider {
    static var previews: some View {
        ErrorView(text: "Error", onClick: {})
    }
}

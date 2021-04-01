import SwiftUI
import Hackernews

struct ErrorView: View {
    let text: String
    let actionTitle: String = "Retry"
    let onClick: () -> ()
    
    var body: some View {
        Text("")
    }
}

struct ErrorView_Previews: PreviewProvider {
    static var previews: some View {
        ErrorView(text: "Error", onClick: {})
    }
}

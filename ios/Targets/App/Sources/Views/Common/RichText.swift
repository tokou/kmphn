import SwiftUI
import Hackernews

struct RichText: View {
    let text: [NewsDetailText]
    let onLinkClicked: (String, Bool) -> ()
    
    var body: some View {
        Text("RichText")
    }
}

struct RichText_Previews: PreviewProvider {
    static var previews: some View {
        RichText(text: [], onLinkClicked: { _, _ in })
    }
}

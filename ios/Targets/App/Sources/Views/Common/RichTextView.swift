import SwiftUI
import Hackernews

extension NewsDetailText: Identifiable {
    public var id: String { text + String(describing: self) }
}

struct RichTextView : View {
    let text: [NewsDetailText]
    let onLinkClicked: (String, Bool) -> ()

    var body: some View {
        text.map { t in
            switch t {
            case let t as NewsDetailText.Plain:
                return Text(t.text)
            case let t as NewsDetailText.Emphasis:
                return Text(t.text)
                    .italic()
            case let t as NewsDetailText.Code:
                return Text(t.text)
                    .font(.system(.callout, design: .monospaced))
            case let t as NewsDetailText.Link:
                return Text(t.text)
                    .underline()
                    .foregroundColor(.blue)
            default: fatalError("Unkown span type")
            }
        }.reduce(Text("")) { result, text in result + text }
    }
}

struct RichText_Previews: PreviewProvider {
    static let stubText = [
        NewsDetailText.Plain(text: "Hello there,\n"),
        NewsDetailText.Emphasis(text: "Hello there,\n"),
        NewsDetailText.Link(text: "https://www.spacejam.com/\n", link: "https://www.spacejam.com/"),
        NewsDetailText.Code(text: "Hmm in teresting"),
        NewsDetailText.Plain(text: "\nThe goal is to get a UILabel integrated via UIViewRepresentable to size the same way Text does - use the available width and wrap to multiple lines to fit all the text thus increasing the height of the HStack it's in, instead of expanding in width infinitely. This is very similar to this question, though the accepted answer does not work for the layout I'm using involving a ScrollView, VStack, and HStack.")
    ]

    static var previews: some View {
        RichTextView(
            text: stubText,
            onLinkClicked: { _, _ in }
        )
        .previewLayout(.sizeThatFits)
        List {
            RichTextView(text: stubText, onLinkClicked: { l, _ in print(l) })
            RichTextView(text: stubText, onLinkClicked: { l, _ in print(l) })
        }
    }
}

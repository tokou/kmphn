import SwiftUI
import Hackernews

struct RichTextView: View {
    let text: [NewsDetailText]
    let onLinkClicked: (String, Bool) -> ()

    @State private var height: CGFloat = 0

    var body: some View {
        InternalRichText(
            text: text,
            onLinkClicked: onLinkClicked,
            dynamicHeight: $height
        )
        .frame(minHeight: height)
    }
}

private struct InternalRichText: UIViewRepresentable {
    let text: [NewsDetailText]
    let onLinkClicked: (String, Bool) -> ()
    
    @Binding var dynamicHeight: CGFloat

    func makeUIView(context: UIViewRepresentableContext<Self>) -> UILabel {
        let label = UILabel()
        label.lineBreakMode = .byWordWrapping
        label.numberOfLines = 0
        label.setContentCompressionResistancePriority(.defaultLow, for: .horizontal)
        return label
    }
    
    private func buildAttributedString() -> NSAttributedString {
        let attributedString = NSMutableAttributedString()
        for t in text {
            switch t {
            case let t as NewsDetailText.Plain:
                attributedString.append(.init(string: t.text, attributes: [.font: UIFont.preferredFont(forTextStyle: .callout)]))
            case let t as NewsDetailText.Emphasis:
                attributedString.append(.init(string: t.text, attributes: [.font: UIFont.preferredFont(forTextStyle: .callout), .obliqueness: 0.2]))
            case let t as NewsDetailText.Code:
                attributedString.append(.init(string: t.text, attributes: [.font: UIFont.monospacedSystemFont(ofSize: 16, weight: .regular)]))
            case let t as NewsDetailText.Link:
                attributedString.append(.init(string: t.text, attributes: [.link: t.link, .font: UIFont.preferredFont(forTextStyle: .callout)]))
            default: fatalError("Unkown span type")
            }
        }
        return attributedString
    }
        
    func updateUIView(_ uiView: UILabel, context: Context) {
        uiView.attributedText = buildAttributedString()
        DispatchQueue.main.async {
            let size = CGSize(width: uiView.bounds.width, height: CGFloat.greatestFiniteMagnitude)
            dynamicHeight = uiView.sizeThatFits(size).height
        }
    }
}

struct RichText_Previews: PreviewProvider {
    static var previews: some View {
        RichTextView(
            text: [
                NewsDetailText.Plain(text: "Hello there,\n"),
                NewsDetailText.Emphasis(text: "Hello there,\n"),
                NewsDetailText.Link(text: "https://www.spacejam.com/\n", link: "https://www.spacejam.com/"),
                NewsDetailText.Code(text: "Hmm in teresting")
            ],
            onLinkClicked: { _, _ in }
        )
        .previewLayout(.sizeThatFits)
    }
}

import SwiftUI
import Hackernews

class TapabbleLabel: UILabel {

    let layoutManager = NSLayoutManager()
    let textContainer = NSTextContainer(size: CGSize.zero)
    var textStorage = NSTextStorage() {
        didSet {
            textStorage.addLayoutManager(layoutManager)
        }
    }

    var onLinkTapped: (URL) -> () = { _ in }

    let tapGesture = UITapGestureRecognizer()

    override var attributedText: NSAttributedString? {
        didSet {
            if let attributedText = attributedText {
                textStorage = NSTextStorage(attributedString: attributedText)
            } else {
                textStorage = NSTextStorage()
            }
        }
    }

    override var lineBreakMode: NSLineBreakMode {
        didSet {
            textContainer.lineBreakMode = lineBreakMode
        }
    }

    override var numberOfLines: Int {
        didSet {
            textContainer.maximumNumberOfLines = numberOfLines
        }
    }

    required init?(coder aDecoder: NSCoder) {
        super.init(coder: aDecoder)
        setUp()
    }

    override init(frame: CGRect) {
        super.init(frame: frame)
        setUp()
    }

    func setUp() {
        isUserInteractionEnabled = true
        layoutManager.addTextContainer(textContainer)
        textContainer.lineFragmentPadding = 0
        textContainer.lineBreakMode = lineBreakMode
        textContainer.maximumNumberOfLines = numberOfLines
        tapGesture.addTarget(self, action: #selector(labelTapped))
        addGestureRecognizer(tapGesture)
    }

    override func layoutSubviews() {
        super.layoutSubviews()
        textContainer.size = bounds.size
    }

    @objc func labelTapped() {
        guard tapGesture.state == .ended else {
            return
        }

        let locationOfTouch = tapGesture.location(in: tapGesture.view)
        let textBoundingBox = layoutManager.usedRect(for: textContainer)
        let textContainerOffset = CGPoint(
            x: (bounds.width - textBoundingBox.width) / 2 - textBoundingBox.minX,
            y: (bounds.height - textBoundingBox.height) / 2 - textBoundingBox.minY
        )
        let locationOfTouchInTextContainer = CGPoint(
            x: locationOfTouch.x - textContainerOffset.x,
            y: locationOfTouch.y - textContainerOffset.y
        )
        let indexOfCharacter = layoutManager.characterIndex(
            for: locationOfTouchInTextContainer,
            in: textContainer,
            fractionOfDistanceBetweenInsertionPoints: nil
        )
        onCharacterTapped(label: self, characterIndex: indexOfCharacter)
    }
    
    func onCharacterTapped(label: UILabel, characterIndex: Int) {
        if
            let attribute = label.attributedText?.attribute(NSAttributedString.Key.link, at: characterIndex, effectiveRange: nil) as? String,
            let url = URL(string: attribute)
        {
            self.onLinkTapped(url)
        }
    }
}

struct RichTextView: UIViewRepresentable {
    let text: [NewsDetailText]
    let onLinkClicked: (String, Bool) -> ()

    func makeUIView(context: UIViewRepresentableContext<Self>) -> UILabel {
        let label = TapabbleLabel()
        label.lineBreakMode = .byWordWrapping
        label.numberOfLines = 0
        label.setContentCompressionResistancePriority(.defaultLow, for: .horizontal)
        label.setContentHuggingPriority(.defaultHigh, for: .vertical)
        label.onLinkTapped = { url in onLinkClicked(url.absoluteString, false) }
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

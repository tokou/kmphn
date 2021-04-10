import SwiftUI

fileprivate let safetyOrange = Color(hue: 24/360, saturation: 1, brightness: 1)
fileprivate let tenne = Color(hue: 24/360, saturation: 1, brightness: 0.74)
fileprivate let nero = Color(hue: 0, saturation: 1, brightness: 0.16)
fileprivate let carmine = Color(hue: 350/360, saturation: 1, brightness: 0.69)
let veryLightGray = Color(hue: 0, saturation: 0, brightness: 0.8)

let mediumOpacity: Double = 0.6

extension Color {
    func mediumOpacity() -> Color {
        self.opacity(0.6)
    }
}

struct Theme {
    let colors = Colors()
    let typography = Typography()
    
    struct Colors {
        let primary = safetyOrange
        let primaryVariant = tenne
        let surface = Color.white
        let background = Color.white
        let onPrimary = Color.white
        let onSurface = Color.black
        let onBackground = Color.black
        let error = carmine
    }
    
    struct Typography {
        let subtitle1 = Font.system(.body)
        let subtitle2 = Font.system(.callout)
        let body1 = Font.system(.body)
        let body2 = Font.system(.callout)
    }
}

let theme = Theme()

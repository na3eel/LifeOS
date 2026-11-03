import SwiftUI
import UIKit

/// Semantic Strata palette. Light values match docs/design/lifeos-approved-theme.md exactly.
/// Dark values are a deliberate native adaptation; every semantic color follows system appearance.
extension Color {
    static let lifeOSBrand = strata(light: 0xDB5635, dark: 0xFF9A7E)
    static let lifeOSSecondary = strata(light: 0x9828F6, dark: 0xC99BFF)
    static let lifeOSCanvas = strata(light: 0xFFFBF7, dark: 0x171313)
    static let lifeOSCard = strata(light: 0xF3E8E1, dark: 0x211C24)
    static let lifeOSSurfaceMuted = strata(light: 0xF3E8E1, dark: 0x342B37)
    static let lifeOSSurfaceStrong = strata(light: 0xE9DDD6, dark: 0x463A49)
    static let lifeOSBorder = strata(light: 0xD6C5BC, dark: 0x655767)
    static let lifeOSInk = strata(light: 0x1F1724, dark: 0xF8F0F5)
    static let lifeOSInkSoft = strata(light: 0x443843, dark: 0xE0D5E3)
    static let lifeOSMuted = strata(light: 0x6F626B, dark: 0xB9AABF)
    static let lifeOSAccentSoft = strata(light: 0xFCE4DA, dark: 0x642B1C)
    static let lifeOSSecondarySoft = strata(light: 0xF0E4FA, dark: 0x4B2670)
    static let lifeOSDanger = strata(light: 0xB3261E, dark: 0xFFB4AB)
}

private func strata(light: UInt32, dark: UInt32) -> Color {
    Color(uiColor: UIColor { traits in
        let hex = traits.userInterfaceStyle == .dark ? dark : light
        return UIColor(
            red: CGFloat((hex >> 16) & 0xFF) / 255,
            green: CGFloat((hex >> 8) & 0xFF) / 255,
            blue: CGFloat(hex & 0xFF) / 255,
            alpha: 1
        )
    })
}

extension Color {
    init(hex: UInt32) {
        self.init(
            .sRGB,
            red: Double((hex >> 16) & 0xFF) / 255,
            green: Double((hex >> 8) & 0xFF) / 255,
            blue: Double(hex & 0xFF) / 255,
            opacity: 1
        )
    }
}

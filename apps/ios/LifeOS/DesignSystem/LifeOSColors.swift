import SwiftUI
import UIKit

/// Semantic Tirni palette. Light values match docs/design/lifeos-approved-theme.md exactly.
/// Dark values are a deliberate native adaptation; every semantic color follows system appearance.
extension Color {
    static let lifeOSBrand = tirni(light: 0xE5093B, dark: 0xFF6B86)
    static let lifeOSSecondary = tirni(light: 0xFF3D6A, dark: 0xFFFFA3B6)
    static let lifeOSCanvas = tirni(light: 0xFFFFFF, dark: 0x0F0B0D)
    static let lifeOSCard = tirni(light: 0xFFF3F5, dark: 0x1B1217)
    static let lifeOSSurfaceMuted = tirni(light: 0xFFFFE1E8, dark: 0x291820)
    static let lifeOSSurfaceStrong = tirni(light: 0xFFFFE1E8, dark: 0x3B202A)
    static let lifeOSBorder = tirni(light: 0xFFF1DDE2, dark: 0x5A3A45)
    static let lifeOSInk = tirni(light: 0xFF111827, dark: 0xFFFFF5F7)
    static let lifeOSInkSoft = tirni(light: 0xFF374151, dark: 0xFFE8D9DE)
    static let lifeOSMuted = tirni(light: 0xFF6B7280, dark: 0xFFC8B8BE)
    static let lifeOSAccentSoft = tirni(light: 0xFFFFE1E8, dark: 0xFF54202D)
    static let lifeOSSecondarySoft = tirni(light: 0xFFFFE1E8, dark: 0xFF54202D)
    static let lifeOSDanger = tirni(light: 0xFFB3261E, dark: 0xFFFFB4AB)
}

private func tirni(light: UInt32, dark: UInt32) -> Color {
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

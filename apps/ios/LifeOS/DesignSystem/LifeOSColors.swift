import SwiftUI

/// Semantic Strata palette. Keep synchronized with docs/design/lifeos-approved-theme.md.
extension Color {
    static let lifeOSBrand = Color(hex: 0xDB5635)
    static let lifeOSSecondary = Color(hex: 0x9828F6)
    static let lifeOSCanvas = Color(hex: 0xFFFBF7)
    static let lifeOSCard = Color(hex: 0xF3E8E1)
    static let lifeOSBorder = Color(hex: 0xD6C5BC)
    static let lifeOSInk = Color(hex: 0x1F1724)
    static let lifeOSMuted = Color(hex: 0x6F626B)
    static let lifeOSAccentSoft = Color(hex: 0xFCE4DA)
    static let lifeOSSecondarySoft = Color(hex: 0xF0E4FA)
}

extension Color {
    fileprivate init(hex: UInt32) {
        self.init(
            .sRGB,
            red: Double((hex >> 16) & 0xFF) / 255,
            green: Double((hex >> 8) & 0xFF) / 255,
            blue: Double(hex & 0xFF) / 255,
            opacity: 1
        )
    }
}

import SwiftUI

/// LifeOS four-colour identity: brand orange, growth purple, white and near-black.
extension Color {
    static let lifeOSBrand = Color(red: 0.86, green: 0.34, blue: 0.21)
    static let lifeOSSecondary = Color(red: 0.60, green: 0.25, blue: 0.96)
    static let lifeOSCanvas = Color(uiColor: .systemGroupedBackground)
    static let lifeOSCard = Color(uiColor: .secondarySystemGroupedBackground)
    static let lifeOSInk = Color.primary
    static let lifeOSMuted = Color.secondary
    static let lifeOSAccentSoft = Color.lifeOSSecondary.opacity(0.10)
}

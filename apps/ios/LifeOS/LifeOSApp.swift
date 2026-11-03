import SwiftUI
import SwiftData
import LumenUI

@main
struct LifeOSApp: App {
    var body: some Scene {
        WindowGroup {
            LifeOSThemedRoot()
        }
        .modelContainer(for: [Habit.self, HabitCompletion.self, LifeArea.self, LifeTask.self, LifeGoal.self])
    }
}

private struct LifeOSThemedRoot: View {
    @Environment(\.colorScheme) private var colorScheme

    private var tirniTheme: LumenTheme {
        let dark = colorScheme == .dark
        let base = dark ? LumenColors.dark : LumenColors.light
        let colors = base.overriding(
            canvas: Color(hex: dark ? 0x0F0B0D : 0xFFFBF7),
            surface: Color(hex: dark ? 0x1B1217 : 0xFFF3F5),
            surfaceMuted: Color(hex: dark ? 0x291820 : 0xFFF3F5),
            surfaceStrong: Color(hex: dark ? 0x3B202A : 0xFFE1E8),
            line: Color(hex: dark ? 0x5A3A45 : 0xF1DDE2),
            ink: Color(hex: dark ? 0xFFF5F7 : 0x111827),
            inkSoft: Color(hex: dark ? 0xE8D9DE : 0x374151),
            inkMuted: Color(hex: dark ? 0xC8B8BE : 0x6B7280),
            brand: Color(hex: dark ? 0xFF6B86 : 0xE5093B),
            brandSolid: Color(hex: dark ? 0xFF6B86 : 0x8B001C),
            brandSoft: Color(hex: dark ? 0x642B1C : 0xFFE1E8),
            onBrand: Color(hex: dark ? 0x32130B : 0xFFFFFF),
            accent: Color(hex: dark ? 0xC99BFF : 0x8B001C),
            success: Color(hex: dark ? 0x79D69D : 0x247A4B),
            warning: Color(hex: dark ? 0xF2C36D : 0x946000),
            danger: Color(hex: dark ? 0xFFB4AB : 0xB3261E),
            onDanger: Color(hex: dark ? 0x690005 : 0xFFFFFF)
        )
        return LumenTheme(
            colors: colors,
            scheme: dark ? .dark : .light,
            appearance: LumenAppearance(material: .solid)
        )
    }

    var body: some View {
        RootView()
            .lumenTheme(tirniTheme, enforceColorScheme: false)
    }
}

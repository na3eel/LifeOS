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

    private var strataTheme: LumenTheme {
        let dark = colorScheme == .dark
        let base = dark ? LumenColors.dark : LumenColors.light
        let colors = base.overriding(
            canvas: Color(hex: dark ? 0x17131A : 0xFFFBF7),
            surface: Color(hex: dark ? 0x211C24 : 0xFFFFFF),
            surfaceMuted: Color(hex: dark ? 0x342B37 : 0xF3E8E1),
            surfaceStrong: Color(hex: dark ? 0x463A49 : 0xE9DDD6),
            line: Color(hex: dark ? 0x655767 : 0xD6C5BC),
            ink: Color(hex: dark ? 0xF8F0F5 : 0x1F1724),
            inkSoft: Color(hex: dark ? 0xE0D5E3 : 0x443843),
            inkMuted: Color(hex: dark ? 0xB9AABF : 0x6F626B),
            brand: Color(hex: dark ? 0xFF9A7E : 0xDB5635),
            brandSolid: Color(hex: dark ? 0xFF9A7E : 0xB83F25),
            brandSoft: Color(hex: dark ? 0x642B1C : 0xFCE4DA),
            onBrand: Color(hex: dark ? 0x32130B : 0xFFFFFF),
            accent: Color(hex: dark ? 0xC99BFF : 0x9828F6),
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
            .lumenTheme(strataTheme, enforceColorScheme: false)
    }
}

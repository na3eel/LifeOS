import SwiftUI

private enum LifeOSTab: Hashable {
    case today, areas, calendar, insights, coach
}

struct RootView: View {
    @State private var selectedTab: LifeOSTab = .today

    var body: some View {
        TabView(selection: $selectedTab) {
            TodayView()
                .tabItem { Label("Today", systemImage: "checkmark.circle") }
                .tag(LifeOSTab.today)
            PlaceholderView(title: "Life areas", symbol: "square.grid.2x2")
                .tabItem { Label("Areas", systemImage: "square.grid.2x2") }
                .tag(LifeOSTab.areas)
            PlaceholderView(title: "Calendar", symbol: "calendar")
                .tabItem { Label("Calendar", systemImage: "calendar") }
                .tag(LifeOSTab.calendar)
            PlaceholderView(title: "Insights", symbol: "chart.bar.xaxis")
                .tabItem { Label("Insights", systemImage: "chart.bar.xaxis") }
                .tag(LifeOSTab.insights)
            PlaceholderView(title: "AI Coach", symbol: "sparkles")
                .tabItem { Label("Coach", systemImage: "sparkles") }
                .tag(LifeOSTab.coach)
        }
        .background(Color.lifeOSCanvas)
        .modifier(GlassTabBarIfAvailable())
    }
}

private struct GlassTabBarIfAvailable: ViewModifier {
    @ViewBuilder
    func body(content: Content) -> some View {
        content
    }
}

private struct PlaceholderView: View {
    let title: String
    let symbol: String

    var body: some View {
        NavigationStack {
            ContentUnavailableView(title, systemImage: symbol, description: Text("This native experience is being built next."))
                .navigationTitle(title)
        }
    }
}

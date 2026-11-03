import SwiftUI
import SwiftData

private enum AppTab: Hashable {
    case today, areas, plan, insights, coach
}

struct RootView: View {
    @Environment(\.modelContext) private var context
    @Query(sort: \Habit.createdAt) private var habits: [Habit]
    @Query(sort: \LifeArea.name) private var areas: [LifeArea]
    @Query private var tasks: [LifeTask]
    @Query private var goals: [LifeGoal]
    @Query private var completions: [HabitCompletion]
    @State private var tab: AppTab = .today
    @State private var showingAdd = false
    @State private var addKind = "Habit"

    private var done: Int {
        let formatter = DateFormatter()
        formatter.calendar = Calendar.current
        formatter.timeZone = .current
        formatter.dateFormat = "yyyy-MM-dd"
        let key = formatter.string(from: Date.now)
        return Set(completions.filter { $0.localDate == key }.map(\.habitID)).count
    }

    var body: some View {
        TabView(selection: $tab) {
            today.tabItem { Label("Today", systemImage: "checkmark.circle") }.tag(AppTab.today)
            areasScreen.tabItem { Label("Areas", systemImage: "square.grid.2x2") }.tag(AppTab.areas)
            planScreen.tabItem { Label("Plan", systemImage: "calendar") }.tag(AppTab.plan)
            insightsScreen.tabItem { Label("Insights", systemImage: "chart.bar.xaxis") }.tag(AppTab.insights)
            coachScreen.tabItem { Label("Coach", systemImage: "bubble.left.and.bubble.right") }.tag(AppTab.coach)
        }
        .tint(Color.lifeOSBrand)
        .toolbarBackground(Color.lifeOSCanvas, for: .tabBar)
        .toolbarBackground(.visible, for: .tabBar)
        .background(Color.lifeOSCanvas.ignoresSafeArea())
        .sheet(isPresented: $showingAdd) {
            AddSheet(kind: addKind) { name, note, area, dueDate in
                switch addKind {
                case "Habit": context.insert(Habit(title: name, detail: note, area: area))
                case "Task": context.insert(LifeTask(title: name, dueDate: dueDate))
                case "Goal": context.insert(LifeGoal(title: name))
                case "Area": context.insert(LifeArea(name: name, detail: note))
                default: break
                }
            }
        }
        .task {
            if areas.isEmpty {
                ["Body", "Mind", "Soul", "Social", "Career"].forEach { context.insert(LifeArea(name: $0)) }
            }
        }
    }

    private var today: some View { TodayView() }

    private var areasScreen: some View {
        NavigationStack {
            List {
                Section("Life areas") {
                    ForEach(areas) { area in
                        VStack(alignment: .leading, spacing: 4) {
                            Text(area.name).font(.headline).foregroundStyle(Color.lifeOSInk)
                            Text(area.detail.isEmpty ? "Build a rhythm that supports this area." : area.detail)
                                .font(.caption).foregroundStyle(Color.lifeOSMuted)
                        }
                        .padding(.vertical, 4)
                    }
                }
                Section("Goals") {
                    if goals.isEmpty {
                        ContentUnavailableView("No goals yet", systemImage: "flag", description: Text("Turn an intention into a milestone."))
                    }
                    ForEach(goals) { goal in
                        VStack(alignment: .leading, spacing: 8) {
                            Text(goal.title).foregroundStyle(Color.lifeOSInk)
                            Slider(value: Bindable(goal).progress, in: 0...100)
                                .tint(Color.lifeOSBrand)
                            Text("\(Int(goal.progress))% complete")
                                .font(.caption).foregroundStyle(Color.lifeOSMuted)
                        }
                        .padding(.vertical, 4)
                    }
                    Button("Create goal") { addKind = "Goal"; showingAdd = true }
                        .tint(Color.lifeOSBrand)
                }
            }
            .modifier(StrataListSurface())
            .navigationTitle("Life areas")
            .toolbarBackground(Color.lifeOSCanvas, for: .navigationBar)
            .toolbarBackground(.visible, for: .navigationBar)
            .toolbar {
                ToolbarItem(placement: .topBarTrailing) {
                    Button { addKind = "Area"; showingAdd = true } label: { Image(systemName: "plus") }
                        .accessibilityLabel("Add life area")
                }
            }
        }
    }

    private var planScreen: some View {
        NavigationStack {
            List {
                if tasks.isEmpty {
                    ContentUnavailableView("Your plan is clear", systemImage: "calendar", description: Text("Add a task or commitment to get started."))
                }
                ForEach(tasks.sorted { lhs, rhs in
                    switch (lhs.dueDate, rhs.dueDate) {
                    case let (left?, right?): return left < right
                    case (_?, nil): return true
                    case (nil, _?): return false
                    case (nil, nil): return lhs.title.localizedCaseInsensitiveCompare(rhs.title) == .orderedAscending
                    }
                }) { task in
                    VStack(alignment: .leading, spacing: 4) {
                        Toggle(task.title, isOn: Bindable(task).isComplete)
                            .tint(Color.lifeOSBrand)
                        if let due = task.dueDate {
                            Text("Due " + due.formatted(date: .abbreviated, time: .omitted))
                                .font(.caption).foregroundStyle(Color.lifeOSMuted)
                        }
                    }
                    .padding(.vertical, 3)
                }
                .onDelete { offsets in offsets.map { tasks[$0] }.forEach(context.delete) }
            }
            .modifier(StrataListSurface())
            .navigationTitle("Your plan")
            .toolbarBackground(Color.lifeOSCanvas, for: .navigationBar)
            .toolbarBackground(.visible, for: .navigationBar)
            .toolbar {
                ToolbarItem(placement: .topBarTrailing) {
                    Button { addKind = "Task"; showingAdd = true } label: { Image(systemName: "plus") }
                        .accessibilityLabel("Add task")
                }
            }
        }
    }

    private var insightsScreen: some View {
        NavigationStack {
            List {
                Section {
                    Text("A simple view of the consistency you’re building.")
                        .foregroundStyle(Color.lifeOSMuted)
                }
                Section("Your activity") {
                    metric("Habits created", habits.count)
                    metric("Completed habits", done)
                    metric("Open tasks", tasks.filter { !$0.isComplete }.count)
                    metric("Goals in progress", goals.filter { $0.progress < 100 }.count)
                }
            }
            .modifier(StrataListSurface())
            .navigationTitle("Insights")
            .toolbarBackground(Color.lifeOSCanvas, for: .navigationBar)
            .toolbarBackground(.visible, for: .navigationBar)
        }
    }

    private func metric(_ title: String, _ value: Int) -> some View {
        HStack {
            Text(title).foregroundStyle(Color.lifeOSInk)
            Spacer()
            Text(value.description)
                .font(.title3.weight(.semibold)).monospacedDigit()
                .foregroundStyle(Color.lifeOSInk)
        }
    }

    private var coachScreen: some View {
        NavigationStack {
            List {
                Section {
                    Text("A small next step")
                        .font(.title2.weight(.semibold)).foregroundStyle(Color.lifeOSInk)
                    Text(habits.isEmpty
                         ? "Start with one habit that makes tomorrow easier."
                         : done == habits.count
                            ? "You’ve completed today’s habits. Take a moment to recognise that progress."
                            : "Choose the easiest remaining habit and give it five focused minutes.")
                        .foregroundStyle(Color.lifeOSInk)
                    Text("On-device guidance for now. AI coaching will connect when the service is ready.")
                        .font(.caption).foregroundStyle(Color.lifeOSMuted)
                }
                Section("Life areas") {
                    ForEach(["Body", "Mind", "Soul", "Social", "Career"], id: \.self) { Text($0).foregroundStyle(Color.lifeOSInk) }
                }
            }
            .modifier(StrataListSurface())
            .navigationTitle("Your coach")
            .toolbarBackground(Color.lifeOSCanvas, for: .navigationBar)
            .toolbarBackground(.visible, for: .navigationBar)
        }
    }
}

private struct StrataListSurface: ViewModifier {
    func body(content: Content) -> some View {
        content
            .listStyle(.insetGrouped)
            .scrollContentBackground(.hidden)
            .background(Color.lifeOSCanvas)
            .tint(Color.lifeOSBrand)
            .foregroundStyle(Color.lifeOSInk)
            .listRowBackground(Color.lifeOSCard)
            .listRowSeparatorTint(Color.lifeOSBorder)
    }
}

private struct AddSheet: View {
    @Environment(\.dismiss) private var dismiss
    let kind: String
    let save: (String, String, String, Date?) -> Void
    @State private var hasDueDate = false
    @State private var dueDate = Date()
    @State private var name = ""
    @State private var note = ""
    @State private var area = "Mind"

    var body: some View {
        NavigationStack {
            Form {
                Section(kind) {
                    TextField("Name", text: $name)
                    if kind == "Habit" {
                        Picker("Start from a preset", selection: $name) {
                            Text("Choose a preset").tag("")
                            ForEach(["Wake up at 05:00", "Gym / Workout", "Avoid pornography", "Reading / Learning", "Budget tracking", "Project work", "No alcohol", "Social media detox", "Goal journaling", "Cold shower"], id: \.self) {
                                Text($0).tag($0)
                            }
                        }
                    }
                    if kind == "Habit" || kind == "Area" { TextField("Optional note", text: $note) }
                    if kind == "Habit" {
                        Picker("Life area", selection: $area) {
                            ForEach(["Body", "Mind", "Soul", "Social", "Career"], id: \.self) { Text($0) }
                        }
                    }
                    if kind == "Task" {
                        Toggle("Set due date", isOn: $hasDueDate)
                        if hasDueDate { DatePicker("Due", selection: $dueDate, displayedComponents: .date) }
                    }
                }
            }
            .listStyle(.insetGrouped)
            .scrollContentBackground(.hidden)
            .background(Color.lifeOSCanvas)
            .tint(Color.lifeOSBrand)
            .listRowBackground(Color.lifeOSCard)
            .navigationTitle("New " + kind.lowercased())
            .toolbarBackground(Color.lifeOSCanvas, for: .navigationBar)
            .toolbarBackground(.visible, for: .navigationBar)
            .toolbar {
                ToolbarItem(placement: .cancellationAction) {
                    Button("Cancel") { dismiss() }.tint(Color.lifeOSInk)
                }
                ToolbarItem(placement: .confirmationAction) {
                    Button("Save") {
                        let value = name.trimmingCharacters(in: .whitespacesAndNewlines)
                        guard !value.isEmpty else { return }
                        save(value, note, area, kind == "Task" && hasDueDate ? dueDate : nil)
                        dismiss()
                    }
                    .disabled(name.trimmingCharacters(in: .whitespacesAndNewlines).isEmpty)
                    .tint(Color.lifeOSBrand)
                }
            }
        }
    }
}

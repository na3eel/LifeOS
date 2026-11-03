import SwiftUI
import LumenUI
import SwiftData

struct TodayView: View {
    @Environment(\.modelContext) private var modelContext
    @Query(sort: \Habit.createdAt, order: .forward) private var habits: [Habit]
    @Query private var completions: [HabitCompletion]
    @State private var isShowingAddHabit = false
    @State private var editingHabit: Habit?

    private var todayHabits: [Habit] {
        let calendarWeekday = Calendar.current.component(.weekday, from: .now)
        let isoWeekday = ((calendarWeekday + 5) % 7) + 1
        return habits.filter { $0.scheduledWeekdays.split(separator: ",").compactMap { Int($0) }.contains(isoWeekday) }
    }
    private var todayKey: String {
        let formatter = DateFormatter()
        formatter.calendar = Calendar.current
        formatter.timeZone = .current
        formatter.dateFormat = "yyyy-MM-dd"
        return formatter.string(from: .now)
    }
    private var completedHabitIDs: Set<UUID> {
        Set(completions.filter { $0.localDate == todayKey }.map(\.habitID))
    }
    private var completedCount: Int { todayHabits.filter { completedHabitIDs.contains($0.id) }.count }
    private var progress: Double {
        todayHabits.isEmpty ? 0 : Double(completedCount) / Double(todayHabits.count)
    }

    var body: some View {
        NavigationStack {
            ScrollView {
                VStack(alignment: .leading, spacing: 22) {
                    VStack(alignment: .leading, spacing: 8) {
                        Text(Date.now.formatted(.dateTime.weekday(.wide).month(.wide).day()).uppercased())
                            .font(.caption.weight(.semibold))
                            .tracking(1.1)
                            .foregroundStyle(.secondary)
                        Text("Make today count.")
                            .font(.largeTitle.weight(.semibold))
                            .fixedSize(horizontal: false, vertical: true)
                    }

                    rhythmCard

                    HStack {
                        Text("Your rhythm").font(.title2.weight(.semibold))
                        Spacer()
                        Text("\(completedCount) of \(todayHabits.count)")
                            .font(.subheadline.weight(.medium))
                            .foregroundStyle(.secondary)
                    }

                    if todayHabits.isEmpty {
                        ContentUnavailableView {
                            Label("Start with one small habit", systemImage: "checkmark.circle")
                        } description: {
                            Text("Choose something meaningful and make it part of your day.")
                        } actions: {
                            Button("Create your first habit") { isShowingAddHabit = true }
                                .buttonStyle(.borderedProminent)
                                .tint(Color.lifeOSBrand)
                        }
                        .frame(minHeight: 230)
                    } else {
                        VStack(spacing: 0) {
                            ForEach(todayHabits) { habit in
                                HabitRow(habit: habit, isComplete: completedHabitIDs.contains(habit.id), onEdit: { editingHabit = habit }) { isComplete in
                                    let existing = completions.first { $0.habitID == habit.id && $0.localDate == todayKey }
                                    if isComplete {
                                        if existing == nil { modelContext.insert(HabitCompletion(habitID: habit.id, localDate: todayKey)) }
                                    } else if let existing {
                                        modelContext.delete(existing)
                                    }
                                }
                                if habit.id != todayHabits.last?.id {
                                    Divider().padding(.leading, 18)
                                }
                            }
                        }
                        .background(Color.lifeOSCard, in: RoundedRectangle(cornerRadius: 22, style: .continuous))
                        .overlay(RoundedRectangle(cornerRadius: 22, style: .continuous).stroke(Color.lifeOSBorder.opacity(0.65), lineWidth: 1))
                    }

                    Text("A little progress, repeated.")
                        .font(.subheadline)
                        .foregroundStyle(.secondary)
                        .padding(.bottom, 24)
                }
                .padding(.horizontal, 20)
                .padding(.top, 18)
                .frame(maxWidth: 680)
                .frame(maxWidth: .infinity)
            }
            .background(Color.lifeOSCanvas)
            .navigationBarTitleDisplayMode(.inline)
            .toolbar {
                ToolbarItem(placement: .principal) {
                    HStack(spacing: 8) {
                        LifeOSBrandMark()
                            .frame(width: 30, height: 30)
                        Text("LifeOS")
                            .font(.system(.headline, design: .rounded).weight(.black))
                            .tracking(-0.7)
                            .foregroundStyle(Color.lifeOSInk)
                    }
                    .accessibilityElement(children: .ignore)
                    .accessibilityLabel("LifeOS")
                }
                ToolbarItem(placement: .topBarTrailing) {
                    Button { isShowingAddHabit = true } label: {
                        Image(systemName: "plus").font(.body.weight(.semibold))
                            .frame(minWidth: 44, minHeight: 44)
                    }
                    .accessibilityLabel("Add a habit")
                    .tint(Color.lifeOSBrand).background(Color.lifeOSAccentSoft, in: Circle())
                }
            }
            .sheet(isPresented: $isShowingAddHabit) {
                AddHabitView()
            }
            .sheet(item: $editingHabit) { habit in
                AddHabitView(habit: habit)
            }
        }
    }

    private var rhythmCard: some View {
        VStack(alignment: .leading, spacing: 12) {
            Text("DAILY RHYTHM")
                .font(.caption.weight(.semibold))
                .tracking(1)
                .foregroundStyle(.secondary)
            HStack(alignment: .firstTextBaseline, spacing: 6) {
                Text("\(completedCount)")
                    .font(.system(size: 36, weight: .semibold, design: .rounded))
                Text("/ \(habits.count)")
                    .font(.title3)
                    .foregroundStyle(.secondary)
            }
            ProgressView(value: progress).tint(Color.lifeOSBrand)
                .accessibilityLabel("Daily completion")
                .accessibilityValue("\(Int(progress * 100)) percent")
        }
        .padding(20)
        .frame(maxWidth: .infinity, alignment: .leading)
        .background(Color.lifeOSAccentSoft, in: RoundedRectangle(cornerRadius: 24, style: .continuous))
    }
}

private struct HabitRow: View {
    let habit: Habit
    let isComplete: Bool
    let onEdit: () -> Void
    let onToggle: (Bool) -> Void

    var body: some View {
        HStack(spacing: 14) {
            VStack(alignment: .leading, spacing: 5) {
                Text(habit.title)
                    .font(.body.weight(.medium))
                    .foregroundStyle(isComplete ? .secondary : .primary)
                    .strikethrough(isComplete)
                    .fixedSize(horizontal: false, vertical: true)
                if !habit.detail.isEmpty {
                    Text(habit.detail).font(.subheadline).foregroundStyle(.secondary)
                        .fixedSize(horizontal: false, vertical: true)
                }
                Text(habit.targetUnit.isEmpty ? habit.area : "\(habit.targetValue.formatted()) \(habit.targetUnit) · \(habit.area)")
                    .font(.caption).foregroundStyle(.secondary)
            }
            Spacer(minLength: 8)
            Button(action: onEdit) {
                Image(systemName: "pencil")
                    .frame(width: 44, height: 44)
            }
            .buttonStyle(.plain)
            .accessibilityLabel("Edit \(habit.title)")
            Toggle("Mark \(habit.title) complete", isOn: Binding(get: { isComplete }, set: onToggle))
                .labelsHidden()
                .tint(Color.lifeOSBrand)
                .accessibilityLabel("Mark \(habit.title) complete")
        }
        .padding(.horizontal, 16)
        .padding(.vertical, 14)
        .contentShape(Rectangle())
    }
}
private struct AddHabitView: View {
    @Environment(\.modelContext) private var modelContext
    @Environment(\.dismiss) private var dismiss
    @State private var title = ""
    @State private var detail = ""
    @State private var area = "Mind"
    @State private var targetValue = "1"
    @State private var targetUnit = ""
    @State private var weekdays: Set<Int> = Set(1...7)
    @State private var search = ""
    @State private var showingPresets = true
    private let habit: Habit?
    @FocusState private var titleFocused: Bool

    private let areas = ["Body", "Mind", "Soul", "Social", "Career"]
    private let presets: [(String, String, String, String)] = [
        ("Wake up at 05:00", "Mind", "Build a consistent morning routine", "Routine"),
        ("Gym / Workout", "Body", "Move your body and build strength", "Health"),
        ("Reading / Learning", "Mind", "Spend time learning something new", "Growth"),
        ("Budget tracking", "Career", "Review spending and stay intentional", "Life"),
        ("Project work", "Career", "Make progress on an important project", "Work"),
        ("No alcohol", "Body", "A private daily commitment", "Health"),
        ("Social media detox", "Mind", "Be intentional with screen time", "Focus"),
        ("Goal journaling", "Soul", "Reflect and write down your goals", "Growth"),
        ("Cold shower", "Body", "A small daily challenge", "Health"),
        ("Avoid pornography", "Mind", "A private personal commitment", "Focus")
    ]

    init(habit: Habit? = nil) {
        self.habit = habit
        _title = State(initialValue: habit?.title ?? "")
        _detail = State(initialValue: habit?.detail ?? "")
        _area = State(initialValue: habit?.area ?? "Mind")
        _targetValue = State(initialValue: habit.map { String($0.targetValue) } ?? "1")
        _targetUnit = State(initialValue: habit?.targetUnit ?? "")
        _weekdays = State(initialValue: Set(habit?.scheduledWeekdays.split(separator: ",").compactMap { Int($0) } ?? Array(1...7)))
        _showingPresets = State(initialValue: habit == nil)
    }

    private var canSave: Bool { !title.trimmingCharacters(in: .whitespacesAndNewlines).isEmpty }
    private var filteredPresets: [(String, String, String, String)] {
        guard !search.isEmpty else { return presets }
        return presets.filter { $0.0.localizedCaseInsensitiveContains(search) || $0.3.localizedCaseInsensitiveContains(search) }
    }

    var body: some View {
        NavigationStack {
            ScrollView {
                VStack(alignment: .leading, spacing: 22) {
                    VStack(alignment: .leading, spacing: 7) {
                        Text(habit == nil ? "BUILD A HABIT" : "REFINE YOUR HABIT")
                            .font(.caption.weight(.bold)).tracking(1.2)
                            .foregroundStyle(Color.lifeOSBrand)
                        Text(habit == nil ? "Small steps. Better days." : "Make it yours.")
                            .font(.system(.largeTitle, design: .rounded).weight(.semibold))
                            .foregroundStyle(Color.lifeOSInk)
                        Text("Choose a starting point, then shape it around your life.")
                            .font(.subheadline).foregroundStyle(.secondary)
                    }

                    if habit == nil {
                        VStack(alignment: .leading, spacing: 12) {
                            HStack {
                                sectionHeading("START WITH A TEMPLATE")
                                Spacer()
                                Button(showingPresets ? "Hide" : "Browse") { showingPresets.toggle() }
                                    .font(.subheadline.weight(.semibold))
                                    .tint(Color.lifeOSBrand)
                            }
                            if showingPresets {
                                HStack(spacing: 10) {
                                    Image(systemName: "magnifyingglass").foregroundStyle(.secondary)
                                    TextField("Search ideas", text: $search)
                                        .textInputAutocapitalization(.never)
                                }
                                .padding(13)
                                .background(Color.lifeOSCard, in: RoundedRectangle(cornerRadius: 14))
                                LazyVStack(spacing: 9) {
                                    ForEach(filteredPresets, id: \.0) { preset in
                                        LumenCard(
                                            variant: title == preset.0 ? .accent : .muted,
                                            padding: .md,
                                            radius: .lg,
                                            action: {
                                                title = preset.0
                                                area = preset.1
                                                detail = preset.2
                                                showingPresets = false
                                            }
                                        ) {
                                            HStack(spacing: 12) {
                                                VStack(alignment: .leading, spacing: 4) {
                                                    Text(preset.0).font(.subheadline.weight(.semibold)).foregroundStyle(Color.lifeOSInk)
                                                    Text(preset.2).font(.caption).foregroundStyle(.secondary).multilineTextAlignment(.leading)
                                                }
                                                Spacer(minLength: 4)
                                                Text(preset.1.uppercased()).font(.system(size: 9, weight: .bold)).tracking(0.5)
                                                    .foregroundStyle(Color.lifeOSBrand)
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }

                    VStack(alignment: .leading, spacing: 12) {
                        sectionHeading("YOUR HABIT")
                        VStack(spacing: 0) {
                            TextField("e.g. Walk for 20 minutes", text: $title)
                                .focused($titleFocused)
                                .textInputAutocapitalization(.sentences)
                                .submitLabel(.next)
                                .padding(15)
                            Divider().padding(.leading, 15)
                            TextField("A note (optional)", text: $detail, axis: .vertical)
                                .lineLimit(2...4)
                                .padding(15)
                        }
                        .background(Color.lifeOSCard, in: RoundedRectangle(cornerRadius: 16))
                        .overlay(RoundedRectangle(cornerRadius: 16).stroke(Color.lifeOSInk.opacity(0.07), lineWidth: 1))
                    }

                    VStack(alignment: .leading, spacing: 12) {
                        sectionHeading("LIFE AREA")
                        Text("Where does this habit support you?")
                            .font(.caption).foregroundStyle(.secondary)
                        LazyVGrid(columns: [GridItem(.flexible()), GridItem(.flexible())], spacing: 9) {
                            ForEach(areas, id: \.self) { choice in
                                Button { area = choice } label: {
                                    HStack {
                                        Text(choice).font(.subheadline.weight(.medium))
                                        Spacer()
                                        if area == choice { Image(systemName: "checkmark").font(.caption.weight(.bold)) }
                                    }
                                    .foregroundStyle(area == choice ? Color.lifeOSBrand : Color.lifeOSInk)
                                    .padding(.horizontal, 14).frame(height: 46)
                                    .background(area == choice ? Color.lifeOSAccentSoft : Color.lifeOSCard, in: RoundedRectangle(cornerRadius: 13))
                                    .overlay(RoundedRectangle(cornerRadius: 13).stroke(area == choice ? Color.lifeOSBrand.opacity(0.55) : Color.lifeOSInk.opacity(0.07), lineWidth: 1))
                                }
                                .buttonStyle(.plain)
                            }
                        }
                    }

                    VStack(alignment: .leading, spacing: 12) {
                        sectionHeading("MEASURABLE TARGET")
                        Text("Use 1 with no unit for a simple yes/no habit.")
                            .font(.caption).foregroundStyle(.secondary)
                        HStack(spacing: 10) {
                            TextField("Target", text: $targetValue)
                                .keyboardType(.decimalPad)
                                .padding(13)
                                .background(Color.lifeOSCard, in: RoundedRectangle(cornerRadius: 13))
                            TextField("Unit (optional)", text: $targetUnit)
                                .padding(13)
                                .background(Color.lifeOSCard, in: RoundedRectangle(cornerRadius: 13))
                        }
                        if let value = Double(targetValue), value <= 0 {
                            Text("Target must be greater than zero.").font(.caption).foregroundStyle(.red)
                        }
                    }

                    VStack(alignment: .leading, spacing: 12) {
                        sectionHeading("REPEAT ON")
                        Text("Choose the days this habit is due.")
                            .font(.caption).foregroundStyle(.secondary)
                        HStack(spacing: 6) {
                            ForEach(1...7, id: \.self) { day in
                                let labels = ["M", "T", "W", "T", "F", "S", "S"]
                                Button {
                                    if weekdays.contains(day) { weekdays.remove(day) }
                                    else { weekdays.insert(day) }
                                } label: {
                                    Text(labels[day - 1])
                                        .font(.subheadline.weight(.semibold))
                                        .frame(maxWidth: .infinity, minHeight: 42)
                                        .background(weekdays.contains(day) ? Color.lifeOSAccentSoft : Color.lifeOSCard, in: RoundedRectangle(cornerRadius: 12))
                                        .overlay(RoundedRectangle(cornerRadius: 12).stroke(weekdays.contains(day) ? Color.lifeOSBrand : Color.lifeOSBorder, lineWidth: 1))
                                        .foregroundStyle(weekdays.contains(day) ? Color.lifeOSBrand : Color.lifeOSInk)
                                }
                                .buttonStyle(.plain)
                                .accessibilityLabel(["Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"][day - 1])
                                .accessibilityAddTraits(weekdays.contains(day) ? .isSelected : [])
                            }
                        }
                        if weekdays.isEmpty {
                            Text("Choose at least one day.").font(.caption).foregroundStyle(.red)
                        }
                    }

                    Text("You can adjust your habit any time.")
                        .font(.caption).foregroundStyle(.secondary)
                    LumenButton(intent: .primary, size: .lg, disabled: !canSave || weekdays.isEmpty || (Double(targetValue) ?? 0) <= 0, action: {
                        let cleanTitle = title.trimmingCharacters(in: .whitespacesAndNewlines)
                        let cleanDetail = detail.trimmingCharacters(in: .whitespacesAndNewlines)
                        guard !cleanTitle.isEmpty else { return }
                        if let habit {
                            habit.title = cleanTitle
                            habit.detail = cleanDetail
                            habit.area = area
                            habit.targetValue = Double(targetValue) ?? 1
                            habit.targetUnit = targetUnit.trimmingCharacters(in: .whitespacesAndNewlines)
                            habit.scheduledWeekdays = weekdays.sorted().map(String.init).joined(separator: ",")
                        } else {
                            modelContext.insert(Habit(title: cleanTitle, detail: cleanDetail, area: area, targetValue: Double(targetValue) ?? 1, targetUnit: targetUnit.trimmingCharacters(in: .whitespacesAndNewlines), scheduledWeekdays: weekdays.sorted().map(String.init).joined(separator: ",")))
                        }
                        dismiss()
                    }) {
                        Text(habit == nil ? "Create habit" : "Save changes")
                            .frame(maxWidth: .infinity)
                    }
                    .frame(maxWidth: .infinity)
                    .padding(.top, 2)
                }
                .padding(20)
                .frame(maxWidth: 620)
                .frame(maxWidth: .infinity)
            }
            .background(Color.lifeOSCanvas)
            .scrollDismissesKeyboard(.interactively)
            .navigationTitle(habit == nil ? "New habit" : "Edit habit")
            .navigationBarTitleDisplayMode(.inline)
            .toolbar {
                ToolbarItem(placement: .cancellationAction) {
                    Button("Cancel") { dismiss() }.tint(Color.lifeOSInk)
                }
            }
            .onAppear { if habit == nil { titleFocused = true } }
        }
        .presentationDetents([.large])
        .presentationDragIndicator(.visible)
    }

    private func sectionHeading(_ value: String) -> some View {
        Text(value).font(.caption.weight(.bold)).tracking(1)
            .foregroundStyle(Color.lifeOSInk.opacity(0.72))
    }
}

private struct GlassControlIfAvailable: ViewModifier {
    @ViewBuilder
    func body(content: Content) -> some View {
        if #available(iOS 26.0, *) {
            content.glassEffect(.regular.interactive())
        } else {
            content.background(.regularMaterial, in: Circle())
        }
    }
}


private struct LifeOSBrandMark: View {
    private let orange = Color(hex: 0xDB5635)
    private let ink = Color(hex: 0x1F1724)

    var body: some View {
        GeometryReader { proxy in
            let side = min(proxy.size.width, proxy.size.height)
            ZStack {
                RoundedRectangle(cornerRadius: side * 0.22, style: .continuous)
                    .fill(orange)
                Circle()
                    .fill(ink)
                    .frame(width: side * 0.21, height: side * 0.21)
                    .position(x: side * 0.68, y: side * 0.22)
                Path { path in
                    path.move(to: CGPoint(x: side * 0.29, y: side * 0.34))
                    path.addQuadCurve(to: CGPoint(x: side * 0.39, y: side * 0.43), control: CGPoint(x: side * 0.24, y: side * 0.30))
                    path.addLine(to: CGPoint(x: side * 0.52, y: side * 0.55))
                    path.addLine(to: CGPoint(x: side * 0.24, y: side * 0.70))
                    path.addCurve(to: CGPoint(x: side * 0.12, y: side * 0.88), control1: CGPoint(x: side * 0.04, y: side * 0.78), control2: CGPoint(x: side * 0.08, y: side * 0.94))
                    path.addCurve(to: CGPoint(x: side * 0.34, y: side * 0.86), control1: CGPoint(x: side * 0.19, y: side * 0.95), control2: CGPoint(x: side * 0.28, y: side * 0.93))
                    path.addLine(to: CGPoint(x: side * 0.67, y: side * 0.68))
                    path.addLine(to: CGPoint(x: side * 0.77, y: side * 0.79))
                    path.addCurve(to: CGPoint(x: side * 0.91, y: side * 0.77), control1: CGPoint(x: side * 0.84, y: side * 0.88), control2: CGPoint(x: side * 0.93, y: side * 0.86))
                    path.addCurve(to: CGPoint(x: side * 0.86, y: side * 0.64), control1: CGPoint(x: side * 0.90, y: side * 0.71), control2: CGPoint(x: side * 0.89, y: side * 0.67))
                    path.addLine(to: CGPoint(x: side * 0.72, y: side * 0.53))
                    path.addLine(to: CGPoint(x: side * 0.82, y: side * 0.42))
                    path.addLine(to: CGPoint(x: side * 0.29, y: side * 0.34))
                    path.closeSubpath()
                }
                .fill(ink)
            }
        }
        .accessibilityHidden(true)
    }
}

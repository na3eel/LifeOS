import SwiftUI
import SwiftData

struct TodayView: View {
    @Environment(\.modelContext) private var modelContext
    @Query(sort: \Habit.createdAt, order: .forward) private var habits: [Habit]
    @State private var isShowingAddHabit = false
    @State private var editingHabit: Habit?

    private var completedCount: Int { habits.filter(\.isComplete).count }
    private var progress: Double {
        habits.isEmpty ? 0 : Double(completedCount) / Double(habits.count)
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
                        Text("\(completedCount) of \(habits.count)")
                            .font(.subheadline.weight(.medium))
                            .foregroundStyle(.secondary)
                    }

                    if habits.isEmpty {
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
                            ForEach(habits) { habit in
                                HabitRow(habit: habit) { editingHabit = habit }
                                if habit.id != habits.last?.id {
                                    Divider().padding(.leading, 18)
                                }
                            }
                        }
                        .background(Color.lifeOSCard, in: RoundedRectangle(cornerRadius: 22, style: .continuous))
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
                    .modifier(GlassControlIfAvailable())
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
    @Bindable var habit: Habit
    let onEdit: () -> Void

    var body: some View {
        HStack(spacing: 14) {
            VStack(alignment: .leading, spacing: 5) {
                Text(habit.title)
                    .font(.body.weight(.medium))
                    .foregroundStyle(habit.isComplete ? .secondary : .primary)
                    .strikethrough(habit.isComplete)
                    .fixedSize(horizontal: false, vertical: true)
                if !habit.detail.isEmpty {
                    Text(habit.detail).font(.subheadline).foregroundStyle(.secondary)
                        .fixedSize(horizontal: false, vertical: true)
                }
            }
            Spacer(minLength: 8)
            Button(action: onEdit) {
                Image(systemName: "pencil")
                    .frame(width: 44, height: 44)
            }
            .buttonStyle(.plain)
            .accessibilityLabel("Edit \(habit.title)")
            Toggle("Mark \(habit.title) complete", isOn: $habit.isComplete)
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
    private let habit: Habit?
    @FocusState private var titleFocused: Bool

    init(habit: Habit? = nil) {
        self.habit = habit
        _title = State(initialValue: habit?.title ?? "")
        _detail = State(initialValue: habit?.detail ?? "")
    }

    private var canSave: Bool { !title.trimmingCharacters(in: .whitespacesAndNewlines).isEmpty }

    var body: some View {
        NavigationStack {
            Form {
                Section("Habit") {
                    TextField("e.g. Walk for 20 minutes", text: $title)
                        .focused($titleFocused)
                        .textInputAutocapitalization(.sentences)
                        .submitLabel(.next)
                    TextField("Optional note or life area", text: $detail, axis: .vertical)
                        .lineLimit(2...4)
                }
                Section {
                    Text("Start small. You can edit this habit later.")
                        .font(.footnote)
                        .foregroundStyle(.secondary)
                }
            }
            .scrollDismissesKeyboard(.interactively)
            .navigationTitle(habit == nil ? "New habit" : "Edit habit")
            .navigationBarTitleDisplayMode(.inline)
            .toolbar {
                ToolbarItem(placement: .cancellationAction) {
                    Button("Cancel") { dismiss() }
                }
                ToolbarItem(placement: .confirmationAction) {
                    Button("Save") {
                        let cleanTitle = title.trimmingCharacters(in: .whitespacesAndNewlines)
                        let cleanDetail = detail.trimmingCharacters(in: .whitespacesAndNewlines)
                        guard !cleanTitle.isEmpty else { return }
                        if let habit {
                            habit.title = cleanTitle
                            habit.detail = cleanDetail
                        } else {
                            modelContext.insert(Habit(title: cleanTitle, detail: cleanDetail))
                        }
                        dismiss()
                    }
                    .disabled(!canSave)
                }
            }
            .onAppear { if habit == nil { titleFocused = true } }
        }
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
    private let orange = Color(red: 0.953, green: 0.420, blue: 0.031)
    private let ink = Color(red: 0.196, green: 0.063, blue: 0.020)

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

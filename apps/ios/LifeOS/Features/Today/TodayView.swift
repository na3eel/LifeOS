import SwiftUI
import SwiftData

struct TodayView: View {
    @Environment(\.modelContext) private var modelContext
    @Query(sort: \Habit.createdAt, order: .forward) private var habits: [Habit]
    @State private var isShowingAddHabit = false

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
                                HabitRow(habit: habit)
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
    @Environment(\.modelContext) private var modelContext
    @Bindable var habit: Habit

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
    @FocusState private var titleFocused: Bool

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
            .navigationTitle("New habit")
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
                        modelContext.insert(Habit(title: cleanTitle, detail: cleanDetail))
                        dismiss()
                    }
                    .disabled(!canSave)
                }
            }
            .onAppear { titleFocused = true }
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

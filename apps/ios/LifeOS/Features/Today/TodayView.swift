import SwiftUI

struct TodayView: View {
    @State private var habits = [
        Habit(title: "Morning movement", detail: "20 minutes · Body"),
        Habit(title: "Read a few pages", detail: "10 minutes · Mind"),
        Habit(title: "Quiet reflection", detail: "5 minutes · Soul")
    ]

    private var completedCount: Int { habits.filter(\.isComplete).count }
    private var progress: Double {
        habits.isEmpty ? 0 : Double(completedCount) / Double(habits.count)
    }

    var body: some View {
        NavigationStack {
            ScrollView {
                VStack(alignment: .leading, spacing: 24) {
                    VStack(alignment: .leading, spacing: 8) {
                        Text(Date.now.formatted(.dateTime.weekday(.wide).month(.wide).day()).uppercased())
                            .font(.caption.weight(.semibold))
                            .tracking(1.1)
                            .foregroundStyle(.secondary)
                        Text("Make today count.")
                            .font(.largeTitle.weight(.semibold))
                    }

                    rhythmCard

                    HStack {
                        Text("Your rhythm").font(.title2.weight(.semibold))
                        Spacer()
                        Text("\(completedCount) of \(habits.count)")
                            .font(.subheadline.weight(.medium))
                            .foregroundStyle(.secondary)
                    }

                    VStack(spacing: 0) {
                        ForEach($habits) { $habit in
                            HabitRow(habit: $habit)
                            if habit.id != habits.last?.id {
                                Divider().padding(.leading, 18)
                            }
                        }
                    }
                    .background(Color.lifeOSCard, in: RoundedRectangle(cornerRadius: 24, style: .continuous))

                    Text("A little progress, repeated.")
                        .font(.subheadline)
                        .foregroundStyle(.secondary)
                        .padding(.bottom, 24)
                }
                .padding(.horizontal, 22)
                .padding(.top, 18)
            }
            .background(Color.lifeOSCanvas)
            .navigationBarTitleDisplayMode(.inline)
            .toolbar {
                ToolbarItem(placement: .topBarTrailing) {
                    Button {} label: {
                        Image(systemName: "plus")
                            .font(.body.weight(.semibold))
                    }
                    .accessibilityLabel("Add a habit")
                    .modifier(GlassControlIfAvailable())
                }
            }
        }
    }

    private var rhythmCard: some View {
        VStack(alignment: .leading, spacing: 14) {
            Text("DAILY RHYTHM")
                .font(.caption.weight(.semibold))
                .tracking(1)
                .foregroundStyle(.secondary)
            HStack(alignment: .firstTextBaseline, spacing: 6) {
                Text("\(completedCount)")
                    .font(.system(size: 38, weight: .semibold, design: .rounded))
                Text("/ \(habits.count)")
                    .font(.title3)
                    .foregroundStyle(.secondary)
            }
            ProgressView(value: progress)
                .tint(Color.lifeOSBrand)
        }
        .padding(22)
        .frame(maxWidth: .infinity, alignment: .leading)
        .background(Color.lifeOSAccentSoft, in: RoundedRectangle(cornerRadius: 26, style: .continuous))
    }
}

private struct HabitRow: View {
    @Binding var habit: Habit

    var body: some View {
        HStack(spacing: 14) {
            VStack(alignment: .leading, spacing: 5) {
                Text(habit.title)
                    .font(.body.weight(.medium))
                    .foregroundStyle(habit.isComplete ? .secondary : .primary)
                    .strikethrough(habit.isComplete)
                Text(habit.detail)
                    .font(.caption)
                    .foregroundStyle(.secondary)
            }
            Spacer()
            Toggle(habit.title, isOn: $habit.isComplete)
                .labelsHidden()
                .tint(Color.lifeOSBrand)
                .accessibilityLabel("Mark \(habit.title) complete")
        }
        .padding(.horizontal, 18)
        .padding(.vertical, 16)
        .contentShape(Rectangle())
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

import XCTest
import SwiftData
@testable import LifeOS

@MainActor
final class SwiftDataPersistenceTests: XCTestCase {
    private func makeContainer() throws -> ModelContainer {
        let configuration = ModelConfiguration(isStoredInMemoryOnly: true)
        return try ModelContainer(
            for: Habit.self, HabitCompletion.self, LifeArea.self, LifeTask.self, LifeGoal.self,
            configurations: configuration
        )
    }

    func testHabitCanBeInsertedUpdatedAndDeleted() throws {
        let container = try makeContainer()
        let context = container.mainContext
        let habit = Habit(title: "Read", area: "Mind")
        context.insert(habit)
        try context.save()

        var habits = try context.fetch(FetchDescriptor<Habit>())
        XCTAssertEqual(habits.count, 1)
        XCTAssertEqual(habits.first?.title, "Read")

        habits[0].isComplete = true
        try context.save()
        XCTAssertTrue(try context.fetch(FetchDescriptor<Habit>()).first?.isComplete ?? false)

        context.delete(habits[0])
        try context.save()
        XCTAssertTrue(try context.fetch(FetchDescriptor<Habit>()).isEmpty)
    }

    func testHabitTargetAndRecurringWeekdaysPersist() throws {
        let container = try makeContainer()
        let context = container.mainContext
        let habit = Habit(
            title: "Walk",
            targetValue: 30,
            targetUnit: "minutes",
            scheduledWeekdays: "1,3,5"
        )
        context.insert(habit)
        try context.save()

        let saved = try XCTUnwrap(context.fetch(FetchDescriptor<Habit>()).first)
        XCTAssertEqual(saved.targetValue, 30)
        XCTAssertEqual(saved.targetUnit, "minutes")
        XCTAssertEqual(saved.scheduledWeekdays, "1,3,5")
    }


    func testHabitCompletionIsUniquePerLocalDateAndCanBeUndone() throws {
        let container = try makeContainer()
        let context = container.mainContext
        let habit = Habit(title: "Read")
        context.insert(habit)
        let first = HabitCompletion(habitID: habit.id, localDate: "2026-11-03")
        context.insert(first)
        try context.save()

        let saved = try context.fetch(FetchDescriptor<HabitCompletion>())
        XCTAssertEqual(saved.count, 1)
        XCTAssertEqual(saved.first?.localDate, "2026-11-03")

        context.delete(saved[0])
        try context.save()
        XCTAssertTrue(try context.fetch(FetchDescriptor<HabitCompletion>()).isEmpty)
    }

    func testAreasTasksAndGoalsPersist() throws {
        let container = try makeContainer()
        let context = container.mainContext
        context.insert(LifeArea(name: "Body"))
        context.insert(LifeTask(title: "Plan the week"))
        context.insert(LifeGoal(title: "Run 5 km", progress: 25))
        try context.save()

        XCTAssertEqual(try context.fetch(FetchDescriptor<LifeArea>()).count, 1)
        XCTAssertEqual(try context.fetch(FetchDescriptor<LifeTask>()).first?.title, "Plan the week")
        XCTAssertEqual(try context.fetch(FetchDescriptor<LifeGoal>()).first?.progress, 25)
    }
}

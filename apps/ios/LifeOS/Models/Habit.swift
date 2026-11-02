import Foundation
import SwiftData

@Model
final class Habit: Identifiable {
    @Attribute(.unique) var id: UUID
    var title: String
    var detail: String
    var area: String
    var isComplete: Bool
    var createdAt: Date

    init(id: UUID = UUID(), title: String, detail: String = "", area: String = "Mind", isComplete: Bool = false, createdAt: Date = .now) {
        self.id = id
        self.title = title
        self.detail = detail
        self.area = area
        self.isComplete = isComplete
        self.createdAt = createdAt
    }
}

@Model
final class LifeArea: Identifiable {
    @Attribute(.unique) var id: UUID
    var name: String
    var detail: String

    init(id: UUID = UUID(), name: String, detail: String = "") {
        self.id = id
        self.name = name
        self.detail = detail
    }
}

@Model
final class LifeTask: Identifiable {
    @Attribute(.unique) var id: UUID
    var title: String
    var dueDate: Date?
    var isComplete: Bool

    init(id: UUID = UUID(), title: String, dueDate: Date? = nil, isComplete: Bool = false) {
        self.id = id
        self.title = title
        self.dueDate = dueDate
        self.isComplete = isComplete
    }
}

@Model
final class LifeGoal: Identifiable {
    @Attribute(.unique) var id: UUID
    var title: String
    var progress: Double

    init(id: UUID = UUID(), title: String, progress: Double = 0) {
        self.id = id
        self.title = title
        self.progress = progress
    }
}

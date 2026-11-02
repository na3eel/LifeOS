import Foundation
import SwiftData

@Model
final class Habit: Identifiable {
    @Attribute(.unique) var id: UUID
    var title: String
    var detail: String
    var isComplete: Bool
    var createdAt: Date

    init(id: UUID = UUID(), title: String, detail: String, isComplete: Bool = false, createdAt: Date = .now) {
        self.id = id
        self.title = title
        self.detail = detail
        self.isComplete = isComplete
        self.createdAt = createdAt
    }
}

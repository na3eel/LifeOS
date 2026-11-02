import Foundation

struct Habit: Identifiable, Equatable {
    let id: UUID
    var title: String
    var detail: String
    var isComplete: Bool

    init(id: UUID = UUID(), title: String, detail: String, isComplete: Bool = false) {
        self.id = id
        self.title = title
        self.detail = detail
        self.isComplete = isComplete
    }
}

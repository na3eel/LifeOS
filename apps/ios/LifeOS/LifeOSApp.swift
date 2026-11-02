import SwiftUI
import SwiftData
@main struct LifeOSApp:App {
 var body:some Scene {
  WindowGroup { RootView().tint(Color.lifeOSBrand) }
   .modelContainer(for:[Habit.self,LifeArea.self,LifeTask.self,LifeGoal.self])
 }
}
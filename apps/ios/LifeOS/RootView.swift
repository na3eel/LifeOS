import SwiftUI
import SwiftData

private enum AppTab:Hashable{case today,areas,plan,insights,coach}
struct RootView:View{
 @Environment(\.modelContext) private var context
 @Query(sort:\Habit.createdAt) private var habits:[Habit]
 @Query(sort:\LifeArea.name) private var areas:[LifeArea]
 @Query private var tasks:[LifeTask]
 @Query private var goals:[LifeGoal]
 @State private var tab:AppTab = .today
 @State private var showingAdd=false
 @State private var addKind="Habit"
 private var done:Int{habits.filter(\.isComplete).count}
 var body:some View{
  TabView(selection:$tab){
   today.tabItem{Label("Today",systemImage:"checkmark.circle")}.tag(AppTab.today)
   areasScreen.tabItem{Label("Areas",systemImage:"square.grid.2x2")}.tag(AppTab.areas)
   planScreen.tabItem{Label("Plan",systemImage:"calendar")}.tag(AppTab.plan)
   insightsScreen.tabItem{Label("Insights",systemImage:"chart.bar.xaxis")}.tag(AppTab.insights)
   coachScreen.tabItem{Label("Coach",systemImage:"bubble.left.and.bubble.right")}.tag(AppTab.coach)
  }.sheet(isPresented:$showingAdd){AddSheet(kind:addKind){name,note,area in
   switch addKind{case "Habit":context.insert(Habit(title:name,detail:note,area:area));case "Task":context.insert(LifeTask(title:name));case "Goal":context.insert(LifeGoal(title:name));case "Area":context.insert(LifeArea(name:name,detail:note));default:break}
  }}.task{if areas.isEmpty{["Body","Mind","Soul","Social","Career"].forEach{context.insert(LifeArea(name:$0))}}}
 }
 private var today:some View{NavigationStack{ScrollView{VStack(alignment:.leading,spacing:18){
  Text(Date.now.formatted(.dateTime.weekday(.wide).month(.wide).day()).uppercased()).font(.caption.weight(.semibold)).foregroundStyle(.secondary)
  Text("Make today count.").font(.largeTitle.weight(.semibold))
  VStack(alignment:.leading,spacing:10){Text("DAILY RHYTHM").font(.caption.weight(.semibold));Text("\(done) / \(habits.count)").font(.largeTitle);ProgressView(value:habits.isEmpty ? 0:Double(done)/Double(habits.count)).tint(Color.lifeOSBrand)}.padding(20).frame(maxWidth:.infinity,alignment:.leading).background(Color.lifeOSAccentSoft,in:RoundedRectangle(cornerRadius:22))
  HStack{Text("Your habits").font(.title2.weight(.semibold));Spacer();Button("Add",action:{addKind="Habit";showingAdd=true})}
  if habits.isEmpty{ContentUnavailableView("Start with one small habit",systemImage:"checkmark.circle",description:Text("Choose something meaningful and make it part of your day."))}
  ForEach(habits){h in HStack{VStack(alignment:.leading){Text(h.title).font(.body.weight(.medium));Text(h.detail.isEmpty ? h.area:h.detail).font(.caption).foregroundStyle(.secondary)};Spacer();Toggle("Complete",isOn:Bindable(h).isComplete).labelsHidden().tint(Color.lifeOSBrand);Button(role:.destructive){context.delete(h)}label:{Image(systemName:"trash")}.accessibilityLabel("Delete "+h.title)}.padding(12).background(Color.lifeOSCard,in:RoundedRectangle(cornerRadius:14))}
  Text("Today’s plan").font(.title2.weight(.semibold))
  ForEach(tasks){t in Toggle(t.title,isOn:Bindable(t).isComplete)}
 }.padding(20).frame(maxWidth:680).frame(maxWidth:.infinity)}.background(Color.lifeOSCanvas).navigationTitle("Today").toolbar{ToolbarItem(placement:.topBarTrailing){Button{addKind="Habit";showingAdd=true}label:{Image(systemName:"plus")}.accessibilityLabel("Add habit")}}}}
 private var areasScreen:some View{NavigationStack{List{Section("Life areas"){ForEach(areas){a in VStack(alignment:.leading){Text(a.name).font(.headline);Text(a.detail.isEmpty ? "Build a rhythm that supports this area.":a.detail).font(.caption).foregroundStyle(.secondary)}}};Section("Goals"){if goals.isEmpty{ContentUnavailableView("No goals yet",systemImage:"flag",description:Text("Turn an intention into a milestone."))};ForEach(goals){g in VStack(alignment:.leading){Text(g.title);Slider(value:Bindable(g).progress,in:0...100);Text("\(Int(g.progress))% complete").font(.caption)}};Button("Create goal"){addKind="Goal";showingAdd=true}}}.navigationTitle("Life areas").toolbar{ToolbarItem(placement:.topBarTrailing){Button{addKind="Area";showingAdd=true}label:{Image(systemName:"plus")}.accessibilityLabel("Add life area")}}}}
 private var planScreen:some View{NavigationStack{List{if tasks.isEmpty{ContentUnavailableView("Your plan is clear",systemImage:"calendar",description:Text("Add a task or commitment to get started."))};ForEach(tasks){t in Toggle(t.title,isOn:Bindable(t).isComplete)}.onDelete{idx in idx.map{tasks[$0]}.forEach(context.delete)}}.navigationTitle("Your plan").toolbar{ToolbarItem(placement:.topBarTrailing){Button{addKind="Task";showingAdd=true}label:{Image(systemName:"plus")}}}}}
 private var insightsScreen:some View{NavigationStack{List{Section{Text("A simple view of the consistency you’re building.").foregroundStyle(.secondary)};Section("Your activity"){metric("Habits created",habits.count);metric("Completed habits",done);metric("Open tasks",tasks.filter{!$0.isComplete}.count);metric("Goals in progress",goals.filter{$0.progress<100}.count)}}.navigationTitle("Insights")}}
 private func metric(_ title:String,_ value:Int)->some View{HStack{Text(title);Spacer();Text(value.description).font(.title3.weight(.semibold)).monospacedDigit()}}
 private var coachScreen:some View{NavigationStack{List{Section{Text("A small next step").font(.title2.weight(.semibold));Text(habits.isEmpty ? "Start with one habit that makes tomorrow easier.":done==habits.count ? "You’ve completed today’s habits. Take a moment to recognise that progress.":"Choose the easiest remaining habit and give it five focused minutes.");Text("On-device guidance for now. AI coaching will connect when the service is ready.").font(.caption).foregroundStyle(.secondary)};Section("Life areas"){ForEach(["Body","Mind","Soul","Social","Career"],id:\.self){Text($0)}}}.navigationTitle("Your coach")}}
}
private struct AddSheet:View{
 @Environment(\.dismiss) private var dismiss
 let kind:String;let save:(String,String,String)->Void
 @State private var name="";@State private var note="";@State private var area="Mind"
 var body:some View{NavigationStack{Form{Section(kind){TextField("Name",text:$name);if kind=="Habit" || kind=="Area"{TextField("Optional note",text:$note)};if kind=="Habit"{Picker("Life area",selection:$area){ForEach(["Body","Mind","Soul","Social","Career"],id:\.self){Text($0)}}}}}.navigationTitle("New "+kind.lowercased()).toolbar{ToolbarItem(placement:.cancellationAction){Button("Cancel"){dismiss()}};ToolbarItem(placement:.confirmationAction){Button("Save"){let value=name.trimmingCharacters(in:.whitespacesAndNewlines);guard !value.isEmpty else{return};save(value,note,area);dismiss()}.disabled(name.trimmingCharacters(in:.whitespacesAndNewlines).isEmpty)}}}}
}

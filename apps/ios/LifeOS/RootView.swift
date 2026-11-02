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
  }.tint(Color.lifeOSBrand).sheet(isPresented:$showingAdd){AddSheet(kind:addKind){name,note,area in
   switch addKind{case "Habit":context.insert(Habit(title:name,detail:note,area:area));case "Task":context.insert(LifeTask(title:name));case "Goal":context.insert(LifeGoal(title:name));case "Area":context.insert(LifeArea(name:name,detail:note));default:break}
  }}.task{if areas.isEmpty{["Body","Mind","Soul","Social","Career"].forEach{context.insert(LifeArea(name:$0))}}}
 }
 private var today:some View {
  NavigationStack {
   ScrollView {
    VStack(alignment:.leading,spacing:20) {
     VStack(alignment:.leading,spacing:5) {
      Text(Date.now.formatted(.dateTime.weekday(.wide).month(.wide).day()).uppercased()).font(.caption.weight(.semibold)).foregroundStyle(.secondary)
      Text("Good morning.").font(.largeTitle.weight(.bold))
      Text("Make space for what matters today.").font(.body).foregroundStyle(.secondary)
     }
     VStack(alignment:.leading,spacing:14) {
      HStack { Text("YOUR DAY").font(.caption.weight(.semibold)).foregroundStyle(.secondary); Spacer(); Text(Date.now.formatted(.dateTime.day().month(.abbreviated))).font(.subheadline.weight(.medium)) }
      HStack(spacing:16) {
       ZStack {
        Circle().stroke(Color.lifeOSBrand.opacity(0.15),lineWidth:9)
        Circle().trim(from:0,to:habits.isEmpty ? 0 : CGFloat(done)/CGFloat(habits.count)).stroke(Color.lifeOSBrand,style:StrokeStyle(lineWidth:9,lineCap:.round)).rotationEffect(.degrees(-90))
        Text(habits.isEmpty ? "—" : "\(done*100/habits.count)%").font(.title2.weight(.bold)).monospacedDigit()
       }.frame(width:82,height:82).accessibilityLabel("Habit progress")
       VStack(alignment:.leading,spacing:5) {
        Text("Daily progress").font(.headline)
        Text("\(done) of \(habits.count) habits complete").font(.subheadline)
        Text("\(tasks.filter{$0.isComplete}.count) of \(tasks.count) tasks complete").font(.caption).foregroundStyle(.secondary)
       }
       Spacer(minLength:0)
      }
     }.padding(18).frame(maxWidth:.infinity,alignment:.leading).background(Color.lifeOSCard,in:RoundedRectangle(cornerRadius:22))
     VStack(alignment:.leading,spacing:10) {
      HStack { Text("Today's focus").font(.title2.weight(.bold)); Spacer(); Button("View plan"){tab = .plan} }
      if let focus = tasks.first(where:{ !$0.isComplete }) {
       HStack(spacing:12) {
        VStack(alignment:.leading,spacing:5) {
         Text("ONE THING TO MOVE FORWARD").font(.caption2.weight(.semibold)).foregroundStyle(.secondary)
         Text(focus.title).font(.headline)
         Text("A small, focused step is enough to begin.").font(.caption).foregroundStyle(.secondary)
        }
        Spacer()
        Button { focus.isComplete = true } label: { Image(systemName:"checkmark").font(.headline).foregroundStyle(.white).padding(13).background(Color.lifeOSBrand,in:Circle()) }.accessibilityLabel("Complete focus task")
       }.padding(16).background(Color.lifeOSAccentSoft,in:RoundedRectangle(cornerRadius:20))
      } else {
       ContentUnavailableView("Choose your most important task",systemImage:"scope",description:Text("Add a task to give today a clear focus."))
      }
     }
     HStack { Text("Your habits").font(.title2.weight(.bold)); Spacer(); Text("\(done)/\(habits.count)").font(.subheadline.weight(.medium)).foregroundStyle(.secondary) }
     if habits.isEmpty {
      ContentUnavailableView("Start with one small habit",systemImage:"checkmark.circle",description:Text("Choose a routine that supports the person you want to become."))
      Button { addKind="Habit";showingAdd=true } label: { Label("Add your first habit",systemImage:"plus").frame(maxWidth:.infinity) }.buttonStyle(.borderedProminent)
     } else {
      VStack(spacing:0) {
       ForEach(habits) { h in
        HStack(spacing:12) {
         VStack(alignment:.leading,spacing:3) { Text(h.title).font(.body.weight(.semibold)); Text(h.detail.isEmpty ? h.area:h.detail).font(.caption).foregroundStyle(.secondary) }
         Spacer()
         Toggle("Complete",isOn:Bindable(h).isComplete).labelsHidden().tint(Color.lifeOSBrand).accessibilityLabel("Complete \(h.title)")
        }.padding(.vertical,12)
        if h.id != habits.last?.id { Divider() }
       }
      }.padding(.horizontal,16).background(Color.lifeOSCard,in:RoundedRectangle(cornerRadius:20))
     }
     VStack(alignment:.leading,spacing:8) {
      Text("Coming up").font(.title2.weight(.bold))
      if tasks.isEmpty { Text("Your schedule is open. Add a task when something needs your attention.").font(.subheadline).foregroundStyle(.secondary) }
      else { ForEach(tasks.prefix(3)) { task in HStack(spacing:10) { Image(systemName:task.isComplete ? "checkmark.circle.fill":"calendar").foregroundStyle(task.isComplete ? Color.lifeOSBrand:Color.lifeOSSecondary); Text(task.title); Spacer(); if task.isComplete { Text("Done").font(.caption).foregroundStyle(.secondary) } }.padding(.vertical,5) } }
     }.padding(16).frame(maxWidth:.infinity,alignment:.leading).background(Color.lifeOSCard,in:RoundedRectangle(cornerRadius:20))
     Button { addKind="Habit";showingAdd=true } label: { Label("Add a habit",systemImage:"plus").frame(maxWidth:.infinity) }.buttonStyle(.bordered)
    }.padding(20).frame(maxWidth:680).frame(maxWidth:.infinity)
   }.background(Color.lifeOSCanvas).navigationTitle("Today").toolbar {
    ToolbarItem(placement:.principal) { Text("LifeOS").font(.system(size:22,weight:.heavy,design:.rounded)).tracking(-1.1) }
    ToolbarItem(placement:.topBarTrailing) { Button { addKind="Habit";showingAdd=true } label:{ Image(systemName:"plus") }.accessibilityLabel("Add habit") }
   }
  }
 }
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

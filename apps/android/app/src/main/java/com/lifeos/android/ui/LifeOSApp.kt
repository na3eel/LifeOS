package com.lifeos.android.ui
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.lifeos.android.data.*
import kotlinx.coroutines.launch
import java.util.UUID

private enum class Tab { Today, Areas, Plan, Insights, Coach }
private val lifeAreas=listOf("Body","Mind","Soul","Social","Career")

@OptIn(ExperimentalMaterial3Api::class)
@Composable fun LifeOSApp(){
 val context = LocalContext.current
 val dao = remember(context) { LifeOSDatabase.get(context).dao() }
 val scope= rememberCoroutineScope()
 val habits by dao.habits().collectAsStateWithLifecycle(emptyList())
 val areas by dao.areas().collectAsStateWithLifecycle(emptyList())
 val tasks by dao.tasks().collectAsStateWithLifecycle(emptyList())
 val goals by dao.goals().collectAsStateWithLifecycle(emptyList())
 var tab by remember { mutableStateOf(Tab.Today) }
 var dialog by remember { mutableStateOf(false) }
 var kind by remember { mutableStateOf("Habit") }
 var title by remember { mutableStateOf("") }
 var note by remember { mutableStateOf("") }
 var area by remember { mutableStateOf("Mind") }
 LaunchedEffect(Unit){if(areas.isEmpty()) lifeAreas.forEach{dao.addArea(AreaEntity(it.lowercase(),it))}}
 Scaffold(topBar={TopAppBar(title={Text("LifeOS",fontFamily=androidx.compose.ui.text.font.FontFamily.SansSerif,fontWeight=FontWeight.Black,letterSpacing=(-1.1).sp)},actions={IconButton(onClick={kind=when(tab){Tab.Plan->"Task";Tab.Areas->"Area";else->"Habit"};dialog=true}){Icon(Icons.Outlined.Add,"Add")}})},
 bottomBar={NavigationBar{listOf(Tab.Today,Tab.Areas,Tab.Plan,Tab.Insights,Tab.Coach).forEach{t->NavigationBarItem(selected = tab == t, onClick = { tab = t }, icon = { Icon(when(t){Tab.Today->Icons.Outlined.Today;Tab.Areas->Icons.Outlined.GridView;Tab.Plan->Icons.Outlined.CalendarMonth;Tab.Insights->Icons.Outlined.Insights;Tab.Coach->Icons.Outlined.Forum}, contentDescription = t.name) }, label = { Text(if(t==Tab.Plan)"Plan" else t.name) })}}}){pad->
 when(tab){
 Tab.Today->LazyColumn(Modifier.fillMaxSize().padding(pad),contentPadding=PaddingValues(start=20.dp,end=20.dp,top=12.dp,bottom=28.dp),verticalArrangement=Arrangement.spacedBy(18.dp)){
 item {
  Column(verticalArrangement=Arrangement.spacedBy(5.dp)){
   Text(java.time.LocalDate.now().format(java.time.format.DateTimeFormatter.ofPattern("EEEE, d MMMM")).uppercase(),style=MaterialTheme.typography.labelMedium,color=MaterialTheme.colorScheme.onSurfaceVariant)
   Text("Good morning.",style=MaterialTheme.typography.headlineLarge,fontWeight=FontWeight.Bold)
   Text("Make space for what matters today.",style=MaterialTheme.typography.bodyLarge,color=MaterialTheme.colorScheme.onSurfaceVariant)
  }
 }
 item {
  Card(shape=RoundedCornerShape(24.dp),colors=CardDefaults.cardColors(containerColor=MaterialTheme.colorScheme.surfaceVariant)){
   Column(Modifier.fillMaxWidth().padding(18.dp),verticalArrangement=Arrangement.spacedBy(12.dp)){
    Row(verticalAlignment=Alignment.CenterVertically,horizontalArrangement=Arrangement.SpaceBetween,modifier=Modifier.fillMaxWidth()){
     Text("YOUR DAY",style=MaterialTheme.typography.labelLarge,color=MaterialTheme.colorScheme.onSurfaceVariant)
     Text(java.time.LocalDate.now().format(java.time.format.DateTimeFormatter.ofPattern("d MMM")),style=MaterialTheme.typography.labelLarge)
    }
    Row(verticalAlignment=Alignment.CenterVertically,horizontalArrangement=Arrangement.spacedBy(16.dp)){
     Box(Modifier.size(84.dp),contentAlignment=Alignment.Center){
      CircularProgressIndicator(progress={if(habits.isEmpty())0f else habits.count{it.done}.toFloat()/habits.size},modifier=Modifier.fillMaxSize(),strokeWidth=8.dp,color=MaterialTheme.colorScheme.primary,trackColor=MaterialTheme.colorScheme.outlineVariant)
      Text(if(habits.isEmpty())"—" else "${(habits.count{it.done}*100/habits.size)}%",fontWeight=FontWeight.Bold,style=MaterialTheme.typography.titleLarge)
     }
     Column(verticalArrangement=Arrangement.spacedBy(4.dp)){
      Text("Daily progress",style=MaterialTheme.typography.titleMedium,fontWeight=FontWeight.SemiBold)
      Text("${habits.count{it.done}} of ${habits.size} habits complete",style=MaterialTheme.typography.bodyMedium)
      Text("${tasks.count{it.done}} of ${tasks.size} tasks complete",style=MaterialTheme.typography.bodySmall,color=MaterialTheme.colorScheme.onSurfaceVariant)
     }
    }
   }
  }
 }
 item {
  Column(verticalArrangement=Arrangement.spacedBy(10.dp)){
   Row(verticalAlignment=Alignment.CenterVertically,horizontalArrangement=Arrangement.SpaceBetween,modifier=Modifier.fillMaxWidth()){
    Text("Today's focus",style=MaterialTheme.typography.titleLarge,fontWeight=FontWeight.Bold)
    TextButton(onClick={tab=Tab.Plan}){Text("View plan")}
   }
   val focusTask=tasks.firstOrNull{!it.done}
   Card(shape=RoundedCornerShape(22.dp),colors=CardDefaults.cardColors(containerColor=MaterialTheme.colorScheme.primaryContainer)){
    Row(Modifier.fillMaxWidth().padding(18.dp),verticalAlignment=Alignment.CenterVertically){
     Column(Modifier.weight(1f),verticalArrangement=Arrangement.spacedBy(5.dp)){
      Text("ONE THING TO MOVE FORWARD",style=MaterialTheme.typography.labelSmall)
      Text(focusTask?.title ?: "Choose your most important task",style=MaterialTheme.typography.titleMedium,fontWeight=FontWeight.SemiBold)
      Text(if(focusTask==null)"Add a task to give today a clear focus." else "A small, focused step is enough to begin.",style=MaterialTheme.typography.bodySmall)
     }
     if(focusTask!=null) FilledTonalButton(onClick={scope.launch{dao.updateTask(focusTask.copy(done=true))}}){Text("Done")}
     else Icon(Icons.Outlined.Flag,"Focus")
    }
   }
  }
 }
 item {
  Row(verticalAlignment=Alignment.CenterVertically,horizontalArrangement=Arrangement.SpaceBetween,modifier=Modifier.fillMaxWidth()){
   Text("Your habits",style=MaterialTheme.typography.titleLarge,fontWeight=FontWeight.Bold)
   Text("${habits.count{it.done}}/${habits.size}",style=MaterialTheme.typography.labelLarge,color=MaterialTheme.colorScheme.onSurfaceVariant)
  }
 }
 if(habits.isEmpty()) item {Card(shape=RoundedCornerShape(20.dp)){Column(Modifier.fillMaxWidth().padding(20.dp),verticalArrangement=Arrangement.spacedBy(8.dp)){Text("Start with one small habit",style=MaterialTheme.typography.titleMedium);Text("Choose a routine that supports the person you want to become.",color=MaterialTheme.colorScheme.onSurfaceVariant);Button(onClick={kind="Habit";dialog=true}){Text("Add your first habit")}}}}
 items(habits,key={it.id}){h->Card(shape=RoundedCornerShape(18.dp),colors=CardDefaults.cardColors(containerColor=MaterialTheme.colorScheme.surface)){Row(Modifier.fillMaxWidth().padding(horizontal=14.dp,vertical=12.dp),verticalAlignment=Alignment.CenterVertically){Column(Modifier.weight(1f),verticalArrangement=Arrangement.spacedBy(2.dp)){Text(h.title,fontWeight=FontWeight.SemiBold);Text(h.note.ifBlank{h.area},style=MaterialTheme.typography.bodySmall,color=MaterialTheme.colorScheme.onSurfaceVariant)};Checkbox(checked=h.done,onCheckedChange={v->scope.launch{dao.updateHabit(h.copy(done=v))}})}}}
 item {
  Column(verticalArrangement=Arrangement.spacedBy(8.dp)){
   Text("Coming up",style=MaterialTheme.typography.titleLarge,fontWeight=FontWeight.Bold)
   if(tasks.isEmpty())Text("Your schedule is open. Add a task when something needs your attention.",color=MaterialTheme.colorScheme.onSurfaceVariant)
   else tasks.take(3).forEach{t->ListItem(headlineContent={Text(t.title)},leadingContent={Icon(Icons.Outlined.CalendarToday,null,color=MaterialTheme.colorScheme.secondary)},trailingContent={if(t.done)Icon(Icons.Outlined.CheckCircle,null,color=MaterialTheme.colorScheme.primary)})}
  }
 }
 item {OutlinedButton(onClick={kind="Habit";dialog=true},modifier=Modifier.fillMaxWidth()){Icon(Icons.Outlined.Add,null);Spacer(Modifier.width(8.dp));Text("Add a habit")}}
}
 Tab.Areas->LazyColumn(Modifier.fillMaxSize().padding(pad).padding(20.dp),verticalArrangement=Arrangement.spacedBy(10.dp)){
 item{Text("Life areas",style=MaterialTheme.typography.headlineLarge);Text("Build a balanced rhythm across the parts of life.");Button(onClick={kind="Area";dialog=true}){Text("Add life area")}}
 items(areas,key={it.id}){a->Card{Column(Modifier.fillMaxWidth().padding(16.dp)){Text(a.name,style=MaterialTheme.typography.titleLarge);Text(habits.count{it.area==a.name}.toString()+" habits");TextButton(onClick={area=a.name;kind="Habit";dialog=true}){Text("Add habit")}}}}
 item{Text("Goals",style=MaterialTheme.typography.titleLarge);Button(onClick={kind="Goal";dialog=true}){Text("Create goal")}}
 items(goals,key={it.id}){g->Card{Column(Modifier.padding(16.dp)){Text(g.title);Slider(g.progress.toFloat(),{v->scope.launch{dao.updateGoal(g.copy(progress=v.toInt()))}},valueRange=0f..100f);Text(g.progress.toString()+"% complete")}}}
 }
 Tab.Plan->LazyColumn(Modifier.fillMaxSize().padding(pad).padding(20.dp),verticalArrangement=Arrangement.spacedBy(10.dp)){
 item{Text("Your plan",style=MaterialTheme.typography.headlineLarge);Text("Tasks and commitments in one place.");Button(onClick={kind="Task";dialog=true}){Text("Add task")}}
 if(tasks.isEmpty())item{Empty("Your plan is clear","Add a task or commitment to get started.")}
 items(tasks,key={it.id}){t->Card{Row(Modifier.fillMaxWidth().padding(12.dp),verticalAlignment=Alignment.CenterVertically){Checkbox(t.done,{v->scope.launch{dao.updateTask(t.copy(done=v))}});Text(t.title,Modifier.weight(1f));IconButton({scope.launch{dao.deleteTask(t)}}){Icon(Icons.Outlined.DeleteOutline,"Delete task")}}}}
 }
 Tab.Insights->LazyColumn(Modifier.fillMaxSize().padding(pad).padding(20.dp),verticalArrangement=Arrangement.spacedBy(12.dp)){
 item{Text("Your insights",style=MaterialTheme.typography.headlineLarge);Text("A simple view of the consistency you’re building.")}
 item{Metric("Habits created",habits.size.toString())};item{Metric("Completed habits",habits.count{it.done}.toString())};item{Metric("Open tasks",tasks.count{!it.done}.toString())};item{Metric("Goals in progress",goals.count{it.progress<100}.toString())}
 }
 Tab.Coach->Column(Modifier.fillMaxSize().padding(pad).padding(24.dp),verticalArrangement=Arrangement.spacedBy(14.dp)){
 Text("Your coach",style=MaterialTheme.typography.headlineLarge);Text("A small next step",style=MaterialTheme.typography.titleLarge)
 Text(if(habits.isEmpty())"Start with one habit that makes tomorrow easier." else if(habits.all{it.done})"You’ve completed today’s habits. Take a moment to recognise that progress." else "Choose the easiest remaining habit and give it five focused minutes.")
 Text("This is on-device guidance. AI coaching will be connected when the service is ready.",style=MaterialTheme.typography.bodySmall,color=MaterialTheme.colorScheme.onSurfaceVariant)
 }
 }
 }
 if(dialog)AlertDialog(onDismissRequest={dialog=false},title={Text("New "+kind.lowercase())},text={Column(verticalArrangement=Arrangement.spacedBy(8.dp)){OutlinedTextField(title,{title=it},label={Text("Name")},singleLine=true);if(kind=="Habit"){OutlinedTextField(note,{note=it},label={Text("Optional note")});Text("Life area");areas.map{it.name}.forEach{FilterChip(area==it,{area=it},{Text(it)})}}}},confirmButton={TextButton(enabled=title.isNotBlank(),onClick={val clean=title.trim();scope.launch{when(kind){"Habit"->dao.addHabit(HabitEntity(UUID.randomUUID().toString(),clean,note.trim(),area));"Task"->dao.addTask(TaskEntity(UUID.randomUUID().toString(),clean));"Goal"->dao.addGoal(GoalEntity(UUID.randomUUID().toString(),clean));"Area"->dao.addArea(AreaEntity(UUID.randomUUID().toString(),clean))}};title="";note="";dialog=false}){Text("Save")}},dismissButton={TextButton(onClick={dialog=false}){Text("Cancel")}})
}
@Composable private fun Empty(title:String,detail:String){Card{Column(Modifier.fillMaxWidth().padding(20.dp),horizontalAlignment=Alignment.CenterHorizontally){Text(title,style=MaterialTheme.typography.titleMedium);Text(detail,color=MaterialTheme.colorScheme.onSurfaceVariant)}}}
@Composable private fun Metric(label:String,value:String){Card{Row(Modifier.fillMaxWidth().padding(18.dp),horizontalArrangement=Arrangement.SpaceBetween){Text(label);Text(value,style=MaterialTheme.typography.headlineSmall)}}}

package com.lifeos.android.ui
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.Image
import androidx.compose.ui.res.painterResource
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
 var editingHabit by remember { mutableStateOf<HabitEntity?>(null) }
 val openAddDialog: (String) -> Unit = { selectedKind -> editingHabit=null;kind=selectedKind;title="";note="";area="Mind";dialog=true }
 LaunchedEffect(Unit){if(areas.isEmpty()) lifeAreas.forEach{dao.addArea(AreaEntity(it.lowercase(),it))}}
 Scaffold(containerColor=MaterialTheme.colorScheme.background,topBar={TopAppBar(colors=TopAppBarDefaults.topAppBarColors(containerColor=MaterialTheme.colorScheme.background),title={Row(verticalAlignment=Alignment.CenterVertically,horizontalArrangement=Arrangement.spacedBy(10.dp)){Image(painterResource(com.lifeos.android.R.drawable.lifeos_mark),contentDescription="LifeOS logo",modifier=Modifier.size(34.dp));Text("LifeOS",fontFamily=androidx.compose.ui.text.font.FontFamily.SansSerif,fontWeight=FontWeight.Black,letterSpacing=(-1.1).sp)}},actions={IconButton(onClick={openAddDialog(when(tab){Tab.Plan->"Task";Tab.Areas->"Area";else->"Habit"})}){Icon(Icons.Outlined.Add,"Add")}})},
 bottomBar={NavigationBar(containerColor=MaterialTheme.colorScheme.surface, tonalElevation=0.dp){listOf(Tab.Today,Tab.Areas,Tab.Plan,Tab.Insights,Tab.Coach).forEach{t->NavigationBarItem(selected = tab == t, onClick = { tab = t }, colors=NavigationBarItemDefaults.colors(selectedIconColor=MaterialTheme.colorScheme.primary,selectedTextColor=MaterialTheme.colorScheme.primary,indicatorColor=MaterialTheme.colorScheme.primaryContainer), icon = { Icon(when(t){Tab.Today->Icons.Outlined.CheckCircle;Tab.Areas->Icons.Outlined.GridView;Tab.Plan->Icons.Outlined.CalendarMonth;Tab.Insights->Icons.Outlined.AutoGraph;Tab.Coach->Icons.Outlined.SelfImprovement}, contentDescription = t.name) }, label = { Text(if(t==Tab.Plan)"Calendar" else t.name) })}}}){pad->
 when(tab){
 Tab.Today->LazyColumn(Modifier.fillMaxSize().padding(pad).padding(20.dp),verticalArrangement=Arrangement.spacedBy(12.dp)){
 item{Text("YOUR DAY",style=MaterialTheme.typography.labelMedium);Text("Make today count.",style=MaterialTheme.typography.headlineLarge,fontWeight=FontWeight.SemiBold)
 Card(shape=RoundedCornerShape(22.dp),colors=CardDefaults.cardColors(containerColor=MaterialTheme.colorScheme.secondaryContainer)){Column(Modifier.fillMaxWidth().padding(18.dp)){Text("DAILY RHYTHM");Text(habits.count{it.done}.toString()+" / "+habits.size+" complete",style=MaterialTheme.typography.headlineMedium);LinearProgressIndicator(progress={if(habits.isEmpty())0f else habits.count{it.done}.toFloat()/habits.size},modifier=Modifier.fillMaxWidth())}}
 Text("Your habits",style=MaterialTheme.typography.titleLarge)}
 if(habits.isEmpty())item{Empty("No habits yet","Start with one small habit. Add something meaningful to your day.")}
 items(habits,key={it.id}){h->Card{Row(Modifier.fillMaxWidth().padding(12.dp),verticalAlignment=Alignment.CenterVertically){Column(Modifier.weight(1f)){Text(h.title,fontWeight=FontWeight.Medium);Text(h.note.ifBlank{h.area},style=MaterialTheme.typography.bodySmall)};Checkbox(h.done,{v->scope.launch{dao.updateHabit(h.copy(done=v))}});IconButton(onClick={editingHabit=h;title=h.title;note=h.note;area=h.area;kind="Habit";dialog=true}){Icon(Icons.Outlined.Edit,"Edit habit")};IconButton({scope.launch{dao.deleteHabit(h)}}){Icon(Icons.Outlined.DeleteOutline,"Delete")}}}}
 item{Button(onClick={openAddDialog("Habit")},modifier=Modifier.fillMaxWidth()){Text("Add habit")};Text("Today’s plan",style=MaterialTheme.typography.titleLarge);if(tasks.isEmpty())Text("No tasks planned yet.") }
 items(tasks.take(5),key={it.id}){t->ListItem(headlineContent={Text(t.title)},leadingContent={Checkbox(t.done,{v->scope.launch{dao.updateTask(t.copy(done=v))}})})}
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
 if(dialog && kind=="Habit") HabitEditorSheet(
  initialTitle=title, initialNote=note, initialArea=area, isEditing=editingHabit!=null,
  areas=areas.map{it.name}.ifEmpty{lifeAreas},
  onDismiss={dialog=false;editingHabit=null;title="";note=""},
  onSave={newTitle,newNote,newArea->title=newTitle;note=newNote;area=newArea;val current=editingHabit;scope.launch{if(current!=null)dao.updateHabit(current.copy(title=newTitle,note=newNote,area=newArea)) else dao.addHabit(HabitEntity(UUID.randomUUID().toString(),newTitle,newNote,newArea))};dialog=false;editingHabit=null}
 ) else if(dialog) AlertDialog(onDismissRequest={dialog=false},title={Text("New "+kind.lowercase())},text={OutlinedTextField(title,{title=it},label={Text("Name")},singleLine=true)},confirmButton={TextButton(enabled=title.isNotBlank(),onClick={val clean=title.trim();scope.launch{when(kind){"Task"->dao.addTask(TaskEntity(UUID.randomUUID().toString(),clean));"Goal"->dao.addGoal(GoalEntity(UUID.randomUUID().toString(),clean));"Area"->dao.addArea(AreaEntity(UUID.randomUUID().toString(),clean))}};title="";dialog=false}){Text("Save")}},dismissButton={TextButton(onClick={dialog=false}){Text("Cancel")}})
}
@Composable private fun Empty(title:String,detail:String){Card{Column(Modifier.fillMaxWidth().padding(20.dp),horizontalAlignment=Alignment.CenterHorizontally){Text(title,style=MaterialTheme.typography.titleMedium);Text(detail,color=MaterialTheme.colorScheme.onSurfaceVariant)}}}
@Composable private fun Metric(label:String,value:String){Card{Row(Modifier.fillMaxWidth().padding(18.dp),horizontalArrangement=Arrangement.SpaceBetween){Text(label);Text(value,style=MaterialTheme.typography.headlineSmall)}}}

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
import java.time.LocalDate
import java.time.DayOfWeek

private enum class Tab { Today, Areas, Plan, Insights, Coach }
private val lifeAreas = listOf("Body", "Mind", "Soul", "Social", "Career")

@OptIn(ExperimentalMaterial3Api::class)
@Composable fun LifeOSApp() {
    val context = LocalContext.current
    val dao = remember(context) { LifeOSDatabase.get(context).dao() }
    val scope = rememberCoroutineScope()
    val habits by dao.habits().collectAsStateWithLifecycle(emptyList())
    val areas by dao.areas().collectAsStateWithLifecycle(emptyList())
    val tasks by dao.tasks().collectAsStateWithLifecycle(emptyList())
    val goals by dao.goals().collectAsStateWithLifecycle(emptyList())
    val today = LocalDate.now()
    var selectedDate by remember { mutableStateOf(today) }
    val completionsFlow = remember(selectedDate) { dao.completionsForDate(selectedDate.toString()) }
    val completions by completionsFlow.collectAsStateWithLifecycle(emptyList())
    val completedHabitIds = remember(completions) { completions.map { it.habitId }.toSet() }
    var tab by remember { mutableStateOf(Tab.Today) }
    var dialog by remember { mutableStateOf(false) }
    var kind by remember { mutableStateOf("Habit") }
    var title by remember { mutableStateOf("") }
    var note by remember { mutableStateOf("") }
    var area by remember { mutableStateOf("Mind") }
    var editingHabit by remember { mutableStateOf<HabitEntity?>(null) }
    val selectedDay = selectedDate.dayOfWeek.value
    val selectedDateHabits = habits.filter { selectedDay in it.scheduledWeekdays.split(",").mapNotNull(String::toIntOrNull) }
    val openAddDialog: (String) -> Unit = { selectedKind ->
        editingHabit = null; kind = selectedKind; title = ""; note = ""; area = "Mind"; dialog = true
    }
    LaunchedEffect(Unit) { if (areas.isEmpty()) lifeAreas.forEach { dao.addArea(AreaEntity(it.lowercase(), it)) } }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.background),
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        Image(painterResource(com.lifeos.android.R.drawable.lifeos_mark), contentDescription = "LifeOS logo", modifier = Modifier.size(34.dp))
                        Text("LifeOS", fontFamily = androidx.compose.ui.text.font.FontFamily.SansSerif, fontWeight = FontWeight.Black, letterSpacing = (-1.1).sp)
                    }
                },
                actions = { IconButton(onClick = { openAddDialog(when (tab) { Tab.Plan -> "Task"; Tab.Areas -> "Area"; else -> "Habit" }) }) { Icon(Icons.Outlined.Add, "Add") } }
            )
        },
        bottomBar = {
            NavigationBar(containerColor = MaterialTheme.colorScheme.surface, tonalElevation = 0.dp) {
                listOf(Tab.Today, Tab.Areas, Tab.Plan, Tab.Insights, Tab.Coach).forEach { t ->
                    NavigationBarItem(
                        selected = tab == t, onClick = { tab = t },
                        colors = NavigationBarItemDefaults.colors(selectedIconColor = MaterialTheme.colorScheme.primary, selectedTextColor = MaterialTheme.colorScheme.primary, indicatorColor = MaterialTheme.colorScheme.primaryContainer),
                        icon = { Icon(when (t) { Tab.Today -> Icons.Outlined.CheckCircle; Tab.Areas -> Icons.Outlined.GridView; Tab.Plan -> Icons.Outlined.CalendarMonth; Tab.Insights -> Icons.Outlined.AutoGraph; Tab.Coach -> Icons.Outlined.SelfImprovement }, contentDescription = t.name) },
                        label = { Text(if (t == Tab.Plan) "Calendar" else t.name) }
                    )
                }
            }
        }
    ) { pad ->
        when (tab) {
            Tab.Today -> LazyColumn(Modifier.fillMaxSize().padding(pad).padding(20.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                item {
                    Text(selectedDate.format(java.time.format.DateTimeFormatter.ofPattern("EEEE, d MMMM")).uppercase(), style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Text(if (selectedDate == today) "Make today count." else "Your day, your pace.", style = MaterialTheme.typography.headlineLarge, fontWeight = FontWeight.SemiBold)
                    WeekDateNavigator(selectedDate = selectedDate, today = today, onDateSelected = { selectedDate = it })
                    Card(shape = RoundedCornerShape(18.dp), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)) {
                        Column(Modifier.fillMaxWidth().padding(18.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            Text("DAILY RHYTHM", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            Text("${selectedDateHabits.count { it.id in completedHabitIds }} / ${selectedDateHabits.size} complete", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.SemiBold)
                            LinearProgressIndicator(progress = { if (selectedDateHabits.isEmpty()) 0f else selectedDateHabits.count { it.id in completedHabitIds }.toFloat() / selectedDateHabits.size }, modifier = Modifier.fillMaxWidth(), color = MaterialTheme.colorScheme.primary, trackColor = MaterialTheme.colorScheme.surface)
                        }
                    }
                    Text("Habits", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.SemiBold)
                }
                if (selectedDateHabits.isEmpty()) item { Empty("No habits due on this day", "Choose another date or adjust a habit's repeat days.") }
                items(selectedDateHabits, key = { it.id }) { h ->
                    Card {
                        Row(Modifier.fillMaxWidth().padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                            Column(Modifier.weight(1f)) {
                                Text(h.title, fontWeight = FontWeight.Medium)
                                Text(if (h.targetUnit.isBlank()) h.area else "${h.targetValue.toInt()} ${h.targetUnit} · ${h.area}", style = MaterialTheme.typography.bodySmall)
                            }
                            Checkbox(h.id in completedHabitIds, { checked -> scope.launch {
                                if (checked) dao.markHabitComplete(HabitCompletionEntity(h.id, selectedDate.toString(), System.currentTimeMillis()))
                                else dao.undoHabitCompletion(h.id, selectedDate.toString())
                            } })
                            IconButton(onClick = { editingHabit = h; title = h.title; note = h.note; area = h.area; kind = "Habit"; dialog = true }) { Icon(Icons.Outlined.Edit, "Edit habit") }
                            IconButton({ scope.launch { dao.deleteHabit(h) } }) { Icon(Icons.Outlined.DeleteOutline, "Delete") }
                        }
                    }
                }
                item {
                    Button(onClick = { openAddDialog("Habit") }, modifier = Modifier.fillMaxWidth()) { Text("Add habit") }
                    Text("Today’s plan", style = MaterialTheme.typography.titleLarge)
                    if (tasks.isEmpty()) Text("No tasks planned yet.")
                }
                items(tasks.take(5), key = { it.id }) { t ->
                    ListItem(headlineContent = { Text(t.title) }, leadingContent = { Checkbox(t.done, { v -> scope.launch { dao.updateTask(t.copy(done = v)) } }) })
                }
            }
            Tab.Areas -> LazyColumn(Modifier.fillMaxSize().padding(pad).padding(20.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                item {
                    Text("Life areas", style = MaterialTheme.typography.headlineLarge)
                    Text("Build a balanced rhythm across the parts of life.")
                    Button(onClick = { kind = "Area"; dialog = true }) { Text("Add life area") }
                }
                items(areas, key = { it.id }) { a ->
                    Card { Column(Modifier.fillMaxWidth().padding(16.dp)) {
                        Text(a.name, style = MaterialTheme.typography.titleLarge)
                        Text(habits.count { it.area == a.name }.toString() + " habits")
                        TextButton(onClick = { area = a.name; kind = "Habit"; dialog = true }) { Text("Add habit") }
                    } }
                }
                item { Text("Goals", style = MaterialTheme.typography.titleLarge); Button(onClick = { kind = "Goal"; dialog = true }) { Text("Create goal") } }
                items(goals, key = { it.id }) { g ->
                    Card { Column(Modifier.padding(16.dp)) {
                        Text(g.title)
                        Slider(g.progress.toFloat(), { v -> scope.launch { dao.updateGoal(g.copy(progress = v.toInt())) } }, valueRange = 0f..100f)
                        Text(g.progress.toString() + "% complete")
                    } }
                }
            }
            Tab.Plan -> LazyColumn(Modifier.fillMaxSize().padding(pad).padding(20.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                item { Text("Your plan", style = MaterialTheme.typography.headlineLarge); Text("Tasks and commitments in one place."); Button(onClick = { kind = "Task"; dialog = true }) { Text("Add task") } }
                if (tasks.isEmpty()) item { Empty("Your plan is clear", "Add a task or commitment to get started.") }
                items(tasks, key = { it.id }) { t ->
                    Card { Row(Modifier.fillMaxWidth().padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                        Checkbox(t.done, { v -> scope.launch { dao.updateTask(t.copy(done = v)) } })
                        Text(t.title, Modifier.weight(1f))
                        IconButton({ scope.launch { dao.deleteTask(t) } }) { Icon(Icons.Outlined.DeleteOutline, "Delete task") }
                    } }
                }
            }
            Tab.Insights -> LazyColumn(Modifier.fillMaxSize().padding(pad).padding(20.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                item { Text("Your insights", style = MaterialTheme.typography.headlineLarge); Text("A simple view of the consistency you’re building.") }
                item { Metric("Habits created", habits.size.toString()) }
                item { Metric("Completed today", completions.size.toString()) }
                item { Metric("Open tasks", tasks.count { !it.done }.toString()) }
                item { Metric("Goals in progress", goals.count { it.progress < 100 }.toString()) }
            }
            Tab.Coach -> Column(Modifier.fillMaxSize().padding(pad).padding(24.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
                Text("Your coach", style = MaterialTheme.typography.headlineLarge)
                Text("A small next step", style = MaterialTheme.typography.titleLarge)
                Text(if (habits.isEmpty()) "Start with one habit that makes tomorrow easier." else if (habits.all { it.done }) "You’ve completed today’s habits. Take a moment to recognise that progress." else "Choose the easiest remaining habit and give it five focused minutes.")
                Text("This is on-device guidance. AI coaching will be connected when the service is ready.", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
    }

    if (dialog && kind == "Habit") HabitEditorSheet(
        initialTitle = title, initialNote = note, initialArea = area,
        initialTargetValue = editingHabit?.targetValue ?: 1.0,
        initialTargetUnit = editingHabit?.targetUnit ?: "",
        initialWeekdays = editingHabit?.scheduledWeekdays?.split(",")?.mapNotNull(String::toIntOrNull) ?: (1..7).toList(),
        isEditing = editingHabit != null, areas = areas.map { it.name }.ifEmpty { lifeAreas },
        onDismiss = { dialog = false; editingHabit = null; title = ""; note = "" },
        onSave = { newTitle, newNote, newArea, targetValue, targetUnit, weekdays ->
            val current = editingHabit
            scope.launch {
                if (current != null) dao.updateHabit(current.copy(title = newTitle, note = newNote, area = newArea, targetValue = targetValue, targetUnit = targetUnit, scheduledWeekdays = weekdays.joinToString(",")))
                else dao.addHabit(HabitEntity(UUID.randomUUID().toString(), newTitle, newNote, newArea, targetValue = targetValue, targetUnit = targetUnit, scheduledWeekdays = weekdays.joinToString(",")))
            }
            dialog = false; editingHabit = null
        }
    ) else if (dialog) AlertDialog(
        onDismissRequest = { dialog = false }, title = { Text("New " + kind.lowercase()) },
        text = { OutlinedTextField(title, { title = it }, label = { Text("Name") }, singleLine = true) },
        confirmButton = {
            TextButton(enabled = title.isNotBlank(), onClick = {
                val clean = title.trim()
                scope.launch { when (kind) {
                    "Task" -> dao.addTask(TaskEntity(UUID.randomUUID().toString(), clean))
                    "Goal" -> dao.addGoal(GoalEntity(UUID.randomUUID().toString(), clean))
                    "Area" -> dao.addArea(AreaEntity(UUID.randomUUID().toString(), clean))
                } }
                title = ""; dialog = false
            }) { Text("Save") }
        },
        dismissButton = { TextButton(onClick = { dialog = false }) { Text("Cancel") } }
    )
}


@Composable
private fun WeekDateNavigator(
    selectedDate: LocalDate,
    today: LocalDate,
    onDateSelected: (LocalDate) -> Unit
) {
    val weekStart = selectedDate.with(DayOfWeek.MONDAY)
    val dates = (0..6).map(weekStart::plusDays)
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { onDateSelected(selectedDate.minusWeeks(1)) }) {
                Icon(Icons.Outlined.ChevronLeft, contentDescription = "Previous week")
            }
            Text(
                weekStart.format(java.time.format.DateTimeFormatter.ofPattern("d MMM")) +
                    " – " + weekStart.plusDays(6).format(java.time.format.DateTimeFormatter.ofPattern("d MMM")),
                style = MaterialTheme.typography.labelLarge
            )
            IconButton(onClick = { onDateSelected(selectedDate.plusWeeks(1)) }) {
                Icon(Icons.Outlined.ChevronRight, contentDescription = "Next week")
            }
        }
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(4.dp)) {
            dates.forEach { date ->
                val selected = date == selectedDate
                TextButton(
                    onClick = { onDateSelected(date) },
                    modifier = Modifier.weight(1f).heightIn(min = 56.dp),
                    contentPadding = PaddingValues(vertical = 6.dp, horizontal = 0.dp),
                    colors = ButtonDefaults.textButtonColors(
                        containerColor = if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface,
                        contentColor = if (selected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(2.dp)) {
                        Text(date.dayOfWeek.name.take(1), style = MaterialTheme.typography.labelSmall)
                        Text(date.dayOfMonth.toString(), style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                        if (date == today) Text("TODAY", style = MaterialTheme.typography.labelSmall)
                    }
                }
            }
        }
    }
}

@Composable private fun Empty(title: String, detail: String) {
    Card { Column(Modifier.fillMaxWidth().padding(20.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        Text(title, style = MaterialTheme.typography.titleMedium)
        Text(detail, color = MaterialTheme.colorScheme.onSurfaceVariant)
    } }
}
@Composable private fun Metric(label: String, value: String) {
    Card { Row(Modifier.fillMaxWidth().padding(18.dp), horizontalArrangement = Arrangement.SpaceBetween) {
        Text(label); Text(value, style = MaterialTheme.typography.headlineSmall)
    } }
}
package com.lifeos.android.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.santi020k.lumen.LumenButton
import com.santi020k.lumen.LumenButtonIntent
import com.santi020k.lumen.LumenCard
import com.santi020k.lumen.LumenCardVariant

private data class HabitPreset(val title: String, val area: String, val note: String, val category: String)
private val habitPresets = listOf(
    HabitPreset("Wake up at 05:00", "Mind", "Build a consistent morning routine", "Routine"),
    HabitPreset("Gym / Workout", "Body", "Move your body and build strength", "Health"),
    HabitPreset("Reading / Learning", "Mind", "Spend time learning something new", "Growth"),
    HabitPreset("Budget tracking", "Career", "Review spending and stay intentional", "Life"),
    HabitPreset("Project work", "Career", "Make progress on an important project", "Work"),
    HabitPreset("No alcohol", "Body", "A private daily commitment", "Health"),
    HabitPreset("Social media detox", "Mind", "Be intentional with screen time", "Focus"),
    HabitPreset("Goal journaling", "Soul", "Reflect and write down your goals", "Growth"),
    HabitPreset("Cold shower", "Body", "A small daily challenge", "Health"),
    HabitPreset("Avoid pornography", "Mind", "A private personal commitment", "Focus")
)
private val weekdayOptions = listOf(1 to "M", 2 to "T", 3 to "W", 4 to "T", 5 to "F", 6 to "S", 7 to "S")

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun HabitEditorSheet(
    initialTitle: String,
    initialNote: String,
    initialArea: String,
    initialTargetValue: Double,
    initialTargetUnit: String,
    initialWeekdays: List<Int>,
    isEditing: Boolean,
    areas: List<String>,
    onDismiss: () -> Unit,
    onSave: (String, String, String, Double, String, List<Int>) -> Unit
) {
    var title by remember(initialTitle, isEditing) { mutableStateOf(initialTitle) }
    var note by remember(initialNote, isEditing) { mutableStateOf(initialNote) }
    var area by remember(initialArea, isEditing) { mutableStateOf(initialArea) }
    var target by remember(initialTargetValue, isEditing) { mutableStateOf(initialTargetValue.toString().removeSuffix(".0")) }
    var unit by remember(initialTargetUnit, isEditing) { mutableStateOf(initialTargetUnit) }
    var weekdays by remember(initialWeekdays, isEditing) { mutableStateOf(initialWeekdays.toSet()) }
    var search by remember { mutableStateOf("") }
    var showPresets by remember { mutableStateOf(!isEditing && initialTitle.isBlank()) }
    val filtered = habitPresets.filter { it.title.contains(search, true) || it.category.contains(search, true) }
    val validTarget = target.toDoubleOrNull()?.takeIf { it > 0.0 }

    ModalBottomSheet(onDismissRequest = onDismiss, containerColor = MaterialTheme.colorScheme.surface) {
        LazyColumn(
            Modifier.fillMaxWidth().padding(horizontal = 22.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp),
            contentPadding = PaddingValues(bottom = 28.dp)
        ) {
            item {
                Text(if (isEditing) "EDIT HABIT" else "BUILD A HABIT", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
                Text(if (isEditing) "Make it yours." else "Small steps. Better days.", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.SemiBold)
                Text("Choose a starting point, then shape it around your life.", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            if (!isEditing) {
                item {
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("START WITH A TEMPLATE", style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.SemiBold)
                        TextButton(onClick = { showPresets = !showPresets }) { Text(if (showPresets) "Hide" else "Browse") }
                    }
                    OutlinedTextField(search, { search = it }, Modifier.fillMaxWidth(), singleLine = true, placeholder = { Text("Search ideas") }, shape = RoundedCornerShape(14.dp))
                }
                if (showPresets) items(filtered) { preset ->
                    LumenCard(
                        variant = if (title == preset.title) LumenCardVariant.Accent else LumenCardVariant.Muted,
                        padding = com.santi020k.lumen.LumenSurfacePadding.Md,
                        onClick = { title = preset.title; note = preset.note; area = preset.area; showPresets = false }
                    ) {
                        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Column(Modifier.weight(1f)) {
                                Text(preset.title, fontWeight = FontWeight.SemiBold)
                                Text(preset.note, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                            Text(preset.area, style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.primary)
                        }
                    }
                }
            }
            item {
                Text("YOUR HABIT", style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.SemiBold)
                OutlinedTextField(title, { title = it }, Modifier.fillMaxWidth(), label = { Text("Habit name") }, placeholder = { Text("e.g. Walk for 20 minutes") }, singleLine = true, shape = RoundedCornerShape(14.dp))
                OutlinedTextField(note, { note = it }, Modifier.fillMaxWidth(), label = { Text("A note (optional)") }, placeholder = { Text("What does this look like for you?") }, minLines = 2, maxLines = 3, shape = RoundedCornerShape(14.dp))
            }
            item {
                Text("MEASURABLE TARGET", style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.SemiBold)
                Text("Use 1 and leave the unit blank for a simple yes/no habit.", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    OutlinedTextField(
                        value = target, onValueChange = { target = it.filter { c -> c.isDigit() || c == '.' } },
                        modifier = Modifier.weight(1f), label = { Text("Target") }, singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal), shape = RoundedCornerShape(14.dp)
                    )
                    OutlinedTextField(
                        value = unit, onValueChange = { unit = it }, modifier = Modifier.weight(1f),
                        label = { Text("Unit (optional)") }, placeholder = { Text("minutes, pages…") },
                        singleLine = true, shape = RoundedCornerShape(14.dp)
                    )
                }
                if (target.isNotBlank() && validTarget == null) Text("Enter a target greater than zero.", color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
            }
            item {
                Text("REPEAT ON", style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.SemiBold)
                Text("Choose the days this habit is due.", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    weekdayOptions.forEach { (day, label) ->
                        FilterChip(selected = day in weekdays, onClick = {
                            weekdays = if (day in weekdays) weekdays - day else weekdays + day
                        }, label = { Text(label) })
                    }
                }
                if (weekdays.isEmpty()) Text("Choose at least one day.", color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
            }
            item {
                Text("LIFE AREA", style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.SemiBold)
                Text("Where does this habit support you?", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    areas.chunked(3).forEach { group ->
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            group.forEach { choice -> FilterChip(selected = area == choice, onClick = { area = choice }, label = { Text(choice) }) }
                        }
                    }
                }
            }
            item {
                Text("You can adjust your habit any time.", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                LumenButton(
                    onClick = { onSave(title.trim(), note.trim(), area, validTarget ?: 1.0, unit.trim(), weekdays.sorted()) },
                    intent = LumenButtonIntent.Primary,
                    enabled = title.isNotBlank() && validTarget != null && weekdays.isNotEmpty(),
                    modifier = Modifier.fillMaxWidth().height(52.dp)
                ) { Text(if (isEditing) "Save changes" else "Create habit") }
                TextButton(onClick = onDismiss, modifier = Modifier.fillMaxWidth()) { Text("Cancel") }
            }
        }
    }
}
package com.lifeos.android.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.GridView
import androidx.compose.material.icons.outlined.Insights
import androidx.compose.material.icons.outlined.SelfImprovement
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.lifeos.android.ui.components.LifeAreaCard
import com.lifeos.android.ui.components.RhythmCard

private data class Habit(val id: Int, val title: String, val detail: String, val done: Boolean = false)

@Composable
fun LifeOSApp() {
    var selected by rememberSaveable { mutableIntStateOf(0) }
    var habits by rememberSaveable {
        mutableStateOf(
            listOf(
                Habit(1, "Morning movement", "20 minutes · Body"),
                Habit(2, "Read a few pages", "10 minutes · Mind"),
                Habit(3, "Quiet reflection", "5 minutes · Soul")
            )
        )
    }
    val completed = habits.count { it.done }

    Scaffold(
        bottomBar = {
            NavigationBar {
                val tabs = listOf("Today", "Areas", "Calendar", "Insights", "Coach")
                val icons = listOf(Icons.Outlined.CheckCircle, Icons.Outlined.GridView, Icons.Outlined.CalendarMonth, Icons.Outlined.Insights, Icons.Outlined.SelfImprovement)
                tabs.forEachIndexed { index, label ->
                    NavigationBarItem(
                        selected = selected == index,
                        onClick = { selected = index },
                        icon = { Icon(icons[index], contentDescription = label) },
                        label = { Text(label) }
                    )
                }
            }
        }
    ) { insets ->
        if (selected == 0) {
            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(insets).padding(horizontal = 22.dp),
                verticalArrangement = Arrangement.spacedBy(18.dp)
            ) {
                item {
                    Spacer(Modifier.height(18.dp))
                    Text("MONDAY, YOUR WAY", style = androidx.compose.material3.MaterialTheme.typography.labelMedium)
                    Spacer(Modifier.height(8.dp))
                    Text("Make today count.", style = androidx.compose.material3.MaterialTheme.typography.headlineLarge, fontWeight = FontWeight.SemiBold)
                    Spacer(Modifier.height(18.dp))
                    RhythmCard(completed = completed, total = habits.size)
                }
                item {
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                        Text("Your rhythm", style = androidx.compose.material3.MaterialTheme.typography.titleLarge, fontWeight = FontWeight.SemiBold)
                        Text("$completed of ${habits.size}", style = androidx.compose.material3.MaterialTheme.typography.labelLarge)
                    }
                }
                items(habits, key = { it.id }) { habit ->
                    LifeAreaCard {
                        Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                            Column(Modifier.weight(1f)) {
                                Text(habit.title, fontWeight = FontWeight.Medium)
                                Text(habit.detail, style = androidx.compose.material3.MaterialTheme.typography.bodySmall)
                            }
                            Checkbox(
                                checked = habit.done,
                                onCheckedChange = { checked ->
                                    habits = habits.map { if (it.id == habit.id) it.copy(done = checked) else it }
                                }
                            )
                        }
                    }
                }
                item {
                    Spacer(Modifier.height(4.dp))
                    Text("A little progress, repeated.", style = androidx.compose.material3.MaterialTheme.typography.bodyMedium)
                    Spacer(Modifier.height(24.dp))
                }
            }
        } else {
            Column(Modifier.fillMaxSize().padding(insets).padding(24.dp), verticalArrangement = Arrangement.Center) {
                Text(listOf("Today", "Life areas", "Calendar", "Insights", "AI Coach")[selected], style = androidx.compose.material3.MaterialTheme.typography.headlineMedium)
                Spacer(Modifier.height(8.dp))
                Text("This native experience is being built next.")
            }
        }
    }
}

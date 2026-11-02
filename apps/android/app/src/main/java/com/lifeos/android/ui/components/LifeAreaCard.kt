package com.lifeos.android.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun LifeAreaCard(content: @Composable () -> Unit) {
    Card(
        modifier = Modifier,
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(1.dp, Color(0xFFE9EDF0))
    ) {
        Column(Modifier.padding(horizontal = 16.dp, vertical = 12.dp)) { content() }
    }
}

@Composable
fun RhythmCard(completed: Int, total: Int) {
    Card(
        shape = RoundedCornerShape(26.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFEAF5F6))
    ) {
        Column(Modifier.padding(20.dp)) {
            androidx.compose.material3.Text("DAILY RHYTHM", style = androidx.compose.material3.MaterialTheme.typography.labelMedium)
            androidx.compose.material3.Text("$completed / $total", style = androidx.compose.material3.MaterialTheme.typography.headlineLarge)
            androidx.compose.material3.LinearProgressIndicator(
                progress = { if (total == 0) 0f else completed.toFloat() / total },
                modifier = Modifier.padding(top = 12.dp)
            )
        }
    }
}

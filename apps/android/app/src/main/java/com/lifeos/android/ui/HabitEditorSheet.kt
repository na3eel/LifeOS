package com.lifeos.android.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.santi020k.lumen.LumenButton
import com.santi020k.lumen.LumenButtonIntent

private data class HabitPreset(val title:String,val area:String,val note:String,val category:String)
private val habitPresets=listOf(
 HabitPreset("Wake up at 05:00","Mind","Build a consistent morning routine","Routine"),
 HabitPreset("Gym / Workout","Body","Move your body and build strength","Health"),
 HabitPreset("Reading / Learning","Mind","Spend time learning something new","Growth"),
 HabitPreset("Budget tracking","Career","Review spending and stay intentional","Life"),
 HabitPreset("Project work","Career","Make progress on an important project","Work"),
 HabitPreset("No alcohol","Body","A private daily commitment","Health"),
 HabitPreset("Social media detox","Mind","Be intentional with screen time","Focus"),
 HabitPreset("Goal journaling","Soul","Reflect and write down your goals","Growth"),
 HabitPreset("Cold shower","Body","A small daily challenge","Health"),
 HabitPreset("Avoid pornography","Mind","A private personal commitment","Focus")
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun HabitEditorSheet(
 initialTitle:String, initialNote:String, initialArea:String, isEditing:Boolean, areas:List<String>,
 onDismiss:()->Unit, onSave:(String,String,String)->Unit
){
 var title by remember(initialTitle,isEditing){mutableStateOf(initialTitle)}
 var note by remember(initialNote,isEditing){mutableStateOf(initialNote)}
 var area by remember(initialArea,isEditing){mutableStateOf(initialArea)}
 var search by remember{mutableStateOf("")}
 var showPresets by remember{mutableStateOf(!isEditing && initialTitle.isBlank())}
 val filtered=habitPresets.filter{it.title.contains(search,true)||it.category.contains(search,true)}
 ModalBottomSheet(onDismissRequest=onDismiss,containerColor=MaterialTheme.colorScheme.surface) {
  LazyColumn(Modifier.fillMaxWidth().padding(horizontal=22.dp),verticalArrangement=Arrangement.spacedBy(14.dp),contentPadding=PaddingValues(bottom=28.dp)){
   item {
    Text(if(isEditing)"EDIT HABIT" else "BUILD A HABIT",style=MaterialTheme.typography.labelMedium,color=MaterialTheme.colorScheme.primary,fontWeight=FontWeight.Bold)
    Text(if(isEditing)"Make it yours." else "Small steps. Better days.",style=MaterialTheme.typography.headlineMedium,fontWeight=FontWeight.SemiBold)
    Text("Choose a starting point, then shape it around your life.",style=MaterialTheme.typography.bodyMedium,color=MaterialTheme.colorScheme.onSurfaceVariant)
   }
   if(!isEditing){
    item {
     Row(Modifier.fillMaxWidth(),horizontalArrangement=Arrangement.SpaceBetween){
      Text("START WITH A TEMPLATE",style=MaterialTheme.typography.labelLarge,fontWeight=FontWeight.SemiBold)
      TextButton(onClick={showPresets=!showPresets}){Text(if(showPresets)"Hide" else "Browse")}
     }
     OutlinedTextField(search,{search=it},Modifier.fillMaxWidth(),singleLine=true,placeholder={Text("Search ideas")},shape=RoundedCornerShape(14.dp))
    }
    if(showPresets) items(filtered){preset->
     Surface(onClick={title=preset.title;note=preset.note;area=preset.area;showPresets=false},shape=RoundedCornerShape(16.dp),color=if(title==preset.title)MaterialTheme.colorScheme.secondaryContainer else MaterialTheme.colorScheme.surfaceVariant.copy(alpha=.45f),border=if(title==preset.title) androidx.compose.foundation.BorderStroke(1.dp,MaterialTheme.colorScheme.primary) else null){
      Row(Modifier.fillMaxWidth().padding(14.dp),horizontalArrangement=Arrangement.SpaceBetween){
       Column(Modifier.weight(1f)){Text(preset.title,fontWeight=FontWeight.SemiBold);Text(preset.note,style=MaterialTheme.typography.bodySmall,color=MaterialTheme.colorScheme.onSurfaceVariant)}
       Text(preset.area,style=MaterialTheme.typography.labelMedium,color=MaterialTheme.colorScheme.primary)
      }
     }
    }
   }
   item {
    Text("YOUR HABIT",style=MaterialTheme.typography.labelLarge,fontWeight=FontWeight.SemiBold)
    OutlinedTextField(title,{title=it},Modifier.fillMaxWidth(),label={Text("Habit name")},placeholder={Text("e.g. Walk for 20 minutes")},singleLine=true,shape=RoundedCornerShape(14.dp))
    OutlinedTextField(note,{note=it},Modifier.fillMaxWidth(),label={Text("A note (optional)")},placeholder={Text("What does this look like for you?")},minLines=2,maxLines=3,shape=RoundedCornerShape(14.dp))
   }
   item {
    Text("LIFE AREA",style=MaterialTheme.typography.labelLarge,fontWeight=FontWeight.SemiBold)
    Text("Where does this habit support you?",style=MaterialTheme.typography.bodySmall,color=MaterialTheme.colorScheme.onSurfaceVariant)
    Column(verticalArrangement=Arrangement.spacedBy(8.dp)){
     areas.chunked(3).forEach{group->Row(horizontalArrangement=Arrangement.spacedBy(8.dp)){group.forEach{choice->FilterChip(selected=area==choice,onClick={area=choice},label={Text(choice)})}}}
    }
   }
   item {
    Text("You can adjust your habit any time.",style=MaterialTheme.typography.bodySmall,color=MaterialTheme.colorScheme.onSurfaceVariant)
    LumenButton(onClick={onSave(title.trim(),note.trim(),area)},intent=LumenButtonIntent.Primary,enabled=title.isNotBlank(),modifier=Modifier.fillMaxWidth().height(52.dp)){Text(if(isEditing)"Save changes" else "Create habit")}
    TextButton(onClick=onDismiss,modifier=Modifier.fillMaxWidth()){Text("Cancel")}
   }
  }
 }
}

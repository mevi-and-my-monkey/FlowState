package com.mevi.flowstate.ui.timeevent

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.ui.window.Dialog

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimeEventScreen(viewModel: TimeEventViewModel) {
    val timeEvents by viewModel.timeEvents.collectAsState()
    var showDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = { TopAppBar(title = { Text("Contadores") }) },
        floatingActionButton = {
            FloatingActionButton(onClick = { showDialog = true }) {
                Icon(Icons.Default.Add, contentDescription = "Add Time Event")
            }
        }
    ) {
        if (timeEvents.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize().padding(it), contentAlignment = Alignment.Center) {
                Text("No hay recuerdos aún")
            }
        } else {
            LazyColumn(modifier = Modifier.fillMaxSize().padding(it)) {
                items(timeEvents) { timeEvent ->
                    TimeEventCard(timeEvent, viewModel::getFormattedDate)
                }
            }
        }

        if (showDialog) {
            AddEventDialog(
                onDismiss = { showDialog = false },
                onConfirm = {
                    // TODO: Implement date validation and formatting
                    viewModel.createTimeEvent(it.title, it.targetDate, it.type.name)
                    showDialog = false
                }
            )
        }
    }
}

@Composable
fun TimeEventCard(timeEvent: com.mevi.flowstate.db.TimeEvent, getFormattedDate: (com.mevi.flowstate.db.TimeEvent) -> String) {
    val cardColors = if (timeEvent.type == "UNTIL") {
        CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
    } else {
        CardDefaults.cardColors(containerColor = Color(0xFFC8E6C9)) // Desaturated green
    }

    Card(
        modifier = Modifier.fillMaxWidth().padding(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = cardColors
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(timeEvent.title, style = MaterialTheme.typography.titleLarge)
            Text(getFormattedDate(timeEvent))
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEventDialog(onDismiss: () -> Unit, onConfirm: (NewEvent) -> Unit) {
    var title by remember { mutableStateOf("") }
    var day by remember { mutableStateOf("") }
    var month by remember { mutableStateOf("") }
    var year by remember { mutableStateOf("") }
    var type by remember { mutableStateOf(EventType.UNTIL) }

    Dialog(onDismissRequest = onDismiss) {
        Card {
            Column(modifier = Modifier.padding(16.dp)) {
                OutlinedTextField(value = title, onValueChange = { title = it }, label = { Text("Title") })
                Row {
                    OutlinedTextField(value = day, onValueChange = { day = it }, label = { Text("DD") }, modifier = Modifier.weight(1f))
                    Spacer(modifier = Modifier.width(8.dp))
                    OutlinedTextField(value = month, onValueChange = { month = it }, label = { Text("MM") }, modifier = Modifier.weight(1f))
                    Spacer(modifier = Modifier.width(8.dp))
                    OutlinedTextField(value = year, onValueChange = { year = it }, label = { Text("YYYY") }, modifier = Modifier.weight(1.5f))
                }
                Row {
                    Button(onClick = { type = EventType.SINCE }) {
                        Text("Since")
                    }
                    Button(onClick = { type = EventType.UNTIL }) {
                        Text("Until")
                    }
                }
                Button(onClick = {
                    val date = "$year-$month-$day T00:00:00.000Z"
                    onConfirm(NewEvent(title, date, type))
                }) {
                    Text("Add")
                }
            }
        }
    }
}

enum class EventType { SINCE, UNTIL }
data class NewEvent(val title: String, val targetDate: String, val type: EventType)

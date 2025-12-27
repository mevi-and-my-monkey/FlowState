package com.mevi.flowstate.ui.notes


import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.mevi.flowstate.db.Note
import org.koin.core.annotation.KoinExperimentalAPI
import org.koin.compose.koinInject


@OptIn(KoinExperimentalAPI::class)
@Composable
fun NoteScreen(viewModel: NoteViewModel = koinInject()) {
    val notes by viewModel.notes.collectAsState()
    var title by remember { mutableStateOf("") }
    var content by remember { mutableStateOf("") }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("My Notes", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = title,
            onValueChange = { title = it },
            label = { Text("Title") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = content,
            onValueChange = { content = it },
            label = { Text("Content") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        Button(onClick = {
            if (title.isNotBlank() && content.isNotBlank()) {
                viewModel.addNote(title, content)
                title = ""
                content = ""
            }
        }) {
            Text("Add Note")
        }

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(modifier = Modifier.weight(1f)) {
            items(notes) { note ->
                NoteItem(note, onDelete = { viewModel.deleteNote(note.id) })
                HorizontalDivider()
            }
        }
    }
}

@Composable
fun NoteItem(note: Note, onDelete: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(note.title, style = MaterialTheme.typography.titleMedium)
            Text(note.content, style = MaterialTheme.typography.bodyMedium)
        }
        IconButton(onClick = onDelete) {
            Icon(Icons.Default.Delete, contentDescription = "Delete note")
        }
    }
}
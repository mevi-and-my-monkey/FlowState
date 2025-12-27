package com.mevi.flowstate.ui.notes

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import com.mevi.flowstate.core.TimeProvider
import com.mevi.flowstate.db.AppDatabase
import com.mevi.flowstate.db.Note
import com.mevi.flowstate.ui.ViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class NoteViewModel(
    private val appDatabase: AppDatabase
) : ViewModel() {

    val notes: StateFlow<List<Note>> =
        appDatabase.noteQueries.selectAll()
            .asFlow()
            .mapToList(Dispatchers.Default)
            .stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(5_000),
                emptyList()
            )

    fun addNote(title: String, content: String) {
        viewModelScope.launch {
            appDatabase.noteQueries.insert(
                title,
                content,
                TimeProvider.nowMillis()
            )
        }
    }

    fun deleteNote(id: Long) {
        viewModelScope.launch {
            appDatabase.noteQueries.delete(id)
        }
    }
}
package com.mevi.flowstate.ui.timeevent

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import com.mevi.flowstate.db.AppDatabase
import com.mevi.flowstate.db.TimeEvent
import com.mevi.flowstate.ui.ViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class TimeEventViewModel(private val appDatabase: AppDatabase) : ViewModel() {

    val timeEvents: StateFlow<List<TimeEvent>> = appDatabase.timeEventQueries.selectAll()
        .asFlow()
        .mapToList(Dispatchers.Default)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun addTimeEvent(name: String, date: Long, isCountdown: Boolean) {
        viewModelScope.launch {
            appDatabase.timeEventQueries.insert(name, date, if (isCountdown) 1L else 0L)
        }
    }

    fun deleteTimeEvent(id: Long) {
        viewModelScope.launch {
            appDatabase.timeEventQueries.delete(id)
        }
    }
}
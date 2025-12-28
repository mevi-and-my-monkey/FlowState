package com.mevi.flowstate.ui.timeevent

import com.mevi.flowstate.core.TimeProvider
import com.mevi.flowstate.db.AppDatabase
import com.mevi.flowstate.db.TimeEvent
import com.mevi.flowstate.ui.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlin.time.Instant

class TimeEventViewModel(private val database: AppDatabase) : ViewModel() {

    private val _timeEvents = MutableStateFlow<List<TimeEvent>>(emptyList())
    val timeEvents = _timeEvents.asStateFlow()

    init {
        loadTimeEvents()
    }

    fun loadTimeEvents() {
        viewModelScope.launch {
            _timeEvents.value = database.timeEventQueries.selectAll().executeAsList()
        }
    }

    fun createTimeEvent(title: String, targetDate: String, type: String) {
        viewModelScope.launch {
            database.timeEventQueries.insert(title, targetDate, type)
            loadTimeEvents()
        }
    }

    fun deleteTimeEvent(id: Long) {
        viewModelScope.launch {
            database.timeEventQueries.delete(id)
            loadTimeEvents()
        }
    }

    fun getFormattedDate(timeEvent: TimeEvent): String {
        val fixedDate = timeEvent.targetDate.replace(" ", "")
        val targetInstant = Instant.parse(fixedDate)
        val now = TimeProvider.now()

        val difference = if (timeEvent.type == "UNTIL") {
            targetInstant - now
        } else {
            now - targetInstant
        }

        val days = difference.inWholeDays

        return if (timeEvent.type == "UNTIL") {
            if (days >= 0) "Faltan $days días" else "-"
        } else {
            "Han pasado $days días"
        }
    }
}

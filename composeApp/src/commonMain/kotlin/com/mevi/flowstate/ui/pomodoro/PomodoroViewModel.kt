package com.mevi.flowstate.ui.pomodoro

import com.mevi.flowstate.ui.ViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

private const val WORK_DURATION_MINUTES = 25L
private const val BREAK_DURATION_MINUTES = 5L

enum class PomodoroState {
    WORK, BREAK, PAUSED
}

data class PomodoroUiState(
    val time: String = "25:00",
    val progress: Float = 1.0f,
    val state: PomodoroState = PomodoroState.PAUSED,
    val isRunning: Boolean = false
)

class PomodoroViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(PomodoroUiState())
    val uiState = _uiState.asStateFlow()

    private var timerJob: Job? = null
    private var totalTimeSeconds = WORK_DURATION_MINUTES * 60
    private var remainingSeconds = totalTimeSeconds

    fun start() {
        if (_uiState.value.isRunning) return

        _uiState.update {
            it.copy(
                isRunning = true,
                state = if (it.state == PomodoroState.PAUSED) PomodoroState.WORK else it.state
            )
        }
        timerJob = viewModelScope.launch {
            while (remainingSeconds > 0) {
                delay(1000)
                remainingSeconds--
                updateUi()
            }
            // When timer finishes, switch state
            if (_uiState.value.state == PomodoroState.WORK) {
                switchToBreak()
            } else {
                switchToWork()
            }
            start() // Start the next cycle automatically
        }
    }

    fun pause() {
        _uiState.update { it.copy(isRunning = false, state = PomodoroState.PAUSED) }
        timerJob?.cancel()
    }

    fun reset() {
        pause()
        switchToWork()
    }

    private fun switchToWork() {
        totalTimeSeconds = WORK_DURATION_MINUTES * 60
        remainingSeconds = totalTimeSeconds
        _uiState.update { it.copy(state = PomodoroState.WORK) }
        updateUi()
    }

    private fun switchToBreak() {
        totalTimeSeconds = BREAK_DURATION_MINUTES * 60
        remainingSeconds = totalTimeSeconds
        _uiState.update { it.copy(state = PomodoroState.BREAK) }
        updateUi()
    }

    private fun updateUi() {
        val minutes = remainingSeconds / 60
        val seconds = remainingSeconds % 60
        _uiState.update {
            it.copy(
                time = "${minutes.toString().padStart(2, '0')}:${seconds.toString().padStart(2, '0')}",
                progress = remainingSeconds.toFloat() / totalTimeSeconds.toFloat()
            )
        }
    }

    public override fun onCleared() {
        super.onCleared()
        timerJob?.cancel()
    }
}
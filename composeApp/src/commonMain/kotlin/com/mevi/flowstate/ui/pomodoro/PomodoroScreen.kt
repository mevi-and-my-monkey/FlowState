package com.mevi.flowstate.ui.pomodoro

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.koin.compose.koinInject

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PomodoroScreen(viewModel: PomodoroViewModel = koinInject()) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = { TopAppBar(title = { Text("Pomodoro") }) }
    ) {
        Column(
            modifier = Modifier.fillMaxSize().padding(it).padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(contentAlignment = Alignment.Center, modifier = Modifier.size(300.dp)) {
                CircularProgressIndicator(
                    progress = { uiState.progress },
                    modifier = Modifier.size(300.dp),
                    strokeWidth = 30.dp,
                    color = when (uiState.state) {
                        PomodoroState.WORK -> MaterialTheme.colorScheme.primary
                        PomodoroState.BREAK -> MaterialTheme.colorScheme.secondary
                        PomodoroState.PAUSED -> MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)
                    },
                    strokeCap = StrokeCap.Round
                )
                Text(uiState.time, style = MaterialTheme.typography.displayLarge.copy(fontSize = 72.sp))
            }

            Spacer(modifier = Modifier.height(32.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(onClick = { viewModel.start() }, enabled = !uiState.isRunning) {
                    Text("Start")
                }
                Button(onClick = { viewModel.pause() }, enabled = uiState.isRunning) {
                    Text("Pause")
                }
                Button(onClick = { viewModel.reset() }) {
                    Text("Reset")
                }
            }
        }
    }
}
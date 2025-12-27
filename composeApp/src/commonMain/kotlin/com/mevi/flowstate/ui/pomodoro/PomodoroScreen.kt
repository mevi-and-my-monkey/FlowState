package com.mevi.flowstate.ui.pomodoro

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.koin.compose.koinInject
import org.koin.core.annotation.KoinExperimentalAPI

@OptIn(KoinExperimentalAPI::class)
@Composable
fun PomodoroScreen(viewModel: PomodoroViewModel = koinInject()) {
    val uiState by viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Pomodoro Timer", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(32.dp))

        Box(contentAlignment = Alignment.Center, modifier = Modifier.size(250.dp)) {
            CircularProgressIndicator(
                progress = { uiState.progress },
                modifier = Modifier.size(250.dp),
                strokeWidth = 25.dp,
                color = when (uiState.state) {
                    PomodoroState.WORK -> MaterialTheme.colorScheme.primary
                    PomodoroState.BREAK -> MaterialTheme.colorScheme.secondary
                    PomodoroState.PAUSED -> MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)
                },
                strokeCap = StrokeCap.Round
            )
            Text(uiState.time, fontSize = 50.sp, fontWeight = FontWeight.Bold)
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
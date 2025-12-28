package com.mevi.flowstate

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Timer
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import com.mevi.flowstate.ui.notes.NoteScreen
import com.mevi.flowstate.ui.pomodoro.PomodoroScreen
import com.mevi.flowstate.ui.theme.FlowStateTheme
import com.mevi.flowstate.ui.timeevent.TimeEventScreen
import org.koin.compose.KoinContext
import org.koin.compose.koinInject

data class NavItem(val screen: Screen, val icon: ImageVector)

sealed class Screen(val title: String) {
    object Notes : Screen("Notes")
    object Pomodoro : Screen("Pomodoro")
    object TimeTracker : Screen("TimeTracker")
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun App() {
    FlowStateTheme {
        val navItems = listOf(
            NavItem(Screen.Notes, Icons.Default.Create),
            NavItem(Screen.Pomodoro, Icons.Default.Timer),
            NavItem(Screen.TimeTracker, Icons.Default.DateRange)
        )
        var currentScreen: Screen by remember { mutableStateOf(Screen.Notes) }

        KoinContext {
            Scaffold(
                bottomBar = {
                    NavigationBar {
                        navItems.forEach { item ->
                            NavigationBarItem(
                                icon = { Icon(item.icon, contentDescription = item.screen.title) },
                                label = { Text(item.screen.title) },
                                selected = currentScreen == item.screen,
                                onClick = { currentScreen = item.screen }
                            )
                        }
                    }
                }
            ) { innerPadding ->
                Surface(modifier = Modifier.padding(innerPadding)) {
                    when (currentScreen) {
                        is Screen.Notes -> NoteScreen()
                        is Screen.Pomodoro -> PomodoroScreen()
                        is Screen.TimeTracker -> TimeEventScreen(koinInject())
                    }
                }
            }
        }
    }
}

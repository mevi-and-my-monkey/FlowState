package com.mevi.flowstate

import androidx.compose.ui.window.ComposeUIViewController

private var koinStarted = false

fun MainViewController() = ComposeUIViewController {
    if (!koinStarted) {
        initKoin()
        koinStarted = true
    }
    App()
}
package com.mevi.flowstate.ui

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel

open class ViewModel {

    protected val viewModelScope =
        CoroutineScope(SupervisorJob() + Dispatchers.Main)

    open fun onCleared() {
        viewModelScope.cancel()
    }
}
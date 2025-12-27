package com.mevi.flowstate.di

import com.mevi.flowstate.data.DatabaseDriverFactory
import com.mevi.flowstate.db.AppDatabase
import com.mevi.flowstate.ui.notes.NoteViewModel
import com.mevi.flowstate.ui.pomodoro.PomodoroViewModel
import com.mevi.flowstate.ui.timeevent.TimeEventViewModel
import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.dsl.module

fun initKoin(appModule: Module) {
    startKoin {
        modules(appModule, commonModule)
    }
}

val commonModule = module {
    single { AppDatabase(get<DatabaseDriverFactory>().createDriver()) }
    factory { NoteViewModel(get()) }
    factory { TimeEventViewModel(get()) }
    factory { PomodoroViewModel() }
}

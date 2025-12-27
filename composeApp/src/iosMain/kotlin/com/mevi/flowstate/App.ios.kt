package com.mevi.flowstate

import com.mevi.flowstate.data.DatabaseDriverFactory
import com.mevi.flowstate.di.initKoin
import org.koin.dsl.module

fun initKoin() {
    initKoin(module {
        single<DatabaseDriverFactory> { DatabaseDriverFactory() }
    })
}

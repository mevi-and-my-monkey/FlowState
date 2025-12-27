package com.mevi.flowstate

import android.app.Application
import com.mevi.flowstate.data.DatabaseDriverFactory
import com.mevi.flowstate.di.initKoin
import org.koin.dsl.module

class AndroidApp : Application() {
    override fun onCreate() {
        super.onCreate()
        initKoin(module {
            single<DatabaseDriverFactory> { DatabaseDriverFactory(applicationContext) }
        })
    }
}

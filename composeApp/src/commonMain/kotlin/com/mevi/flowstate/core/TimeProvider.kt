package com.mevi.flowstate.core

import kotlin.time.Clock

object TimeProvider {
    private val clock = Clock.System

    fun nowMillis(): Long =
        clock.now().toEpochMilliseconds()
}
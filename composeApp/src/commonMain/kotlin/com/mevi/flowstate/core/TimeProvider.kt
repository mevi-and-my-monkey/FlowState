package com.mevi.flowstate.core

import kotlin.time.Clock
import kotlin.time.Instant

object TimeProvider {
    private val clock = Clock.System

    fun nowMillis(): Long =
        clock.now().toEpochMilliseconds()

    fun now(): Instant = Clock.System.now()
}
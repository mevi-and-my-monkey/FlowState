package com.mevi.flowstate

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform
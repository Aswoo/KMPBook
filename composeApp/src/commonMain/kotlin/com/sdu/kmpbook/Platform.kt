package com.sdu.kmpbook

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform
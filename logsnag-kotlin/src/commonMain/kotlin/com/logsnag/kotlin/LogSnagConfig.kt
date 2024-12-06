package com.logsnag.kotlin

import io.ktor.client.engine.HttpClientEngine

internal class LogSnagConfig(
    val token: String,
    val engine: HttpClientEngine? = null,
)

package com.logsnag.kotlin.api

internal class LogSnagException(
    val status: Int,
    val body: String,
) : Exception("[LogSnag] Failed to publish: ${status}\n$body\nPlease check our docs at https://docs.logsnag.com")

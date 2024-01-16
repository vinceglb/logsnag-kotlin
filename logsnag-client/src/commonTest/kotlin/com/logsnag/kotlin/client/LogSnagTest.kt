package com.logsnag.kotlin.client

import com.logsnag.LogSnagSecrets
import kotlinx.coroutines.runBlocking
import kotlin.test.Test
import kotlin.test.assertTrue

class LogSnagTest {
    private val logSnag = LogSnag(
        token = LogSnagSecrets.LogSnagToken ?: throw Exception("LogSnagToken is null"),
        project = LogSnagSecrets.LogSnagProject ?: throw Exception("LogSnagProject is null")
    )

    @Test
    fun sendTrackTest(): Unit = runBlocking {
        val res = logSnag.track(
            channel = "test",
            event = "Unit test event"
        )
        assertTrue { res }
    }

    @Test
    fun sendIdentifyTest(): Unit = runBlocking {
        val res = logSnag.identify(
            userId = "test",
            properties = mapOf(
                "testKey" to "testValue",
                "test key" to "testValue2"
            )
        )
        assertTrue { res }
    }

    @Test
    fun sendInsightTrackTest(): Unit = runBlocking {
        val res = logSnag.insightTrack(
            title = "Unit test insight",
            value = "test",
        )
        assertTrue { res }
    }

    @Test
    fun sendInsightIncrementTest(): Unit = runBlocking {
        val res = logSnag.insightIncrement(
            title = "Unit test increment",
            value = 1,
        )
        assertTrue { res }
    }
}

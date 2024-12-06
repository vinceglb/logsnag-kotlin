package com.logsnag.kotlin.api

import com.logsnag.LogSnagSecrets
import com.logsnag.kotlin.LogSnagConfig
import com.logsnag.kotlin.createHttpClient
import com.logsnag.kotlin.types.TrackOptions
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

internal val token: String
    get() = requireNotNull(LogSnagSecrets.LogSnagToken) { "LogSnagToken is null" }

internal val project: String
    get() = requireNotNull(LogSnagSecrets.LogSnagProject) { "LogSnagProject is null" }

class LogSnagApiTest {
    @Test
    fun trackTest() = runTest {
        val engine = MockEngine { request ->
            assertEquals("/v1/log", request.url.encodedPath)
            assertEquals("Bearer $token", request.headers["Authorization"])
            assertEquals(HttpMethod.Post, request.method)
            respond("{}", HttpStatusCode.OK)
        }

        val config = LogSnagConfig(token, engine)
        val client = createHttpClient(config)
        val api = LogSnagApi(client)

        val res = api.track(
            trackOptions = TrackOptions(
                project = project,
                channel = "test",
                event = "Unit test event"
            )
        )

        assertTrue { res.isSuccess }
    }
}
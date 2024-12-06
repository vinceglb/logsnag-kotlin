package com.logsnag.kotlin.api

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

class SendEventTasksDataSourceTest {
    @Test
    fun sendTrackTest() = runTest {
        val engine = MockEngine { request ->
            assertEquals("/v1/log", request.url.encodedPath)
            assertEquals("Bearer $token", request.headers["Authorization"])
            assertEquals(HttpMethod.Post, request.method)
            respond("{}", HttpStatusCode.OK)
        }

        val config = LogSnagConfig(token, engine)
        val client = createHttpClient(config)
        val api = LogSnagApi(client)
        val dataSource = SendEventTasksDataSource(api)

        val trackOptions = TrackOptions(
            project = project,
            userId = "test",
            channel = "test",
            event = "Unit test event",
        )

        dataSource.track(trackOptions)
    }
}

package com.logsnag.kotlin.api

import com.logsnag.kotlin.ENDPOINTS
import com.logsnag.kotlin.client.LogSnagException
import com.logsnag.kotlin.types.IdentifyOptions
import com.logsnag.kotlin.types.InsightIncrementOptions
import com.logsnag.kotlin.types.InsightTrackOptions
import com.logsnag.kotlin.types.TrackOptions
import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.bearerAuth
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.http.isSuccess
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json

/**
 * Call the LogSnag API
 */
internal class LogSnagApi(
    private val token: String,
) {
    @OptIn(ExperimentalSerializationApi::class)
    private val client = HttpClient {
        install(ContentNegotiation) {
            json(Json {
                explicitNulls = false
            })
        }
    }

    /**
     * Track an event
     */
    suspend fun track(trackOptions: TrackOptions): Result<Unit> = runSafely {
        // Send track request
        client.post(ENDPOINTS.LOG) {
            contentType(ContentType.Application.Json)
            bearerAuth(token)
            setBody(trackOptions)
        }
    }

    /**
     * Identify a user
     */
    suspend fun identify(identifyOptions: IdentifyOptions): Result<Unit> = runSafely {
        client.post(ENDPOINTS.IDENTIFY) {
            contentType(ContentType.Application.Json)
            bearerAuth(token)
            setBody(identifyOptions)
        }
    }

    /**
     * Track an insight
     */
    suspend fun insightTrack(insightTrackOptions: InsightTrackOptions): Result<Unit> = runSafely {
        client.post(ENDPOINTS.INSIGHT) {
            contentType(ContentType.Application.Json)
            bearerAuth(token)
            setBody(insightTrackOptions)
        }
    }

    /**
     * Increment an insight
     */
    suspend fun insightIncrement(insightIncrementOptions: InsightIncrementOptions): Result<Unit> = runSafely {
        client.post(ENDPOINTS.INSIGHT) {
            contentType(ContentType.Application.Json)
            bearerAuth(token)
            setBody(insightIncrementOptions)
        }
    }

    /**
     * Run a block of code that calls the LogSnag API
     * Handles errors and returns a Result
     * @param block code to run
     * @return Result
     */
    private suspend fun runSafely(block: suspend () -> HttpResponse): Result<Unit> {
        return try {
            // Send request
            val response = block()

            // Check response
            if (!response.status.isSuccess()) {
                throw LogSnagException(
                    status = response.status.value,
                    body = response.bodyAsText()
                )
            }

            // Return success
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}

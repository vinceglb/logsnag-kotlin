package com.logsnag.kotlin.api

import com.logsnag.kotlin.types.EventSuccess
import com.logsnag.kotlin.types.IdentifyOptions
import com.logsnag.kotlin.types.InsightIncrementOptions
import com.logsnag.kotlin.types.InsightTrackOptions
import com.logsnag.kotlin.types.TrackOptions
import io.ktor.client.HttpClient
import io.ktor.client.request.patch
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.http.isSuccess

/**
 * Call the LogSnag API
 */
internal class LogSnagApi(private val client: HttpClient) {
    /**
     * Track an event
     */
    suspend fun track(trackOptions: TrackOptions) = runSafely {
        client.post(ENDPOINTS.LOG) {
            contentType(ContentType.Application.Json)
            setBody(trackOptions)
        }
    }

    /**
     * Identify a user
     */
    suspend fun identify(identifyOptions: IdentifyOptions) = runSafely {
        client.post(ENDPOINTS.IDENTIFY) {
            contentType(ContentType.Application.Json)
            setBody(identifyOptions)
        }
    }

    /**
     * Track an insight
     */
    suspend fun insightTrack(insightTrackOptions: InsightTrackOptions): Result<EventSuccess> = runSafely {
        client.post(ENDPOINTS.INSIGHT) {
            contentType(ContentType.Application.Json)
            setBody(insightTrackOptions)
        }
    }

    /**
     * Increment an insight
     */
    suspend fun insightIncrement(insightIncrementOptions: InsightIncrementOptions): Result<EventSuccess> = runSafely {
        client.patch(ENDPOINTS.INSIGHT) {
            contentType(ContentType.Application.Json)
            setBody(insightIncrementOptions)
        }
    }

    /**
     * Run a block of code that calls the LogSnag API
     * Handles errors and returns a Result
     * @param block code to run
     * @return Result
     */
    private suspend fun runSafely(block: suspend () -> HttpResponse): Result<EventSuccess> {
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
            Result.success(EventSuccess(
                status = response.status.value,
                message = response.status.description,
            ))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}

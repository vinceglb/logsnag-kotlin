package com.logsnag.kotlin.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.logsnag.kotlin.api.LogSnagApi
import com.logsnag.kotlin.types.EventType
import com.logsnag.kotlin.types.IdentifyOptions
import com.logsnag.kotlin.types.InsightIncrementOptions
import com.logsnag.kotlin.types.InsightTrackOptions
import com.logsnag.kotlin.types.TrackOptions
import kotlinx.serialization.json.Json

internal class SendEventWorker(
    context: Context,
    params: WorkerParameters,
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        // Get token
        val token = inputData.getString(TOKEN_KEY)
            ?: return Result.failure()

        // Get event type
        val eventType = inputData
            .getString(EVENT_TYPE_KEY)
            ?.let(EventType::valueOf)
            ?: return Result.failure()

        // Get event payload
        val eventJson = inputData.getString(EVENT_PAYLOAD_KEY)
            ?: return Result.failure()

        // Create API
        val logSnagApi = LogSnagApi(token)

        // Send event
        val result = when (eventType) {
            EventType.TRACK -> {
                val options = Json.decodeFromString<TrackOptions>(eventJson)
                logSnagApi.track(options)
            }

            EventType.IDENTIFY -> {
                val options = Json.decodeFromString<IdentifyOptions>(eventJson)
                logSnagApi.identify(options)
            }

            EventType.INSIGHT -> {
                val options = Json.decodeFromString<InsightTrackOptions>(eventJson)
                logSnagApi.insightTrack(options)
            }

            EventType.INCREMENT -> {
                val options = Json.decodeFromString<InsightIncrementOptions>(eventJson)
                logSnagApi.insightIncrement(options)
            }
        }

        return if (result.isSuccess) {
            Result.success()
        } else {
            // TODO - Cancel worker if it fails too many times
            Result.retry()
        }
    }

    companion object {
        internal const val TOKEN_KEY = "api-key"
        internal const val EVENT_TYPE_KEY = "event-type"
        internal const val EVENT_PAYLOAD_KEY = "event-payload"
        internal const val WORKER_TAG = "logsnag-worker"
    }
}

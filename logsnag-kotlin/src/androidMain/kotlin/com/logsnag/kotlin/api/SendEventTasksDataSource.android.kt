package com.logsnag.kotlin.api

import androidx.work.Constraints
import androidx.work.Data
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import co.touchlab.kermit.Logger
import com.logsnag.kotlin.LogSnagConfig
import com.logsnag.kotlin.types.EventType
import com.logsnag.kotlin.types.IdentifyOptions
import com.logsnag.kotlin.types.InsightIncrementOptions
import com.logsnag.kotlin.types.InsightTrackOptions
import com.logsnag.kotlin.types.TrackOptions
import com.logsnag.kotlin.worker.SendEventWorker
import com.logsnag.kotlin.worker.SendEventWorker.Companion.EVENT_PAYLOAD_KEY
import com.logsnag.kotlin.worker.SendEventWorker.Companion.EVENT_TYPE_KEY
import com.logsnag.kotlin.worker.SendEventWorker.Companion.TOKEN_KEY
import com.logsnag.kotlin.worker.SendEventWorker.Companion.WORKER_TAG
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

internal actual class SendEventTasksDataSource(
    private val token: String,
    private val workManager: WorkManager = SendEventTasksDataSource.workManager,
) {
    actual suspend fun track(trackOptions: TrackOptions) {
        startWorkRequest(
            eventType = EventType.TRACK,
            eventPayload = Json.encodeToString(trackOptions),
            token = token,
        )
    }

    actual suspend fun identify(identifyOptions: IdentifyOptions) {
        startWorkRequest(
            eventType = EventType.IDENTIFY,
            eventPayload = Json.encodeToString(identifyOptions),
            token = token,
        )
    }

    actual suspend fun insightTrack(insightTrackOptions: InsightTrackOptions) {
        startWorkRequest(
            eventType = EventType.INSIGHT,
            eventPayload = Json.encodeToString(insightTrackOptions),
            token = token,
        )
    }

    actual suspend fun insightIncrement(insightIncrementOptions: InsightIncrementOptions) {
        startWorkRequest(
            eventType = EventType.INCREMENT,
            eventPayload = Json.encodeToString(insightIncrementOptions),
            token = token,
        )
    }

    private fun startWorkRequest(
        eventType: EventType,
        eventPayload: String,
        token: String,
    ) {
        val data: Data = workDataOf(
            EVENT_TYPE_KEY to eventType.name,
            EVENT_PAYLOAD_KEY to eventPayload,
            TOKEN_KEY to token,
        )

        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val request = OneTimeWorkRequestBuilder<SendEventWorker>()
            .setConstraints(constraints)
            .setInputData(data)
            .addTag(WORKER_TAG)
            .build()

        workManager.enqueue(request)
    }

    companion object {
        private lateinit var workManager: WorkManager

        fun init(workManager: WorkManager) {
            this.workManager = workManager
            Logger.d("SendEventTasksDataSource") { "Initialized" }
        }
    }

    actual object Factory {
        actual fun create(config: LogSnagConfig): SendEventTasksDataSource {
            return SendEventTasksDataSource(config.token)
        }
    }
}

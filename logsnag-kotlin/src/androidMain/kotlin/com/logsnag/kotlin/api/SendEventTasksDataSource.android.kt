package com.logsnag.kotlin.api

import androidx.work.Constraints
import androidx.work.Data
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import co.touchlab.kermit.Logger
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

internal actual class SendEventTasksDataSource private constructor(
    private val workManager: WorkManager,
) {
    actual fun track(trackOptions: TrackOptions, token: String) {
        startWorkRequest(
            eventType = EventType.TRACK,
            eventPayload = Json.encodeToString(trackOptions),
            token = token,
        )
    }

    actual fun identify(identifyOptions: IdentifyOptions, token: String) {
        startWorkRequest(
            eventType = EventType.IDENTIFY,
            eventPayload = Json.encodeToString(identifyOptions),
            token = token,
        )
    }

    actual fun insightTrack(insightTrackOptions: InsightTrackOptions, token: String) {
        startWorkRequest(
            eventType = EventType.INSIGHT,
            eventPayload = Json.encodeToString(insightTrackOptions),
            token = token,
        )
    }

    actual fun insightIncrement(insightIncrementOptions: InsightIncrementOptions, token: String) {
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

    actual companion object {
        private var sendEventTasksDataSource: SendEventTasksDataSource? = null

        fun init(workManager: WorkManager): SendEventTasksDataSource {
            sendEventTasksDataSource = SendEventTasksDataSource(workManager)
            Logger.d("SendEventTasksDataSource") { "Initialized" }
            return instance
        }

        actual val instance: SendEventTasksDataSource
            get() = sendEventTasksDataSource
                ?: throw IllegalStateException("EventTasksDataSource has not been initialized")
    }
}

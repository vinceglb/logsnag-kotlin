package com.logsnag.kotlin.api

import com.logsnag.kotlin.LogSnagConfig
import com.logsnag.kotlin.createHttpClient
import com.logsnag.kotlin.types.IdentifyOptions
import com.logsnag.kotlin.types.InsightIncrementOptions
import com.logsnag.kotlin.types.InsightTrackOptions
import com.logsnag.kotlin.types.TrackOptions

internal actual class SendEventTasksDataSource(
    private val logSnagApi: LogSnagApi,
) {
    actual suspend fun track(trackOptions: TrackOptions) {
        logSnagApi.track(trackOptions)
    }

    actual suspend fun identify(identifyOptions: IdentifyOptions) {
        logSnagApi.identify(identifyOptions)
    }

    actual suspend fun insightTrack(insightTrackOptions: InsightTrackOptions) {
        logSnagApi.insightTrack(insightTrackOptions)
    }

    actual suspend fun insightIncrement(insightIncrementOptions: InsightIncrementOptions) {
        logSnagApi.insightIncrement(insightIncrementOptions)
    }

    actual object Factory {
        actual fun create(config: LogSnagConfig): SendEventTasksDataSource {
            val client = createHttpClient(config)
            val logSnagApi = LogSnagApi(client)
            return SendEventTasksDataSource(logSnagApi)
        }
    }
}

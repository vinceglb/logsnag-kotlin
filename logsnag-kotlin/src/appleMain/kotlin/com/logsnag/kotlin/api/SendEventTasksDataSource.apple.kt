package com.logsnag.kotlin.api

import com.logsnag.kotlin.types.IdentifyOptions
import com.logsnag.kotlin.types.InsightIncrementOptions
import com.logsnag.kotlin.types.InsightTrackOptions
import com.logsnag.kotlin.types.TrackOptions
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

internal actual class SendEventTasksDataSource {
    private val scope = CoroutineScope(Dispatchers.Default)

    actual fun track(trackOptions: TrackOptions, token: String) {
        scope.launch {
            val api = LogSnagApi(token)
            api.track(trackOptions)
        }
    }

    actual fun identify(identifyOptions: IdentifyOptions, token: String) {
        scope.launch {
            val api = LogSnagApi(token)
            api.identify(identifyOptions)
        }
    }

    actual fun insightTrack(insightTrackOptions: InsightTrackOptions, token: String) {
        scope.launch {
            val api = LogSnagApi(token)
            api.insightTrack(insightTrackOptions)
        }
    }

    actual fun insightIncrement(insightIncrementOptions: InsightIncrementOptions, token: String) {
        scope.launch {
            val api = LogSnagApi(token)
            api.insightIncrement(insightIncrementOptions)
        }
    }

    actual companion object {
        actual val instance: SendEventTasksDataSource = SendEventTasksDataSource()
    }

}
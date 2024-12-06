package com.logsnag.kotlin.api

import com.logsnag.kotlin.LogSnagConfig
import com.logsnag.kotlin.types.IdentifyOptions
import com.logsnag.kotlin.types.InsightIncrementOptions
import com.logsnag.kotlin.types.InsightTrackOptions
import com.logsnag.kotlin.types.TrackOptions

internal expect class SendEventTasksDataSource {
    suspend fun track(trackOptions: TrackOptions)
    suspend fun identify(identifyOptions: IdentifyOptions)
    suspend fun insightTrack(insightTrackOptions: InsightTrackOptions)
    suspend fun insightIncrement(insightIncrementOptions: InsightIncrementOptions)

    object Factory {
        fun create(config: LogSnagConfig): SendEventTasksDataSource
    }
}

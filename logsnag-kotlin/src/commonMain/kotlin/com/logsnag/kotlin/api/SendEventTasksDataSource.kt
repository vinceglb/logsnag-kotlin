package com.logsnag.kotlin.api

import com.logsnag.kotlin.types.IdentifyOptions
import com.logsnag.kotlin.types.InsightIncrementOptions
import com.logsnag.kotlin.types.InsightTrackOptions
import com.logsnag.kotlin.types.TrackOptions

internal expect class SendEventTasksDataSource {
    fun track(trackOptions: TrackOptions, token: String)
    fun identify(identifyOptions: IdentifyOptions, token: String)
    fun insightTrack(insightTrackOptions: InsightTrackOptions, token: String)
    fun insightIncrement(insightIncrementOptions: InsightIncrementOptions, token: String)

    companion object {
        val instance: SendEventTasksDataSource
    }
}

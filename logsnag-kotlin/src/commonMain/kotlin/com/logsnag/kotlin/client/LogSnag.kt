package com.logsnag.kotlin.client

import co.touchlab.kermit.Logger
import com.logsnag.kotlin.api.LogSnagApi
import com.logsnag.kotlin.types.IdentifyOptions
import com.logsnag.kotlin.types.InsightIncrementOptions
import com.logsnag.kotlin.types.InsightIncrementValue
import com.logsnag.kotlin.types.InsightTrackOptions
import com.logsnag.kotlin.types.Parser
import com.logsnag.kotlin.types.Properties
import com.logsnag.kotlin.types.TrackOptions

class LogSnag(
    token: String,
    private val project: String,
    private var disabled: Boolean = false,
) {
    private val api = LogSnagApi(token)

    /**
     * Disable tracking for this instance
     * (this is useful for development)
     */
    fun disableTracking() {
        disabled = true
    }

    /**
     * Enable tracking for this instance
     * (this is useful for development)
     */
    fun enableTracking() {
        disabled = false
    }

    /**
     * Get project name
     * @returns project name
     */
    fun getProject(): String = project

    /**
     * Publish a new event to LogSnag
     *
     * @param channel Channel name
     * @param event Event name
     * @param description Event description
     * @param userId User ID
     * @param icon Event icon (emoji). Must be a single emoji
     * @param tags Event tags
     * @param notify Send push notification
     * @param parser Parser for description
     * @param timestamp Event timestamp
     * @returns true if event was sent successfully
     */
    suspend fun track(
        channel: String,
        event: String,
        description: String? = null,
        userId: String? = null,
        icon: String? = null,
        tags: Properties = emptyMap(),
        notify: Boolean = false,
        parser: Parser? = null,
        timestamp: Long? = null
    ): Boolean {
        if (disabled) return true

        // Construct track options
        val options = TrackOptions(
            project = project,
            channel = channel,
            event = event,
            description = description,
            userId = userId,
            icon = icon,
            tags = cleanProperties(tags),
            notify = notify,
            parser = parser,
            timestamp = timestamp,
        )

        // Send track request
        api.track(options)

        return true
    }

    /**
     * Identify a user
     *
     * @param userId User ID
     * @param properties User properties
     * @returns true if identify was sent successfully
     */
    suspend fun identify(
        userId: String,
        properties: Properties
    ): Boolean {
        if (disabled) return true

        // Construct identify options
        val options = IdentifyOptions(
            project = project,
            userId = userId,
            properties = cleanProperties(properties),
        )

        // Send identify request
        api.identify(options)

        return true
    }

    /**
     * Track an insight
     *
     * @param title Insight title
     * @param value Insight value
     * @param icon Insight icon (emoji). Must be a single emoji
     * @returns true if insight was sent successfully
     */
    suspend fun insightTrack(
        title: String,
        value: String,
        icon: String? = null,
    ): Boolean {
        if (disabled) return true

        // Construct insight options
        val options = InsightTrackOptions(
            project = project,
            title = title,
            icon = icon,
            value = value,
        )

        // Send insight request
        api.insightTrack(options)

        return true
    }

    /**
     * Increment an insight
     *
     * @param title Insight title
     * @param value Insight value
     * @param icon Insight icon (emoji). Must be a single emoji
     * @returns true if insight was sent successfully
     */
    suspend fun insightIncrement(
        title: String,
        value: Int,
        icon: String? = null,
    ): Boolean {
        if (disabled) return true

        // Construct insight options
        val options = InsightIncrementOptions(
            project = project,
            title = title,
            icon = icon,
            value = InsightIncrementValue(value),
        )

        // Send insight request
        api.insightIncrement(options)

        return true
    }

    private fun cleanProperties(properties: Properties): Properties {
        return properties.mapKeys {
            if (it.key != it.key.lowercase().replace(" ", "-")) {
                Logger.w("LogSnag") { "Tags Key must be lowercase with no spaces" }
                it.key.lowercase().replace(" ", "-")
            } else {
                it.key
            }
        }
    }
}

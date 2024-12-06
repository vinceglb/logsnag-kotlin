package com.logsnag.kotlin

import co.touchlab.kermit.Logger
import com.logsnag.kotlin.api.SendEventTasksDataSource
import com.logsnag.kotlin.types.IdentifyOptions
import com.logsnag.kotlin.types.InsightIncrementOptions
import com.logsnag.kotlin.types.InsightIncrementValue
import com.logsnag.kotlin.types.InsightTrackOptions
import com.logsnag.kotlin.types.Parser
import com.logsnag.kotlin.types.Properties
import com.logsnag.kotlin.types.TrackOptions
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.launch

public class LogSnag(
    token: String,
    private val project: String,
    private var disabled: Boolean = false,
    dispatcher: CoroutineDispatcher = Dispatchers.IO,
) {
    private val scope = CoroutineScope(dispatcher)
    private val log: SendEventTasksDataSource = SendEventTasksDataSource.Factory.create(
        config = LogSnagConfig(token)
    )

    /**
     * Disable tracking for this instance
     * (this is useful for development)
     */
    public fun disableTracking() {
        disabled = true
    }

    /**
     * Enable tracking for this instance
     * (this is useful for development)
     */
    public fun enableTracking() {
        disabled = false
    }

    /**
     * Get project name
     * @returns project name
     */
    public fun getProject(): String = project

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
    public fun track(
        channel: String,
        event: String,
        description: String? = null,
        userId: String? = null,
        icon: String? = null,
        tags: Properties = emptyMap(),
        notify: Boolean = false,
        parser: Parser? = null,
        timestamp: Long? = null
    ) {
        if (disabled) return

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
        scope.launch { log.track(options) }
    }

    /**
     * Identify a user
     *
     * @param userId User ID
     * @param properties User properties
     * @returns true if identify was sent successfully
     */
    public fun identify(
        userId: String,
        properties: Properties
    ) {
        if (disabled) return

        // Construct identify options
        val options = IdentifyOptions(
            project = project,
            userId = userId,
            properties = cleanProperties(properties),
        )

        // Send identify request
        scope.launch { log.identify(options) }
    }

    /**
     * Track an insight
     *
     * @param title Insight title
     * @param value Insight value
     * @param icon Insight icon (emoji). Must be a single emoji
     * @returns true if insight was sent successfully
     */
    public fun insightTrack(
        title: String,
        value: String,
        icon: String? = null,
    ) {
        if (disabled) return

        // Construct insight options
        val options = InsightTrackOptions(
            project = project,
            title = title,
            icon = icon,
            value = value,
        )

        // Send insight request
        scope.launch {
            log.insightTrack(options)
        }
    }

    /**
     * Increment an insight
     *
     * @param title Insight title
     * @param value Insight value
     * @param icon Insight icon (emoji). Must be a single emoji
     * @returns true if insight was sent successfully
     */
    public fun insightIncrement(
        title: String,
        value: Int,
        icon: String? = null,
    ) {
        if (disabled) return

        // Construct insight options
        val options = InsightIncrementOptions(
            project = project,
            title = title,
            icon = icon,
            value = InsightIncrementValue(value),
        )

        // Send insight request
        scope.launch { log.insightIncrement(options) }
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

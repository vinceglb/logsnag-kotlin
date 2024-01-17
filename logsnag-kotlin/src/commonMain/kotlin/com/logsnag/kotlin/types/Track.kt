package com.logsnag.kotlin.types

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Options for publishing LogSnag events
 */
@Serializable
internal data class TrackOptions(
    /**
     * Project name
     * example: "my-project"
     */
    val project: String,

    /**
     * Channel name
     * example: "waitlist"
     */
    val channel: String,

    /**
     * Event name
     * example: "User Joined"
     */
    val event: String,

    /**
     * Event description
     * example: "joe@example.com joined waitlist"
     */
    val description: String? = null,

    /**
     * User ID
     * example: "user_123"
     */
    @SerialName("user_id")
    val userId: String? = null,

    /**
     * Event icon (emoji)
     * must be a single emoji
     * example: "🎉"
     */
    val icon: String? = null,

    /**
     * Event tags
     * example: mapOf("username" to "mattie")
     */
    val tags: Properties = emptyMap(),

    /**
     * Send push notification
     */
    val notify: Boolean = false,

    /**
     * Parser for description
     */
    val parser: Parser? = null,

    /**
     * Event timestamp
     */
    val timestamp: Long? = null
)

/** Tag Type **/
typealias Parser = String // Assuming that 'markdown' or 'text' are valid values for Parser

/** Properties Type **/
typealias Properties = Map<String, String>

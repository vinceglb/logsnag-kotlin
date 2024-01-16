package com.logsnag.kotlin.types

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

internal interface InsightBase {
    /**
     * Project name
     * example: "my-project"
     */
    val project: String

    /**
     * Insight title
     * example: "User Count"
     */
    val title: String

    /**
     * Event icon (emoji)
     * must be a single emoji
     * example: "👨"
     */
    val icon: String?
}

/**
 * Options for publishing LogSnag insight
 */
@Serializable
internal data class InsightTrackOptions(
    override val project: String,
    override val title: String,
    override val icon: String? = null,

    /**
     * Insight value
     * example: 100
     */
    val value: String,
) : InsightBase

/**
 * Options for publishing LogSnag increment insight
 */
@Serializable
internal data class InsightIncrementOptions(
    override val project: String,
    override val title: String,
    override val icon: String? = null,

    /**
     * Insight value
     * example: 1
     */
    val value: InsightIncrementValue,
) : InsightBase

@Serializable
internal data class InsightIncrementValue(
    @SerialName("\$inc")
    val inc: Int,
)

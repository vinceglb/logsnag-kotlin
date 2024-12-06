package com.logsnag.kotlin.types

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Options for publishing LogSnag identify
 */
@Serializable
internal data class IdentifyOptions(
    /**
     * Project name
     * example: "my-project"
     */
    val project: String,

    /**
     * User ID
     * example: "user_123"
     */
    @SerialName("user_id")
    val userId: String,

    /**
     * User properties
     * example: mapOf("username" to "mattie")
     */
    val properties: Properties,
)

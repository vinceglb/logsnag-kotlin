package com.logsnag.kotlin

/**
 * LogSnag Base Endpoint
 */
internal const val LOGSNAG_BASE = "https://api.logsnag.com"

internal object ENDPOINTS {
    /**
     * LogSnag Log Endpoint
     */
    const val LOG = "$LOGSNAG_BASE/v1/log"

    /**
     * LogSnag Identify Endpoint
     */
    const val IDENTIFY = "$LOGSNAG_BASE/v1/identify"

    /**
     * LogSnag Insight Endpoint
     */
    const val INSIGHT = "$LOGSNAG_BASE/v1/insight"
}

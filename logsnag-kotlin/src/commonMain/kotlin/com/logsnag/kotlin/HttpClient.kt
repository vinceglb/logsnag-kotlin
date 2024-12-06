package com.logsnag.kotlin

import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BearerTokens
import io.ktor.client.plugins.auth.providers.bearer
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

internal fun createHttpClient(config: LogSnagConfig): HttpClient {
    val configuration: HttpClientConfig<*>.() -> Unit = {
        install(ContentNegotiation) {
            json(Json { explicitNulls = false })
        }

        install(Auth) {
            bearer {
                loadTokens {
                    BearerTokens(accessToken = config.token, refreshToken = null)
                }
            }
        }
    }

    return when (config.engine) {
        null -> HttpClient(configuration)
        else -> HttpClient(config.engine, configuration)
    }
}
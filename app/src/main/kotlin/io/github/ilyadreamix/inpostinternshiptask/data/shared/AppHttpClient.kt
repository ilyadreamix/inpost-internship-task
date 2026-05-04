package io.github.ilyadreamix.inpostinternshiptask.data.shared

import android.util.Log
import io.github.ilyadreamix.inpostinternshiptask.BuildConfig
import io.github.ilyadreamix.inpostinternshiptask.shared.AppBuildFlavor
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.HttpSend
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.plugin
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

internal fun buildAppHttpClient(): HttpClient {
  val client = HttpClient(OkHttp) {
    defaultRequest {
      url(ApiEndpoint)
    }

    install(ContentNegotiation) {
      json(
        json = Json {
          ignoreUnknownKeys = true
        }
      )
    }
  }

  client.configureLogging()

  return client
}

private fun HttpClient.configureLogging() {
  plugin(HttpSend).intercept { call ->
    if (BuildConfig.FLAVOR == AppBuildFlavor.Development.value) {
      Log.d(LogTag, "HttpSend.intercept: Making request to ${call.url}")
    }

    return@intercept execute(call)
  }
}

private const val LogTag = "AppHttpClient"
private const val ApiEndpoint = "https://api-global-points.easypack24.net/v1/"

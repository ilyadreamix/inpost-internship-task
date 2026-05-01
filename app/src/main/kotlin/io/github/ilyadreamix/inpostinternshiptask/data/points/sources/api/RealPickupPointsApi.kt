package io.github.ilyadreamix.inpostinternshiptask.data.points.sources.api

import io.github.ilyadreamix.inpostinternshiptask.data.points.sources.api.dtos.ListPickupPointsResponseApiDto
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.http.parameters

internal class RealPickupPointsApi(private val httpClient: HttpClient) : PickupPointsApi {
  override suspend fun listPickupPoints(
    perPage: Int?,
    status: String?,
    country: String?,
    relativePoint: String?
  ): ListPickupPointsResponseApiDto {
    val response = httpClient.get("points") {
      parameters {
        if (perPage != null) { append("per_page", perPage.toString()) }
        if (status != null) { append("status", status) }
        if (country != null) { append("country", country) }
        if (relativePoint != null) { append("relative_point", relativePoint) }
      }
    }
    return response.body()
  }
}

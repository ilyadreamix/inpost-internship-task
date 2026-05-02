package io.github.ilyadreamix.inpostinternshiptask.data.points.sources.api

import io.github.ilyadreamix.inpostinternshiptask.data.points.sources.api.dtos.ListPickupPointsResponseApiDto
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter

internal class RealPickupPointsApi(private val httpClient: HttpClient) : PickupPointsApi {
  override suspend fun listPickupPoints(
    perPage: Int?,
    status: String?,
    country: String?,
    relativePoint: String?
  ): ListPickupPointsResponseApiDto {
    val response = httpClient.get("points") {
      if (perPage != null) { parameter("per_page", perPage) }
      if (status != null) { parameter("status", status) }
      if (country != null) { parameter("country", country) }
      if (relativePoint != null) { parameter("relative_point", relativePoint) }
    }
    return response.body()
  }
}

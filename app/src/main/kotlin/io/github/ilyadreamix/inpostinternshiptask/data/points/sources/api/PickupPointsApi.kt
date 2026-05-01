package io.github.ilyadreamix.inpostinternshiptask.data.points.sources.api

import io.github.ilyadreamix.inpostinternshiptask.data.points.sources.api.dtos.ListPickupPointsResponseApiDto

internal interface PickupPointsApi {
  suspend fun listPickupPoints(
    perPage: Int?,
    status: String?,
    country: String?,
    relativePoint: String?
  ): ListPickupPointsResponseApiDto
}

package io.github.ilyadreamix.inpostinternshiptask.domain.points.repositories

import io.github.ilyadreamix.inpostinternshiptask.domain.points.options.ListPickupPointsOption
import io.github.ilyadreamix.inpostinternshiptask.domain.points.results.ListPickupPointsResult

internal interface PickupPointsRepository {
  suspend fun listPickupPoints(option: ListPickupPointsOption): ListPickupPointsResult
}

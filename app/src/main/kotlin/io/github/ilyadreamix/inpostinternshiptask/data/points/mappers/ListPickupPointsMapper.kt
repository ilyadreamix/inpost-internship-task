package io.github.ilyadreamix.inpostinternshiptask.data.points.mappers

import io.github.ilyadreamix.inpostinternshiptask.data.points.sources.api.dtos.ListPickupPointsResponseApiDto
import io.github.ilyadreamix.inpostinternshiptask.domain.points.results.ListPickupPointsResult

internal interface ListPickupPointsMapper {
  fun apiResponseToResult(response: ListPickupPointsResponseApiDto): ListPickupPointsResult
}

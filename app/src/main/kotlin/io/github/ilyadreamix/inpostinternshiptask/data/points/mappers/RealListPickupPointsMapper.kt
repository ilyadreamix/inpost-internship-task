package io.github.ilyadreamix.inpostinternshiptask.data.points.mappers

import io.github.ilyadreamix.inpostinternshiptask.data.points.sources.api.dtos.ListPickupPointsResponseApiDto
import io.github.ilyadreamix.inpostinternshiptask.domain.points.results.ListPickupPointsResult

internal class RealListPickupPointsMapper(private val pickupPointMapper: PickupPointMapper) : ListPickupPointsMapper {
  override fun apiResponseToResult(response: ListPickupPointsResponseApiDto) =
    ListPickupPointsResult(items = response.items.map(pickupPointMapper::apiDtoToModel))
}

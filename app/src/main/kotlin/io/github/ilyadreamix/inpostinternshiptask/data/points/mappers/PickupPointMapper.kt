package io.github.ilyadreamix.inpostinternshiptask.data.points.mappers

import io.github.ilyadreamix.inpostinternshiptask.data.points.sources.api.dtos.PickupPointApiDto
import io.github.ilyadreamix.inpostinternshiptask.domain.points.models.PickupPointModel

internal interface PickupPointMapper {

  fun apiDtoToModel(dto: PickupPointApiDto): PickupPointModel

  fun apiCountryToModel(country: String): PickupPointModel.Country
  fun apiStatusToModel(status: String): PickupPointModel.Status
  fun apiLocationTypeToModel(locationType: String): PickupPointModel.LocationType

  fun modelCountryToApi(country: PickupPointModel.Country): String
  fun modelStatusToApi(status: PickupPointModel.Status): String
  fun modelLocationTypeToApi(locationType: PickupPointModel.LocationType): String
}

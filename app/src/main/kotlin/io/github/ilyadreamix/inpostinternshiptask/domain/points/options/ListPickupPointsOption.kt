package io.github.ilyadreamix.inpostinternshiptask.domain.points.options

import io.github.ilyadreamix.inpostinternshiptask.domain.points.models.PickupPointModel

internal data class ListPickupPointsOption(
  val perPage: Int? = 100,
  val status: PickupPointModel.Status? = PickupPointModel.Status.Operating,
  val country: PickupPointModel.Country? = PickupPointModel.Country.Poland,
  val relativePoint: PickupPointModel.Location? = null
)

package io.github.ilyadreamix.inpostinternshiptask.domain.points.usecases

import io.github.ilyadreamix.inpostinternshiptask.domain.points.options.ListPickupPointsOption
import io.github.ilyadreamix.inpostinternshiptask.domain.points.repositories.PickupPointsRepository

internal class ListPickupPointsUseCase(private val pickupPointsRepository: PickupPointsRepository) {
  suspend operator fun invoke(option: ListPickupPointsOption) = pickupPointsRepository.listPickupPoints(option)
}

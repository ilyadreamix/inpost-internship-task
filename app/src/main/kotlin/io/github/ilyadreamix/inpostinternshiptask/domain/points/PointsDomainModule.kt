package io.github.ilyadreamix.inpostinternshiptask.domain.points

import io.github.ilyadreamix.inpostinternshiptask.domain.points.usecases.ListPickupPointsUseCase
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

internal val PointsDomainModule = module {
  singleOf(::ListPickupPointsUseCase)
}

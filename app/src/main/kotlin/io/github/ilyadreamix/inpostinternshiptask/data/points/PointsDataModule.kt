package io.github.ilyadreamix.inpostinternshiptask.data.points

import io.github.ilyadreamix.inpostinternshiptask.data.points.mappers.ListPickupPointsMapper
import io.github.ilyadreamix.inpostinternshiptask.data.points.mappers.PickupPointMapper
import io.github.ilyadreamix.inpostinternshiptask.data.points.mappers.RealListPickupPointsMapper
import io.github.ilyadreamix.inpostinternshiptask.data.points.mappers.RealPickupPointMapper
import io.github.ilyadreamix.inpostinternshiptask.data.points.repositories.RealPickupPointsRepository
import io.github.ilyadreamix.inpostinternshiptask.data.points.sources.api.PickupPointsApi
import io.github.ilyadreamix.inpostinternshiptask.data.points.sources.api.RealPickupPointsApi
import io.github.ilyadreamix.inpostinternshiptask.domain.points.repositories.PickupPointsRepository
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

internal val PointsDataModule = module {
  singleOf(::RealListPickupPointsMapper) { bind<ListPickupPointsMapper>() }
  singleOf(::RealPickupPointMapper) { bind<PickupPointMapper>() }

  singleOf(::RealPickupPointsApi) { bind<PickupPointsApi>() }

  singleOf(::RealPickupPointsRepository) { bind<PickupPointsRepository>() }
}

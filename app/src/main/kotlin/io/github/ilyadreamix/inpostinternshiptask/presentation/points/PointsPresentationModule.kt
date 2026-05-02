package io.github.ilyadreamix.inpostinternshiptask.presentation.points

import io.github.ilyadreamix.inpostinternshiptask.presentation.points.map.PointsMapViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

internal val PointsPresentationModule = module {
  viewModelOf(::PointsMapViewModel)
}

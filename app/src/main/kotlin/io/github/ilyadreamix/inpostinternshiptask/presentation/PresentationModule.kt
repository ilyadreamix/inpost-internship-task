package io.github.ilyadreamix.inpostinternshiptask.presentation

import io.github.ilyadreamix.inpostinternshiptask.presentation.points.PointsPresentationModule
import org.koin.dsl.module

internal val PresentationModule = module {
  includes(PointsPresentationModule)
}

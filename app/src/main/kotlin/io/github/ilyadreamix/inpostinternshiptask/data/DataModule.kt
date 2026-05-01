package io.github.ilyadreamix.inpostinternshiptask.data

import io.github.ilyadreamix.inpostinternshiptask.data.points.PointsDataModule
import org.koin.dsl.module

internal val DataModule = module {
  includes(PointsDataModule)
}

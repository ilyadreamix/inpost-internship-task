package io.github.ilyadreamix.inpostinternshiptask.domain

import io.github.ilyadreamix.inpostinternshiptask.domain.points.PointsDomainModule
import org.koin.dsl.module

internal val DomainModule = module {
  includes(PointsDomainModule)
}

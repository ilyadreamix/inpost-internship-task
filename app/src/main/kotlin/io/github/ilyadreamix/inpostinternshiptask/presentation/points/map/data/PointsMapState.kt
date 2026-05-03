package io.github.ilyadreamix.inpostinternshiptask.presentation.points.map.data

import io.github.ilyadreamix.inpostinternshiptask.presentation.points.map.composables.PointsMapMarkerData

internal data class PointsMapState(
  val markers: List<PointsMapMarkerData> = emptyList(),
  val hasError: Boolean = false
)

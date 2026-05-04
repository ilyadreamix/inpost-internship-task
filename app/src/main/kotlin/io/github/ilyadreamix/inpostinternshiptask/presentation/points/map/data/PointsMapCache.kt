package io.github.ilyadreamix.inpostinternshiptask.presentation.points.map.data

import io.github.ilyadreamix.inpostinternshiptask.presentation.points.map.composables.PointsMapMarkerData
import kotlin.math.roundToInt

internal class PointsMapCache private constructor(private val maxSize: Int, capacity: Int, loadFactor: Float = DefaultLoadFactor)
  : LinkedHashMap<String, PointsMapMarkerData>(capacity, loadFactor, true) {

  override fun removeEldestEntry(eldest: Map.Entry<String?, PointsMapMarkerData?>?) = size == maxSize

  companion object {

    const val DefaultLoadFactor = 0.75f

    operator fun invoke(maxSize: Int): PointsMapCache {
      val capacity = (maxSize / DefaultLoadFactor).roundToInt()
      return PointsMapCache(maxSize, capacity)
    }
  }
}

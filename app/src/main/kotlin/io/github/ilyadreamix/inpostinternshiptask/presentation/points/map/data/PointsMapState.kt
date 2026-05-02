package io.github.ilyadreamix.inpostinternshiptask.presentation.points.map.data

import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.clustering.ClusterItem
import io.github.ilyadreamix.inpostinternshiptask.domain.points.models.PickupPointModel

internal data class PointsMapState(
  val isZoomWarningVisible: Boolean = true,
  val points: List<Point> = emptyList()
) {
  data class Point(val model: PickupPointModel) : ClusterItem {
    override fun getPosition() = LatLng(model.location.latitude, model.location.longitude)
    override fun getTitle() = null
    override fun getSnippet() = null
    override fun getZIndex() = null
  }
}

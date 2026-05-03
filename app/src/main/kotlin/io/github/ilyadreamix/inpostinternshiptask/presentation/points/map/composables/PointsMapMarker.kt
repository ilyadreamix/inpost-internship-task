package io.github.ilyadreamix.inpostinternshiptask.presentation.points.map.composables

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.clustering.ClusterItem
import io.github.ilyadreamix.inpostinternshiptask.R
import io.github.ilyadreamix.inpostinternshiptask.domain.points.models.PickupPointModel

internal data class PointsMapMarkerData(val point: PickupPointModel) : ClusterItem {
  override fun getPosition() = LatLng(point.location.latitude, point.location.longitude)
  override fun getTitle() = null
  override fun getSnippet() = null
  override fun getZIndex() = null
}

@Composable
internal fun PointsMapMarker(data: PointsMapMarkerData, modifier: Modifier = Modifier) {

  val iconPainter = painterResource(R.drawable.mic_shelves)

  val colorPrimary = MaterialTheme.colorScheme.primary
  val colorOnPrimary = MaterialTheme.colorScheme.onPrimary
  val colorOutline = MaterialTheme.colorScheme.outline

  Canvas(modifier = modifier.size(MarkerSize)) {
    drawMarkerBackground(color = colorPrimary, borderColor = colorOutline)
    drawMarkerIcon(iconPainter, color = colorOnPrimary)
  }
}

private val MarkerSize = 36.dp

private fun DrawScope.drawMarkerBackground(color: Color, borderColor: Color) {
  drawCircle(color = borderColor, radius = size.minDimension / 2)
  drawCircle(color = color, radius = (size.minDimension / 2) - MarkerBackgroundBorderThickness.toPx())
}

private val MarkerBackgroundBorderThickness = 1.dp

private fun DrawScope.drawMarkerIcon(painter: Painter, color: Color) {

  val iconSizePx = MarkerIconSize.toPx()

  translate(left = (size.width - iconSizePx) / 2, top = (size.height - iconSizePx) / 2) {
    with(painter) {
      draw(
        size = Size(iconSizePx, iconSizePx),
        colorFilter = ColorFilter.tint(color)
      )
    }
  }
}

private val MarkerIconSize = 20.dp

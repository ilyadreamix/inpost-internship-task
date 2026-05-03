package io.github.ilyadreamix.inpostinternshiptask.presentation.points.map.composables

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.inset
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
  val iconPainter = painterResource(R.drawable.mic_location_on)
  val easyAccessIconPainter = painterResource(R.drawable.mic_accessible)

  val colorPrimary = MaterialTheme.colorScheme.primary
  val colorOnPrimary = MaterialTheme.colorScheme.onPrimary
  val colorOutline = MaterialTheme.colorScheme.outline

  Canvas(modifier = modifier.size(MarkerContainerSize)) {

    val markerInsetPx = MarkerInset.toPx()

    inset(horizontal = markerInsetPx, vertical = markerInsetPx) {
      drawMarkerBackground(color = colorPrimary, borderColor = colorOutline)
      drawMarkerIcon(iconPainter, color = colorOnPrimary)
    }

    if (data.point.easyAccess) {
      inset(left = size.minDimension / 1.5f, top = size.minDimension / 1.5f, right = 0f, bottom = 0f) {
        drawMarkerEasyAccessIcon(painter = easyAccessIconPainter, borderColor = colorOutline)
      }
    }
  }
}

private val MarkerContainerSize = 48.dp
private val MarkerInset = 6.dp

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

private fun DrawScope.drawMarkerEasyAccessIcon(painter: Painter, borderColor: Color) {
  val borderThicknessPx = MarkerEasyAccessBorderThickness.toPx()
  val borderCornerRadiusPx = MarkerEasyAccessBorderCornerRadius.toPx()
  val cornerRadiusPx = MarkerEasyAccessCornerRadius.toPx()

  drawRoundRect(
    color = borderColor,
    cornerRadius = CornerRadius(borderCornerRadiusPx, borderCornerRadiusPx)
  )

  inset(vertical = borderThicknessPx, horizontal = borderThicknessPx) {
    drawRoundRect(
      color = MarkerEasyAccessIconBackgroundColor,
      cornerRadius = CornerRadius(cornerRadiusPx, cornerRadiusPx)
    )

    with(painter) {
      val iconSizePx = size.minDimension * 0.9f
      val iconInset = (size.minDimension - iconSizePx) / 2f

      inset(vertical = iconInset, horizontal = iconInset) {
        draw(
          size = Size(size.minDimension, size.minDimension),
          colorFilter = ColorFilter.tint(MarkerEasyAccessIconColor)
        )
      }
    }
  }
}

private val MarkerIconSize = 20.dp
private val MarkerEasyAccessIconBackgroundColor = Color(0xFF0040FF)
private val MarkerEasyAccessIconColor = Color.White
private val MarkerEasyAccessBorderCornerRadius = 2.5.dp
private val MarkerEasyAccessBorderThickness = 1.dp
private val MarkerEasyAccessCornerRadius = 1.5.dp

package io.github.ilyadreamix.inpostinternshiptask.presentation.points.map.composables

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.inset
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.graphics.graphicsLayer
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
internal fun PointsMapMarker(
  data: PointsMapMarkerData,
  focused: Boolean,
  hidden: Boolean,
  modifier: Modifier = Modifier
) {
  // At first, it was supposed to be passed to drawMarker* functions' sizeFraction parameter,
  // but I've decided to pass it to Canvas graphicsLayer instead because of the lags.
  // It looks worse, but does not make the map too chunky
  val sizeFraction = animateFloatAsState(if (focused) MarkerFocusedSizeFraction else 1f)

  val iconPainter = painterResource(R.drawable.mic_location_on)
  val easyAccessIconPainter = painterResource(R.drawable.mic_accessible)

  val colorPrimary = MaterialTheme.colorScheme.primary
  val colorOnPrimary = MaterialTheme.colorScheme.onPrimary
  val colorOutline = MaterialTheme.colorScheme.outline

  Box(
    modifier = modifier
      .size(MarkerSize * MarkerFocusedSizeFraction)
      .graphicsLayer { alpha = if (hidden) 0f else 1f }, // There also could be animation, but it makes map even more laggy
    contentAlignment = Alignment.Center
  ) {
    Canvas(
      modifier = Modifier
        .size(MarkerSize)
        .graphicsLayer { scaleX = sizeFraction.value; scaleY = sizeFraction.value }
    ) {

      val markerInsetPx = MarkerInset.toPx() // * sizeFraction.value

      inset(horizontal = markerInsetPx, vertical = markerInsetPx) {
        drawMarkerBackground(
          color = colorPrimary,
          borderColor = colorOutline,
          /* sizeFraction = sizeFraction.value */
        )

        drawMarkerIcon(
          painter = iconPainter,
          color = colorOnPrimary,
          /* sizeFraction = sizeFraction.value */
        )
      }

      if (data.point.easyAccess) {
        inset(left = size.minDimension / 1.5f, top = size.minDimension / 1.5f, right = 0f, bottom = 0f) {
          drawMarkerEasyAccessIcon(
            painter = easyAccessIconPainter,
            borderColor = colorOutline,
            /* sizeFraction = sizeFraction.value */
          )
        }
      }
    }
  }
}

private val MarkerSize = 48.dp
private val MarkerInset = 6.dp
private const val MarkerFocusedSizeFraction = 1.5f

private fun DrawScope.drawMarkerBackground(color: Color, borderColor: Color, sizeFraction: Float = 1f) {
  drawCircle(color = borderColor, radius = size.minDimension / 2)
  drawCircle(color = color, radius = (size.minDimension / 2) - (MarkerBackgroundBorderThickness.toPx() * sizeFraction))
}

private val MarkerBackgroundBorderThickness = 1.dp

private fun DrawScope.drawMarkerIcon(painter: Painter, color: Color, sizeFraction: Float = 1f) {

  val iconSizePx = MarkerIconSize.toPx() * sizeFraction

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

private fun DrawScope.drawMarkerEasyAccessIcon(painter: Painter, borderColor: Color, sizeFraction: Float = 1f) {
  val borderThicknessPx = MarkerBackgroundBorderThickness.toPx() * sizeFraction
  val borderCornerRadiusPx = MarkerEasyAccessBorderCornerRadius.toPx() * sizeFraction
  val cornerRadiusPx = MarkerEasyAccessCornerRadius.toPx() * sizeFraction

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
private val MarkerEasyAccessIconBackgroundColor = Color(0xFF0040FF)
private val MarkerEasyAccessIconColor = Color.White
private val MarkerEasyAccessBorderCornerRadius = 2.5.dp
private val MarkerEasyAccessCornerRadius = 1.5.dp

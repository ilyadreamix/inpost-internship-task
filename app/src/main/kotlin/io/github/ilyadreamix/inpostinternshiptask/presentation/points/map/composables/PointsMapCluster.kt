package io.github.ilyadreamix.inpostinternshiptask.presentation.points.map.composables

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.text.TextMeasurer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.maps.android.clustering.Cluster
import io.github.ilyadreamix.inpostinternshiptask.presentation.shared.shapes.RoundedRhombusPath

@Composable
internal fun PointsMapCluster(data: Cluster<PointsMapMarkerData>, modifier: Modifier = Modifier) {

  val textMeasurer = rememberTextMeasurer()

  val colorSecondary = MaterialTheme.colorScheme.secondary
  val colorOnSecondary = MaterialTheme.colorScheme.onSecondary
  val colorOutline = MaterialTheme.colorScheme.outline

  Canvas(modifier = modifier.clusterSize(data.size)) {
    drawClusterBackground(borderColor = colorOutline, color = colorSecondary)
    drawClusterText(textMeasurer, markersCount = data.size, color = colorOnSecondary)
  }
}

private fun Modifier.clusterSize(markersCount: Int) = size(
  when {
    markersCount <= 25 -> ClusterSizeSmall
    markersCount <= 50 -> ClusterSizeMedium
    markersCount <= 100 -> ClusterSizeBig
    else -> ClusterSizeHuge
  }
)

private val ClusterSizeSmall = 56.dp
private val ClusterSizeMedium = 64.dp
private val ClusterSizeBig = 72.dp
private val ClusterSizeHuge = 80.dp

private fun DrawScope.drawClusterBackground(borderColor: Color, color: Color) {

  val borderThicknessPx = ClusterBackgroundBorderThickness.toPx()

  drawPath(
    path = RoundedRhombusPath(radius = ClusterBackgroundBorderRadius, density = drawContext.density, size = size),
    color = borderColor
  )

  translate(left = borderThicknessPx / 2, top = borderThicknessPx / 2) {
    drawPath(
      path = RoundedRhombusPath(
        radius = ClusterBackgroundRadius,
        density = drawContext.density,
        size = Size(width = size.minDimension - borderThicknessPx, height = size.minDimension - borderThicknessPx)
      ),
      color = color
    )
  }
}

private val ClusterBackgroundBorderThickness = 3.dp
private val ClusterBackgroundBorderRadius = 5.dp
private val ClusterBackgroundRadius = 4.dp

private fun DrawScope.drawClusterText(textMeasurer: TextMeasurer, markersCount: Int, color: Color) {
  val textSize = when {
    markersCount <= 25 -> ClusterTextSizeSmall
    markersCount <= 50 -> ClusterTextSizeMedium
    markersCount <= 100 -> ClusterTextSizeBig
    else -> ClusterTextSizeHuge
  }

  val weight = if (markersCount >= 75) FontWeight.Bold else FontWeight.Normal

  val layoutResult = textMeasurer.measure(
    text = if (markersCount <= 100) markersCount.toString() else "100+",
    style = TextStyle(textAlign = TextAlign.Center, fontSize = textSize, color = color, fontWeight = weight)
  )

  drawText(
    textLayoutResult = layoutResult,
    topLeft = Offset(
      x = (size.width - layoutResult.size.width) / 2,
      y = (size.height - layoutResult.size.height) / 2
    )
  )
}

private val ClusterTextSizeSmall = 14.sp
private val ClusterTextSizeMedium = 16.sp
private val ClusterTextSizeBig = 18.sp
private val ClusterTextSizeHuge = 20.sp

package io.github.ilyadreamix.inpostinternshiptask.presentation.points.map.composables

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.maps.android.clustering.view.DefaultClusterRenderer
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.ComposeMapColorScheme
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.MapsComposeExperimentalApi
import com.google.maps.android.compose.clustering.Clustering
import com.google.maps.android.compose.rememberCameraPositionState
import io.github.ilyadreamix.inpostinternshiptask.R
import io.github.ilyadreamix.inpostinternshiptask.presentation.points.map.PointsMapViewModel
import io.github.ilyadreamix.inpostinternshiptask.presentation.points.map.data.PointsMapState
import io.github.ilyadreamix.inpostinternshiptask.presentation.shared.shapes.RoundedRhombusPath
import org.koin.compose.viewmodel.koinViewModel

@Composable
internal fun PointsMap(viewModel: PointsMapViewModel = koinViewModel()) {

  val cameraPositionState = rememberCameraPositionState { position = PolandCenter }
  val state = viewModel.state.collectAsStateWithLifecycle()

  PointsMap(
    state = state.value,
    cameraPositionState = cameraPositionState
  )

  LaunchedEffect(cameraPositionState.isMoving) {
    if (cameraPositionState.isMoving) {
      val centerUpdate = cameraPositionState.position.target
      val zoom = cameraPositionState.position.zoom

      viewModel.onCameraIdle(centerUpdate, zoom)
    }
  }
}

@OptIn(MapsComposeExperimentalApi::class)
@Composable
internal fun PointsMap(state: PointsMapState, cameraPositionState: CameraPositionState) {
  GoogleMap(
    modifier = Modifier.fillMaxSize(),
    uiSettings = MapUiSettings(
      compassEnabled = false,
      indoorLevelPickerEnabled = false,
      mapToolbarEnabled = false,
      myLocationButtonEnabled = false,
      zoomControlsEnabled = false
    ),
    contentPadding = WindowInsets.systemBars.asPaddingValues(),
    mapColorScheme = ComposeMapColorScheme.FOLLOW_SYSTEM,
    properties = MapProperties(latLngBoundsForCameraTarget = PolandBoundaries, minZoomPreference = MinZoom),
    cameraPositionState = cameraPositionState
  ) {

    val textMeasurer = rememberTextMeasurer()

    val colorPrimary = MaterialTheme.colorScheme.primary
    val colorOnPrimary = MaterialTheme.colorScheme.onPrimary

    val textTitleMedium = MaterialTheme.typography.titleMedium
    val textTitleLarge = MaterialTheme.typography.titleLarge

    Clustering(
      items = state.points,
      clusterContent = {
        Canvas(
          modifier = Modifier.size(
            when {
              it.size < 25 -> 56.dp
              it.size < 50 -> 64.dp
              else -> 72.dp
            }
          )
        ) {
          drawPath(
            path = RoundedRhombusPath(radius = 5.dp, density = drawContext.density, size = size),
            color = colorOnPrimary
          )

          translate(
            left = 1.5.dp.toPx(),
            top = 1.5.dp.toPx()
          ) {
            drawPath(
              path = RoundedRhombusPath(
                radius = 4.dp,
                density = drawContext.density,
                size = Size(width = size.minDimension - 3.dp.toPx(), height = size.minDimension - 3.dp.toPx())
              ),
              color = colorPrimary
            )
          }

          val style = if (it.size < 50) textTitleMedium else textTitleLarge
          val layoutResult = textMeasurer.measure(
            text = if (it.size <= 100) it.size.toString() else "100+",
            style = style.copy(fontWeight = FontWeight.Bold, color = colorOnPrimary)
          )

          drawText(
            textLayoutResult = layoutResult,
            topLeft = Offset(
              x = (size.width - layoutResult.size.width) / 2,
              y = (size.height - layoutResult.size.height) / 2
            )
          )
        }
      },
      clusterItemContent = {
        val secondaryColor = MaterialTheme.colorScheme.secondary
        val onSecondaryColor = MaterialTheme.colorScheme.onSecondary
        val painter = painterResource(R.drawable.mic_shelves)

        Canvas(modifier = Modifier.size(36.dp)) {
          drawCircle(
            color = onSecondaryColor,
            radius = size.minDimension / 2
          )

          drawCircle(
            color = secondaryColor,
            radius = (size.minDimension / 2) - 1.dp.toPx()
          )

          val iconSize = 20.dp.toPx()
          translate(
            left = (size.width - iconSize) / 2,
            top = (size.height - iconSize) / 2
          ) {
            with(painter) {
              draw(
                size = Size(iconSize, iconSize),
                colorFilter = ColorFilter.tint(onSecondaryColor)
              )
            }
          }
        }
      },
      onClusterManager = { (it.renderer as? DefaultClusterRenderer<*>)?.minClusterSize = 3 }
    )
  }
}

private const val MinZoom = 5.5f
private val PolandBoundaries = LatLngBounds(LatLng(48.5, 14.3), LatLng(54.6, 24.6))
private val PolandCenter = CameraPosition.fromLatLngZoom(LatLng(52.0, 19.0), 5.5f)

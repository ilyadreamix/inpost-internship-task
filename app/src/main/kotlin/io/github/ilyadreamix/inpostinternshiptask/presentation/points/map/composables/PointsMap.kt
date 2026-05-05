package io.github.ilyadreamix.inpostinternshiptask.presentation.points.map.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.maps.android.clustering.ClusterItem
import com.google.maps.android.clustering.view.DefaultClusterRenderer
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.ComposeMapColorScheme
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.GoogleMapComposable
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.MapsComposeExperimentalApi
import com.google.maps.android.compose.clustering.Clustering
import io.github.ilyadreamix.inpostinternshiptask.R
import io.github.ilyadreamix.inpostinternshiptask.presentation.points.map.data.PointsMapState
import io.github.ilyadreamix.inpostinternshiptask.presentation.shared.theme.AppTokens
import kotlinx.coroutines.launch

@OptIn(MapsComposeExperimentalApi::class)
@Composable
internal fun PointsMap(
  state: PointsMapState,
  cameraPositionState: CameraPositionState,
  onMarkerFocused: (marker: PointsMapMarkerData) -> Unit,
  contentPadding: PaddingValues,
  disableGestures: Boolean,
  currentLocation: LatLng?,
  modifier: Modifier = Modifier
) {

  val coroutineScope = rememberCoroutineScope()

  GoogleMap(
    modifier = modifier.fillMaxSize(),
    uiSettings = MapUiSettings(
      compassEnabled = false,
      indoorLevelPickerEnabled = false,
      mapToolbarEnabled = false,
      myLocationButtonEnabled = false,
      zoomControlsEnabled = false,
      scrollGesturesEnabled = !disableGestures,
      tiltGesturesEnabled = false,
      zoomGesturesEnabled = !disableGestures,
      rotationGesturesEnabled = false,
      scrollGesturesEnabledDuringRotateOrZoom = true,
    ),
    contentPadding = contentPadding,
    mapColorScheme = ComposeMapColorScheme.FOLLOW_SYSTEM,
    properties = MapProperties(latLngBoundsForCameraTarget = MapPolandBoundaries, minZoomPreference = MapMinZoom),
    cameraPositionState = cameraPositionState
  ) {

    MapCurrentLocationIndicator(latLng = currentLocation)

    Clustering(
      items = state.markers,
      clusterContent = { cluster -> PointsMapCluster(data = cluster) },
      clusterItemContent = { marker ->

        val markerVisible = cameraPositionState.calculateMapMarkerVisible(marker, focusedMarker = state.focusedMarker)

        PointsMapMarker(
          data = marker,
          state = when {
            marker.point.name == state.focusedMarker?.point?.name -> PointsMapMarkerState.Focused
            !markerVisible.value -> PointsMapMarkerState.Hidden
            else -> PointsMapMarkerState.Visible
          }
        )
      },
      onClusterManager = { (it.renderer as? DefaultClusterRenderer<*>)?.minClusterSize = MapMinClusterSize },
      onClusterClick = { cluster ->
        coroutineScope.launch {
          val newCameraLatLng = cluster.position
          val newCameraZoom = if (cameraPositionState.position.zoom < PointsMapScreenMapZoomThreshold) {
            PointsMapScreenMapZoomThreshold + MapClusterClickZoomStep
          } else {
            cameraPositionState.position.zoom + MapClusterClickZoomStep
          }
          val newCamera = CameraUpdateFactory.newLatLngZoom(newCameraLatLng, newCameraZoom)
          cameraPositionState.animate(newCamera)
        }
        return@Clustering true
      },
      onClusterItemClick = { marker ->
        if (state.focusedMarker != null) {
          return@Clustering true
        }

        coroutineScope.launch {
          val newCameraLatLng = LatLng(marker.point.location.latitude, marker.point.location.longitude)
          val newCamera = CameraUpdateFactory.newLatLngZoom(newCameraLatLng, PointsMapMarkerZoomAfterFocus)
          cameraPositionState.animate(newCamera)
          onMarkerFocused(marker)
        }

        return@Clustering true
      }
    )
  }
}

@OptIn(MapsComposeExperimentalApi::class)
@GoogleMapComposable
@Composable
private fun MapCurrentLocationIndicator(latLng: LatLng?) {
  // I know what it looks like, but I haven't found the way to display a single composable marker.
  // Please consider that it's a workaround.

  if (latLng != null) {
    Clustering(
      items = listOf(
        object : ClusterItem {
          override fun getPosition() = latLng
          override fun getTitle() = null
          override fun getSnippet() = null
          override fun getZIndex() = null
        }
      ),
      clusterItemContent = {
        Column(
          verticalArrangement = Arrangement.Center,
          horizontalAlignment = Alignment.CenterHorizontally
        ) {
          Box(
            modifier = Modifier
              .padding(bottom = AppTokens.Spacings.XS)
              .size(MapCurrentLocationIndicatorSize)
              .background(color = MapCurrentLocationIndicatorColor.copy(alpha = 0.5f), shape = CircleShape)
              .border(
                color = MaterialTheme.colorScheme.outline,
                shape = CircleShape,
                width = PointsMapMarkerBackgroundBorderThickness
              )
              .padding(MapCurrentLocationIndicatorPadding)
              .background(color = MapCurrentLocationIndicatorColor, shape = CircleShape)
          )

          Text(
            text = stringResource(R.string.app_you_are_here),
            color = MaterialTheme.colorScheme.onSurface
          )
        }
      },
      onClusterItemClick = { true }
    )
  }
}

@Composable
private fun CameraPositionState.calculateMapMarkerVisible(
  marker: PointsMapMarkerData,
  focusedMarker: PointsMapMarkerData?
) = remember(marker.point.name, focusedMarker?.point?.name) {
  derivedStateOf {
    val focusedMarker = focusedMarker?.point
    if (focusedMarker == null || focusedMarker.name == marker.point.name) {
      return@derivedStateOf true
    }

    val bounds = this.projection?.visibleRegion?.latLngBounds ?: return@derivedStateOf true
    val isNearFocusedMarker = bounds.contains(marker.position)

    return@derivedStateOf !isNearFocusedMarker
  }
}

internal const val PointsMapMarkerZoomAfterFocus = 18f

private const val MapMinClusterSize = 4
private const val MapMinZoom = 5.5f
private val MapPolandBoundaries = LatLngBounds(LatLng(48.5, 14.3), LatLng(54.6, 24.6))
private const val MapClusterClickZoomStep = 2f
private val MapCurrentLocationIndicatorColor = Color(0xFF0047FF)
private val MapCurrentLocationIndicatorSize = 42.dp
private val MapCurrentLocationIndicatorPadding = 6.dp

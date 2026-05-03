package io.github.ilyadreamix.inpostinternshiptask.presentation.points.map.composables

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.systemBars
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import com.google.android.gms.maps.CameraUpdateFactory
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
import io.github.ilyadreamix.inpostinternshiptask.presentation.points.map.data.PointsMapState
import kotlinx.coroutines.launch

@OptIn(MapsComposeExperimentalApi::class)
@Composable
internal fun PointsMap(state: PointsMapState, cameraPositionState: CameraPositionState) {

  val coroutineScope = rememberCoroutineScope()

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
    properties = MapProperties(latLngBoundsForCameraTarget = MapPolandBoundaries, minZoomPreference = MapMinZoom),
    cameraPositionState = cameraPositionState
  ) {
    Clustering(
      items = state.markers,
      clusterContent = { cluster -> PointsMapCluster(data = cluster) },
      clusterItemContent = { marker -> PointsMapMarker(data = marker) },
      onClusterManager = { (it.renderer as? DefaultClusterRenderer<*>)?.minClusterSize = MapMinClusterSize },
      onClusterClick = { cluster ->
        coroutineScope.launch {
          val newCameraLatLng = cluster.position
          val newCameraZoom = cameraPositionState.position.zoom + 2f
          val newCamera = CameraUpdateFactory.newLatLngZoom(newCameraLatLng, newCameraZoom)
          cameraPositionState.animate(newCamera)
        }
        return@Clustering true
      }
    )
  }
}

private const val MapMinClusterSize = 4
private const val MapMinZoom = 5.5f
private val MapPolandBoundaries = LatLngBounds(LatLng(48.5, 14.3), LatLng(54.6, 24.6))

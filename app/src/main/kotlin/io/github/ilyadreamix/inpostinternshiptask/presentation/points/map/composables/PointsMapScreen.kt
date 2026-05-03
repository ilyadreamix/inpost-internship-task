package io.github.ilyadreamix.inpostinternshiptask.presentation.points.map.composables

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.rememberCameraPositionState
import io.github.ilyadreamix.inpostinternshiptask.presentation.points.map.PointsMapViewModel
import io.github.ilyadreamix.inpostinternshiptask.presentation.points.map.data.PointsMapState
import io.github.ilyadreamix.inpostinternshiptask.shared.theme.AppTokens
import org.koin.compose.viewmodel.koinViewModel

@Composable
internal fun PointsMapScreen(viewModel: PointsMapViewModel = koinViewModel()) {

  val state = viewModel.state.collectAsStateWithLifecycle()

  PointsMapScreen(
    state = state.value,
    onCameraIdle = viewModel::onCameraIdle,
    onCameraStartedMoving = viewModel::onCameraStartedMoving
  )
}

@Composable
internal fun PointsMapScreen(
  state: PointsMapState,
  onCameraIdle: (center: LatLng) -> Unit,
  onCameraStartedMoving: () -> Unit,
  modifier: Modifier = Modifier
) {

  val cameraPositionState = rememberCameraPositionState { position = ScreenMapPolandCenter }

  Box(modifier = modifier.fillMaxSize()) {
    PointsMap(
      state = state,
      cameraPositionState = cameraPositionState
    )

    Column(modifier = Modifier.fillMaxWidth()) {
      PointsMapScreenSnackBar(
        content = when {
          cameraPositionState.position.zoom < ScreenMapZoomThreshold -> PointsMapScreenSnackBarContent.ZoomWarning
          state.hasError -> PointsMapScreenSnackBarContent.Error
          else -> null
        },
        modifier = Modifier
          .statusBarsPadding()
          .padding(top = AppTokens.Paddings.SizeScreen)
      )
    }
  }

  LaunchedEffect(cameraPositionState.isMoving) {
    if (cameraPositionState.isMoving) {
      onCameraStartedMoving()
    } else {
      val centerUpdate = cameraPositionState.position.target
      val zoom = cameraPositionState.position.zoom

      if (zoom >= ScreenMapZoomThreshold) {
        onCameraIdle(centerUpdate)
      }
    }
  }
}

private const val ScreenMapZoomThreshold = 10f
private val ScreenMapPolandCenter = CameraPosition.fromLatLngZoom(LatLng(52.0, 19.0), 5.5f)

@Preview
@Composable
private fun ScreenPreview() {
  MaterialTheme {
    PointsMapScreen(
      state = PointsMapState(),
      onCameraIdle = { /* ... */ },
      onCameraStartedMoving = { /* ... */ }
    )
  }
}

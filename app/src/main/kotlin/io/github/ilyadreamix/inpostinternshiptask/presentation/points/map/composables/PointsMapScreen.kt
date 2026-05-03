package io.github.ilyadreamix.inpostinternshiptask.presentation.points.map.composables

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.CameraMoveStartedReason
import com.google.maps.android.compose.rememberCameraPositionState
import io.github.ilyadreamix.inpostinternshiptask.presentation.points.map.PointsMapViewModel
import io.github.ilyadreamix.inpostinternshiptask.presentation.points.map.data.PointsMapState
import io.github.ilyadreamix.inpostinternshiptask.presentation.shared.composables.AppBottomSheetContent
import io.github.ilyadreamix.inpostinternshiptask.presentation.shared.theme.AppTheme
import io.github.ilyadreamix.inpostinternshiptask.presentation.shared.theme.AppTokens
import io.github.ilyadreamix.swissknife.dialogs.bottomsheet.SKBottomSheetState
import io.github.ilyadreamix.swissknife.dialogs.bottomsheet.rememberSKBottomSheetState
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.compose.viewmodel.koinViewModel

@Composable
internal fun PointsMapScreen(viewModel: PointsMapViewModel = koinViewModel()) {

  val state = viewModel.state.collectAsStateWithLifecycle()

  PointsMapScreen(
    state = state.value,
    onCameraIdle = viewModel::onCameraIdle,
    onCameraStartedMoving = viewModel::onCameraStartedMoving,
    onFocusMarker = viewModel::updateFocusedMarker,
    onUnfocusMarker = { viewModel.updateFocusedMarker(null) }
  )
}

@Composable
internal fun PointsMapScreen(
  state: PointsMapState,
  onCameraIdle: (center: LatLng) -> Unit,
  onCameraStartedMoving: () -> Unit,
  onFocusMarker: (marker: PointsMapMarkerData) -> Unit,
  onUnfocusMarker: () -> Unit,
  modifier: Modifier = Modifier
) {
  val coroutineScope = rememberCoroutineScope()
  val bottomSheetState = rememberSKBottomSheetState(visible = false)

  val cameraPositionState = rememberCameraPositionState { position = ScreenMapPolandCenter }

  val mapContentPadding = calculateScreenMapContentPadding(bottomSheetState)

  Box(modifier = modifier.fillMaxSize()) {
    PointsMap(
      state = state,
      cameraPositionState = cameraPositionState,
      onMarkerFocused = onFocusMarker,
      contentPadding = mapContentPadding.value
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

    AppBottomSheetContent(
      state = bottomSheetState,
      onHide = {
        coroutineScope.launch {
          bottomSheetState.hide()
          onUnfocusMarker()
        }
      },
      modifier = Modifier.align(Alignment.BottomCenter)
    ) {
      state.focusedMarker?.let {
        Text(
          text = it.point.name,
          modifier = Modifier.navigationBarsPadding()
        )
      }
    }
  }

  LaunchedEffect(state.focusedMarker) {
    if (state.focusedMarker != null) {
      bottomSheetState.show()
    }
  }

  LaunchedEffect(cameraPositionState.isMoving) {
    if (cameraPositionState.cameraMoveStartedReason != CameraMoveStartedReason.GESTURE) {
      return@LaunchedEffect
    }

    if (cameraPositionState.isMoving) {
      withContext(NonCancellable) { bottomSheetState.hide() } // It must finish no matter what
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

@Composable
private fun calculateScreenMapContentPadding(bottomSheetState: SKBottomSheetState): State<PaddingValues> {
  val density = LocalDensity.current
  val layoutDirection = LocalLayoutDirection.current

  val systemBarsPadding = WindowInsets.systemBars.asPaddingValues()

  return remember {
    derivedStateOf {
      with(density) {
        val sheetOffset = bottomSheetState.animationProgress * bottomSheetState.height
        val systemBottom = systemBarsPadding.calculateBottomPadding().toPx()

        PaddingValues(
          start = AppTokens.Paddings.SizeScreen + systemBarsPadding.calculateStartPadding(layoutDirection),
          top = AppTokens.Paddings.SizeScreen + systemBarsPadding.calculateBottomPadding(),
          end = AppTokens.Paddings.SizeScreen + systemBarsPadding.calculateEndPadding(layoutDirection),
          bottom = AppTokens.Spacings.SM + maxOf(sheetOffset, systemBottom).toDp()
        )
      }
    }
  }
}

private const val ScreenMapZoomThreshold = 10f
private val ScreenMapPolandCenter = CameraPosition.fromLatLngZoom(LatLng(52.0, 19.0), 5.5f)

@Preview
@Composable
private fun ScreenPreview() {
  AppTheme {
    PointsMapScreen(
      state = PointsMapState(),
      onCameraIdle = { /* ... */ },
      onCameraStartedMoving = { /* ... */ },
      onFocusMarker = { /* ... */ },
      onUnfocusMarker = { /* ... */ }
    )
  }
}

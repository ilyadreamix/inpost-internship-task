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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import io.github.ilyadreamix.swissknife.dialogs.bottomsheet.rememberSKBottomSheetState
import kotlinx.coroutines.launch
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun PointsMapScreen(viewModel: PointsMapViewModel = koinViewModel()) {

  val density = LocalDensity.current
  val layoutDirection = LocalLayoutDirection.current

  val state = viewModel.state.collectAsStateWithLifecycle()

  val coroutineScope = rememberCoroutineScope()
  val bottomSheetState = rememberSKBottomSheetState(visible = false)

  val systemBarsPadding = WindowInsets.systemBars.asPaddingValues()
  val bottomPadding = remember {
    derivedStateOf {
      with(density) {
        val sheetOffset = bottomSheetState.animationProgress * bottomSheetState.height
        val systemBottom = systemBarsPadding.calculateBottomPadding().toPx()
        maxOf(sheetOffset, systemBottom).toDp()
      }
    }
  }

  Box(modifier = Modifier.fillMaxSize()) {
    PointsMapScreen(
      state = state.value,
      onCameraIdle = viewModel::onCameraIdle,
      onCameraStartedMoving = {
        coroutineScope.launch {
          bottomSheetState.hide()
          viewModel.onCameraStartedMoving()
        }
      },
      onMarkerFocused = viewModel::updateFocusedMarker,
      contentPadding = PaddingValues(
        start = AppTokens.Paddings.SizeScreen + systemBarsPadding.calculateStartPadding(layoutDirection),
        top = AppTokens.Paddings.SizeScreen + systemBarsPadding.calculateBottomPadding(),
        end = AppTokens.Paddings.SizeScreen + systemBarsPadding.calculateEndPadding(layoutDirection),
        bottom = AppTokens.Spacings.SM + bottomPadding.value
      )
    )

    AppBottomSheetContent(
      state = bottomSheetState,
      onHide = {
        coroutineScope.launch {
          bottomSheetState.hide()
          viewModel.updateFocusedMarker(null)
        }
      },
      modifier = Modifier.align(Alignment.BottomCenter)
    ) {
      state.value.focusedMarker?.let {
        Text(
          text = it.point.name,
          modifier = Modifier.navigationBarsPadding()
        )
      }
    }
  }

  LaunchedEffect(state.value.focusedMarker) {
    if (state.value.focusedMarker != null) {
      bottomSheetState.show()
    }
  }
}

@Composable
internal fun PointsMapScreen(
  state: PointsMapState,
  onCameraIdle: (center: LatLng) -> Unit,
  onCameraStartedMoving: () -> Unit,
  onMarkerFocused: (marker: PointsMapMarkerData) -> Unit,
  contentPadding: PaddingValues,
  modifier: Modifier = Modifier
) {

  val cameraPositionState = rememberCameraPositionState { position = ScreenMapPolandCenter }

  Box(modifier = modifier.fillMaxSize()) {
    PointsMap(
      state = state,
      cameraPositionState = cameraPositionState,
      onMarkerFocused = onMarkerFocused,
      contentPadding = contentPadding
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
    if (cameraPositionState.cameraMoveStartedReason != CameraMoveStartedReason.GESTURE) {
      return@LaunchedEffect
    }

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
  AppTheme {
    PointsMapScreen(
      state = PointsMapState(),
      onCameraIdle = { /* ... */ },
      onCameraStartedMoving = { /* ... */ },
      onMarkerFocused = { /* ... */ },
      contentPadding = PaddingValues.Zero
    )
  }
}

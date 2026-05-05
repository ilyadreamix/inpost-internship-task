package io.github.ilyadreamix.inpostinternshiptask.presentation.points.map.composables

import android.Manifest
import android.annotation.SuppressLint
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.CameraMoveStartedReason
import com.google.maps.android.compose.rememberCameraPositionState
import io.github.ilyadreamix.inpostinternshiptask.R
import io.github.ilyadreamix.inpostinternshiptask.presentation.points.map.PointsMapViewModel
import io.github.ilyadreamix.inpostinternshiptask.presentation.points.map.data.PointsMapState
import io.github.ilyadreamix.inpostinternshiptask.presentation.shared.composables.AppBottomSheetContent
import io.github.ilyadreamix.inpostinternshiptask.presentation.shared.composables.AppInfoAlert
import io.github.ilyadreamix.inpostinternshiptask.presentation.shared.theme.AppTheme
import io.github.ilyadreamix.inpostinternshiptask.presentation.shared.theme.AppTokens
import io.github.ilyadreamix.inpostinternshiptask.shared.extensions.permissionGranted
import io.github.ilyadreamix.swissknife.dialogs.bottomsheet.SKBottomSheetState
import io.github.ilyadreamix.swissknife.dialogs.bottomsheet.rememberSKBottomSheetState
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.compose.viewmodel.koinViewModel

internal val PointsMapScreenOverlayElevation = 6.dp

@Composable
internal fun PointsMapScreen(viewModel: PointsMapViewModel = koinViewModel()) {

  val state = viewModel.state.collectAsStateWithLifecycle()

  PointsMapScreen(
    state = state.value,
    onCameraIdle = viewModel::onCameraIdle,
    onCameraStartedMoving = viewModel::onCameraStartedMoving,
    onFocusMarker = viewModel::updateFocusedMarker,
    onUpdateLocationDeniedError = viewModel::updateLocationDeniedError,
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
  onUpdateLocationDeniedError: (value: Boolean) -> Unit,
  modifier: Modifier = Modifier
) {
  val context = LocalContext.current
  val density = LocalDensity.current

  val statusBarInsets = WindowInsets.statusBars.asPaddingValues()

  val coroutineScope = rememberCoroutineScope()
  val bottomSheetState = rememberSKBottomSheetState(visible = false)
  val cameraPositionState = rememberCameraPositionState { position = ScreenMapPolandCenter }

  val currentLocation = rememberSaveable { mutableStateOf<LatLng?>(null) }
  val requestPermissionAndFocusCurrentLocation = rememberLauncherForActivityResult(
    contract = ActivityResultContracts.RequestPermission(),
    onResult = {
      val hasFinePermission = context.permissionGranted(Manifest.permission.ACCESS_FINE_LOCATION)
      val hasCoarsePermission = context.permissionGranted(Manifest.permission.ACCESS_COARSE_LOCATION)

      if (hasFinePermission || hasCoarsePermission) {
        @SuppressLint("MissingPermission")
        LocationServices.getFusedLocationProviderClient(context).lastLocation.addOnSuccessListener { location ->
          if (location != null) {
            val newLatLng = LatLng(location.latitude, location.longitude)
            val newCamera = CameraUpdateFactory.newLatLngZoom(newLatLng, PointsMapMarkerZoomAfterFocus)

            coroutineScope.launch { cameraPositionState.animate(newCamera) }
            currentLocation.value = newLatLng
          }
        }
      } else {
        onUpdateLocationDeniedError(true)
      }
    }
  )
  val requestLocationPermissionAndFocusCurrentLocation: () -> Unit = {
    requestPermissionAndFocusCurrentLocation.launch(Manifest.permission.ACCESS_FINE_LOCATION)
  }

  val snackBarContent = when {
    state.focusedMarker != null -> null
    cameraPositionState.position.zoom < PointsMapScreenMapZoomThreshold -> PointsMapScreenSnackBarContent.ZoomWarning
    state.hasError -> PointsMapScreenSnackBarContent.Error
    else -> null
  }

  val mapContentPadding = calculateScreenMapContentPadding(bottomSheetState)

  val dismissSheetAndUnfocus: () -> Unit = {
    coroutineScope.launch {
      bottomSheetState.hide()
      onUnfocusMarker()
    }
  }

  Box(modifier = modifier.fillMaxSize()) {
    PointsMap(
      state = state,
      cameraPositionState = cameraPositionState,
      onMarkerFocused = onFocusMarker,
      contentPadding = mapContentPadding.value,
      disableGestures = state.focusedMarker != null,
      currentLocation = currentLocation.value
    )

    PointsMapScreenSnackBar(
      content = snackBarContent,
      modifier = Modifier.statusBarsPadding()
    )

    PointsControlButton(
      visible = state.focusedMarker != null,
      onClick = dismissSheetAndUnfocus,
      text = { Text(text = stringResource(R.string.app_back_to_map)) },
      icon = { Icon(painter = painterResource(R.drawable.mic_arrow_back), contentDescription = null) },
      snapAnimationToStart = true,
      modifier = Modifier
        .statusBarsPadding()
        .padding(start = AppTokens.Paddings.SizeScreen)
    )

    PointsControlButton(
      visible = state.focusedMarker == null,
      onClick = requestLocationPermissionAndFocusCurrentLocation,
      text = { Text(text = stringResource(R.string.app_show_me)) },
      icon = { Icon(painter = painterResource(R.drawable.mic_location_on), contentDescription = null) },
      snapAnimationToStart = false,
      modifier = Modifier
        .align(Alignment.BottomEnd)
        .statusBarsPadding()
        .padding(end = AppTokens.Paddings.SizeScreen, bottom = AppTokens.Paddings.SizeScreen)
    )

    AppBottomSheetContent(
      state = bottomSheetState,
      onHide = { dismissSheetAndUnfocus() },
      modifier = Modifier.align(Alignment.BottomCenter)
    ) {
      state.focusedMarker?.let {
        PointsMapSheetContent(point = it.point)
      }
    }
  }

  AppInfoAlert(
    visible = state.locationDeniedError,
    onHide = { onUpdateLocationDeniedError(false) },
    title = { Text(text = stringResource(R.string.app_location_permission_denied)) },
    text = { Text(text = stringResource(R.string.app_location_permission_denied_text)) },
    hideButton = {
      Button(
        onClick = { onUpdateLocationDeniedError(false) },
        content = { Text(text = stringResource(R.string.app_got_it)) }
      )
    }
  )

  LaunchedEffect(state.focusedMarker) {
    if (state.focusedMarker != null) {
      bottomSheetState.show()
    }
  }

  LaunchedEffect(Unit) {

    var lastProgress = 0f

    snapshotFlow { bottomSheetState.animationProgress }
      .collect { progress ->
        val delta = progress - lastProgress
        val focusedPaddingPx = bottomSheetState.height / 2 -
          with(density) { statusBarInsets.calculateBottomPadding().toPx() } -
          with(density) { AppTokens.Paddings.SizeScreen.toPx() } -
          with(density) { ScreenMapFocusedExtraPadding.toPx() }

        cameraPositionState.move(CameraUpdateFactory.scrollBy(0f, focusedPaddingPx * delta))
        lastProgress = progress
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

      if (zoom >= PointsMapScreenMapZoomThreshold) {
        onCameraIdle(centerUpdate)
      }
    }
  }

  BackHandler(enabled = state.focusedMarker != null, onBack = dismissSheetAndUnfocus)
}

const val PointsMapScreenMapZoomThreshold = 10f
private val ScreenMapPolandCenter = CameraPosition.fromLatLngZoom(LatLng(52.0, 19.0), 5.5f)
private val ScreenMapFocusedExtraPadding = 36.dp // Approximate height of exit button

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

@Preview
@Composable
private fun ScreenPreview() {
  AppTheme {
    PointsMapScreen(
      state = PointsMapState(),
      onCameraIdle = { /* ... */ },
      onCameraStartedMoving = { /* ... */ },
      onFocusMarker = { /* ... */ },
      onUnfocusMarker = { /* ... */ },
      onUpdateLocationDeniedError = { /* ... */ }
    )
  }
}

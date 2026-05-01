package io.github.ilyadreamix.inpostinternshiptask

import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.systemBars
import androidx.compose.ui.Modifier
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.maps.android.compose.ComposeMapColorScheme
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.rememberCameraPositionState

internal class InPostInternshipTaskActivity : AppCompatActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    val isDarkTheme = (resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) ==
      Configuration.UI_MODE_NIGHT_YES

    WindowCompat.enableEdgeToEdge(window)
    WindowInsetsControllerCompat(window, window.decorView).apply {
      isAppearanceLightStatusBars = !isDarkTheme
      isAppearanceLightNavigationBars = !isDarkTheme
    }

    setContent {

      val cameraPositionState = rememberCameraPositionState { position = PolandCenter }

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
      )
    }
  }

  companion object {
    private const val MinZoom = 5.5f
    private val PolandBoundaries = LatLngBounds(LatLng(48.5, 14.3), LatLng(54.6, 24.6))
    private val PolandCenter = CameraPosition.fromLatLngZoom(LatLng(52.0, 19.0), 5.5f)
  }
}

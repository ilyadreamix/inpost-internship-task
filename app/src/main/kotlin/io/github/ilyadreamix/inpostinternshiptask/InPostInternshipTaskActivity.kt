package io.github.ilyadreamix.inpostinternshiptask

import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsControllerCompat
import io.github.ilyadreamix.inpostinternshiptask.presentation.points.map.composables.PointsMapScreen
import io.github.ilyadreamix.inpostinternshiptask.shared.theme.AppTheme

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
      AppTheme {
        PointsMapScreen()
      }
    }
  }
}

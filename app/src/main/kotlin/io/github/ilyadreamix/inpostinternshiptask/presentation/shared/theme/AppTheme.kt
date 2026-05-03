package io.github.ilyadreamix.inpostinternshiptask.presentation.shared.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable

@Composable
internal fun AppTheme(content: @Composable () -> Unit) {
  MaterialTheme(
    colorScheme = if (isSystemInDarkTheme()) DarkColorScheme else LightColorScheme,
    content = content
  )
}

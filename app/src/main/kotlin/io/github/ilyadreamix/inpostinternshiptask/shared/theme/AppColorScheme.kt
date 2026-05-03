package io.github.ilyadreamix.inpostinternshiptask.shared.theme

import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color

private val Primary = Color(0xFFFCCC06)
private val OnPrimary = Color(0xFF353535)
private val Secondary = Color(0xFFFF2B7E)
private val OnSecondary = Color(0xFFFFFFFF)

private val LightSurface = Color(0xFFF4F4F4)
private val LightOnSurface = Color(0xFF000000)
private val LightBackground = Color(0xFFF4F4F4)
private val LightOnBackground = Color(0xFF000000)
private val LightOutline = Color(0xFF000000)

private val DarkSurface = Color(0xFF1D1D1D)
private val DarkOnSurface = Color(0xFFFFFFFF)
private val DarkBackground = Color(0xFF1D1D1D)
private val DarkOnBackground = Color(0xFFFFFFFF)
private val DarkOutline = Color(0xFFFFFFFF)

internal val LightColorScheme = lightColorScheme().copy(
  primary = Primary,
  onPrimary = OnPrimary,
  secondary = Secondary,
  onSecondary = OnSecondary,
  surface = LightSurface,
  onSurface = LightOnSurface,
  background = LightBackground,
  onBackground = LightOnBackground,
  outline = LightOutline
)

internal val DarkColorScheme = darkColorScheme().copy(
  primary = Primary,
  onPrimary = OnPrimary,
  secondary = Secondary,
  onSecondary = OnSecondary,
  surface = DarkSurface,
  onSurface = DarkOnSurface,
  background = DarkBackground,
  onBackground = DarkOnBackground,
  outline = DarkOutline
)



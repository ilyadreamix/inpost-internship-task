package io.github.ilyadreamix.inpostinternshiptask.presentation.shared.shapes

import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp

@Suppress("FunctionName")
internal fun RoundedRhombusPath(radius: Dp, density: Density, size: Size) = Path().apply {

  val radiusPx = with(density) { radius.toPx() }

  moveTo(size.width / 2 + radiusPx, radiusPx)

  lineTo(size.width - radiusPx, size.height / 2 - radiusPx)
  quadraticTo(size.width, size.height / 2, size.width - radiusPx, size.height / 2 + radiusPx)

  lineTo(size.width / 2 + radiusPx, size.height - radiusPx)
  quadraticTo(size.width / 2, size.height, size.width / 2 - radiusPx, size.height - radiusPx)

  lineTo(radiusPx, size.height / 2 + radiusPx)
  quadraticTo(0f, size.height / 2, radiusPx, size.height / 2 - radiusPx)

  lineTo(size.width / 2 - radiusPx, radiusPx)
  quadraticTo(size.width / 2, 0f, size.width / 2 + radiusPx, radiusPx)

  close()
}

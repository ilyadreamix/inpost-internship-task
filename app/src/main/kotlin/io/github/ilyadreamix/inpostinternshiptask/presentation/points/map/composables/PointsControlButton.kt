package io.github.ilyadreamix.inpostinternshiptask.presentation.points.map.composables

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.TransformOrigin
import io.github.ilyadreamix.inpostinternshiptask.presentation.shared.theme.AppTokens

@Composable
internal fun PointsControlButton(
  visible: Boolean,
  onClick: () -> Unit,
  text: @Composable () -> Unit,
  icon: @Composable () -> Unit,
  snapAnimationToStart: Boolean,
  modifier: Modifier = Modifier
) {
  val animationSign = if (snapAnimationToStart) -1 else 1
  val animationPivotX = if (snapAnimationToStart) 0f else 1f

  AnimatedVisibility(
    visible = visible,
    enter = scaleIn(transformOrigin = TransformOrigin(animationPivotX,  0.5f)) +
      slideInHorizontally { it * animationSign },
    exit = scaleOut(transformOrigin = TransformOrigin(animationPivotX,  0.5f)) +
      slideOutHorizontally { it * animationSign },
    modifier = modifier
  ) {
    Surface(
      modifier = Modifier.padding(PointsMapScreenOverlayElevation),
      color = MaterialTheme.colorScheme.surface,
      shadowElevation = PointsMapScreenOverlayElevation,
      shape = RoundedCornerShape(50),
      onClick = onClick
    ) {
      Row(
        modifier = Modifier.padding(horizontal = AppTokens.Spacings.MS, vertical = AppTokens.Spacings.SM),
        horizontalArrangement = Arrangement.spacedBy(AppTokens.Spacings.SM),
        verticalAlignment = Alignment.CenterVertically
      ) {
        icon()
        text()
      }
    }
  }
}

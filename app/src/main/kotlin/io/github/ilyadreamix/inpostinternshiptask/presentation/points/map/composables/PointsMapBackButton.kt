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
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import io.github.ilyadreamix.inpostinternshiptask.R
import io.github.ilyadreamix.inpostinternshiptask.presentation.shared.theme.AppTokens

@Composable
internal fun PointsMapBackButton(
  visible: Boolean,
  onClick: () -> Unit,
  modifier: Modifier = Modifier
) {
  AnimatedVisibility(
    visible = visible,
    enter = scaleIn(transformOrigin = TransformOrigin(0f,  0.5f)) + slideInHorizontally { -it },
    exit = scaleOut(transformOrigin = TransformOrigin(0f,  0.5f)) + slideOutHorizontally { -it }
  ) {
    Surface(
      modifier = modifier.padding(bottom = PointsMapScreenOverlayElevation),
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
        Icon(painter = painterResource(R.drawable.mic_arrow_back), contentDescription = null)
        Text(text = stringResource(R.string.app_back_to_map))
      }
    }
  }
}

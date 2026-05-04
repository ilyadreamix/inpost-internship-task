package io.github.ilyadreamix.inpostinternshiptask.presentation.points.map.composables

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
internal fun PointsMapScreenSnackBar(content: PointsMapScreenSnackBarContent?, modifier: Modifier = Modifier) {
  SnackBarAnimator(
    content = content,
    icon = {
      Icon(
        painter = when (it) {
          PointsMapScreenSnackBarContent.ZoomWarning -> painterResource(R.drawable.mic_pinch)
          PointsMapScreenSnackBarContent.Error -> painterResource(R.drawable.mic_sentiment_dissatisfied)
        },
        contentDescription = null
      )
    },
    text = {
      Text(
        text = when (it) {
          PointsMapScreenSnackBarContent.ZoomWarning -> stringResource(R.string.app_map_distance_too_far)
          PointsMapScreenSnackBarContent.Error -> stringResource(R.string.app_map_update_error)
        }
      )
    },
    modifier = modifier
  )
}

internal enum class PointsMapScreenSnackBarContent { ZoomWarning, Error; }

@Composable
private fun SnackBarAnimator(
  content: PointsMapScreenSnackBarContent?,
  icon: @Composable (PointsMapScreenSnackBarContent) -> Unit,
  text: @Composable (PointsMapScreenSnackBarContent) -> Unit,
  modifier: Modifier = Modifier
) {
  AnimatedContent(
    targetState = content,
    contentKey = { it == null },
    transitionSpec = {
      slideInVertically { -it } +
        scaleIn(transformOrigin = TransformOrigin(0.5f, 0f)) togetherWith
        slideOutVertically { -it } +
        scaleOut(transformOrigin = TransformOrigin(0.5f, 0f))
    }
  ) { contentOrNull ->
    if (contentOrNull == null) {
      Spacer(modifier = Modifier.fillMaxWidth())
    } else {
      SnackBar(
        icon = {
          AnimatedContent(
            targetState = contentOrNull,
            transitionSpec = { fadeIn() togetherWith fadeOut() },
            content = { icon(it) }
          )
        },
        text = {
          AnimatedContent(
            targetState = contentOrNull,
            transitionSpec = { fadeIn() togetherWith fadeOut() },
            content = { text(it) }
          )
        },
        modifier = modifier
      )
    }
  }
}

@Composable
private fun SnackBar(
  icon: @Composable () -> Unit,
  text: @Composable () -> Unit,
  modifier: Modifier = Modifier
) {
  Surface(
    modifier = modifier
      .padding(bottom = PointsMapScreenOverlayElevation)
      .fillMaxWidth()
      .padding(horizontal = AppTokens.Paddings.SizeScreen),
    color = MaterialTheme.colorScheme.surface,
    shadowElevation = PointsMapScreenOverlayElevation,
    shape = AppTokens.RoundedCornerShapes.MS
  ) {
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(all = AppTokens.Spacings.MS),
      horizontalArrangement = Arrangement.spacedBy(AppTokens.Spacings.MS),
      verticalAlignment = Alignment.CenterVertically
    ) {
      icon()
      text()
    }
  }
}

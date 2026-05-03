package io.github.ilyadreamix.inpostinternshiptask.presentation.points.map.composables

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
          PointsMapScreenSnackBarContent.ZoomWarning -> stringResource(R.string.app_points_map_distance_warning)
          PointsMapScreenSnackBarContent.Error -> stringResource(R.string.app_points_map_update_error)
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
      if (initialState == null || targetState == null) {
        fadeIn() + slideInVertically { -it } togetherWith fadeOut() + slideOutVertically { -it }
      } else {
        fadeIn() togetherWith fadeOut()
      }
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
  Row(
    modifier = modifier
      .fillMaxWidth()
      .padding(horizontal = AppTokens.Paddings.SizeScreen)
      .background(color = SnackBarColor, shape = AppTokens.RoundedCornerShapes.MS)
      .padding(all = AppTokens.Spacings.MS),
    horizontalArrangement = Arrangement.spacedBy(AppTokens.Spacings.MS),
    verticalAlignment = Alignment.CenterVertically
  ) {
    CompositionLocalProvider(LocalContentColor provides SnackBarContentColor) {
      icon()
      text()
    }
  }
}

private val SnackBarColor = Color.White
private val SnackBarContentColor = Color.Black

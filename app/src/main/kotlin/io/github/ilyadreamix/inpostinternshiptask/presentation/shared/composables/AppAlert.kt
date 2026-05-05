package io.github.ilyadreamix.inpostinternshiptask.presentation.shared.composables

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LocalMinimumInteractiveComponentSize
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.lerp
import io.github.ilyadreamix.swissknife.dialogs.alert.SKAlert

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun AppInfoAlert(
  visible: Boolean,
  onHide: () -> Unit,
  title: @Composable () -> Unit,
  text: @Composable () -> Unit,
  hideButton: @Composable () -> Unit
) {
  SKAlert(
    visible = visible,
    onHide = { onHide() },
  ) { animationProgress ->
    Surface(
      modifier = Modifier
        .graphicsLayer {
          alpha = animationProgress
          scaleX = lerp(0.85f, 1f, animationProgress)
          scaleY = lerp(0.85f, 1f, animationProgress)
        }
        .padding(24.dp)
        .widthIn(min = 280.dp, max = 560.dp),
      color = MaterialTheme.colorScheme.surfaceContainerLow,
      shadowElevation = 6.dp,
      shape = MaterialTheme.shapes.extraLarge
    ) {
      Column(modifier = Modifier.padding(24.dp)) {
        Box(modifier = Modifier.padding(bottom = 16.dp)) {
          CompositionLocalProvider(
            value = LocalTextStyle provides MaterialTheme.typography.headlineSmall,
            content = { title() }
          )
        }

        Box(modifier = Modifier.padding(bottom = 24.dp)) {
          CompositionLocalProvider(
            value = LocalTextStyle provides MaterialTheme.typography.bodyMedium,
            content = { text() }
          )
        }

        Box(modifier = Modifier.align(Alignment.End)) {
          CompositionLocalProvider(
            value = LocalMinimumInteractiveComponentSize provides 0.dp,
            content = { hideButton() }
          )
        }
      }
    }
  }
}

package io.github.ilyadreamix.inpostinternshiptask.presentation.shared.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.github.ilyadreamix.swissknife.core.SKInsets
import io.github.ilyadreamix.swissknife.dialogs.bottomsheet.SKBottomSheetContainer
import io.github.ilyadreamix.swissknife.dialogs.bottomsheet.SKBottomSheetContent
import io.github.ilyadreamix.swissknife.dialogs.bottomsheet.SKBottomSheetHideOptions
import io.github.ilyadreamix.swissknife.dialogs.bottomsheet.SKBottomSheetHideReason
import io.github.ilyadreamix.swissknife.dialogs.bottomsheet.SKBottomSheetState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun AppBottomSheetContent(
  state: SKBottomSheetState,
  onHide: (SKBottomSheetHideReason) -> Unit,
  modifier: Modifier = Modifier,
  content: @Composable () -> Unit,
) {
  SKBottomSheetContent(
    content = {
      CompositionLocalProvider(LocalContentColor provides MaterialTheme.colorScheme.onSurface) {
        Column(modifier = Modifier.fillMaxWidth()) {
          Box(
            modifier = Modifier
              .align(Alignment.CenterHorizontally)
              .padding(vertical = 22.dp)
              .height(4.dp)
              .width(32.dp)
              .background(
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.4f),
                shape = RoundedCornerShape(50)
              )
          )

          content()
        }
      }
    },
    state = state,
    container = SKBottomSheetContainer(
      maxWidth = 640.dp,
      color = MaterialTheme.colorScheme.surfaceContainerLow,
      shape = MaterialTheme.shapes.extraLarge.copy(
        bottomStart = CornerSize(0.dp),
        bottomEnd = CornerSize(0.dp),
      ),
      elevation = 1.dp
    ),
    hideOptions = SKBottomSheetHideOptions(),
    insets = SKInsets(WindowInsets.safeDrawing),
    animationProgress = state.animationProgress,
    onHide = onHide,
    nestedScrollConnection = null,
    modifier = modifier
  )
}

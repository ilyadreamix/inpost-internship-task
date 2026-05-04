package io.github.ilyadreamix.inpostinternshiptask.presentation.points.map.composables

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.capitalize
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImagePainter
import coil3.compose.rememberAsyncImagePainter
import io.github.ilyadreamix.inpostinternshiptask.R
import io.github.ilyadreamix.inpostinternshiptask.domain.points.models.PickupPointModel
import io.github.ilyadreamix.inpostinternshiptask.domain.points.models.isOpen
import io.github.ilyadreamix.inpostinternshiptask.presentation.shared.theme.AppTheme
import io.github.ilyadreamix.inpostinternshiptask.presentation.shared.theme.AppTokens

@Composable
internal fun PointsMapSheetContent(point: PickupPointModel, modifier: Modifier = Modifier) {
  Column(
    modifier = modifier
      .fillMaxWidth()
      .navigationBarsPadding()
      .padding(
        start = AppTokens.Paddings.SizeScreen,
        end = AppTokens.Paddings.SizeScreen,
        bottom = AppTokens.Paddings.SizeScreen
      )
  ) {
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .height(IntrinsicSize.Min),
      verticalAlignment = Alignment.CenterVertically
    ) {
      Column(modifier = Modifier.weight(1f)) {
        Text(
          text = point.name,
          style = MaterialTheme.typography.titleLarge,
          fontWeight = FontWeight.Bold
        )

        Text(
          text = point.addressDetails.let {
            if (it.buildingNumber == null) {
              stringResource(
                R.string.app_address_no_number,
                it.street.capitalize(Locale.current),
                it.city,
                it.province
              )
            } else {
              stringResource(
                R.string.app_address,
                it.street.capitalize(Locale.current),
                it.buildingNumber,
                it.city,
                it.province
              )
            }
          },
          style = MaterialTheme.typography.bodyMedium,
          lineHeight = LocalTextStyle.current.lineHeight * 0.8f
        )
      }

      ContentImage(
        url = point.imageUrl,
        modifier = Modifier.padding(start = AppTokens.Spacings.MD)
      )
    }

    HorizontalDivider(modifier = Modifier.padding(vertical = AppTokens.Spacings.MD))

    ContentOperatingHours(point)

    HorizontalDivider(modifier = Modifier.padding(vertical = AppTokens.Spacings.MD))
  }
}

@Composable
private fun ContentImage(url: String, modifier: Modifier = Modifier) {
  val painter = rememberAsyncImagePainter(url)
  val state = painter.state.collectAsStateWithLifecycle()

  when (val currentState = state.value) {
    is AsyncImagePainter.State.Empty,
    is AsyncImagePainter.State.Loading,
    is AsyncImagePainter.State.Error ->
      Box(
        modifier = modifier
          .size(ContentImageHeight)
          .background(
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f),
            shape = AppTokens.RoundedCornerShapes.PS
          ),
        contentAlignment = Alignment.Center
      ) {
        if (currentState is AsyncImagePainter.State.Error) {
          Icon(
            painter = painterResource(R.drawable.mic_broken_image),
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurface
          )
        }
      }

    is AsyncImagePainter.State.Success -> {
      val intrinsicSize = currentState.painter.intrinsicSize
      val aspectRatio = if (intrinsicSize.height > 0f) intrinsicSize.width / intrinsicSize.height else 1f
      val targetWidth = (ContentImageHeight * aspectRatio).coerceAtMost(ContentImageMaxWidth)

      Image(
        painter = currentState.painter,
        contentDescription = null,
        modifier = modifier
          .size(width = targetWidth, height = ContentImageHeight)
          .clip(AppTokens.RoundedCornerShapes.PS)
          .border(
            width = ContentImageBorderWidth,
            color = MaterialTheme.colorScheme.primary,
            shape = AppTokens.RoundedCornerShapes.PS
          )
          .background(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f)),
        contentScale = ContentScale.Crop,
      )
    }
  }
}

@Composable
private fun ContentOperatingHours(point: PickupPointModel, modifier: Modifier = Modifier) {

  val isOpen = point.isOpen()

  Column(
    modifier = modifier.fillMaxWidth(),
    verticalArrangement = Arrangement.spacedBy(AppTokens.Spacings.SM)
  ) {
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {

      Text(text = stringResource(R.string.app_operating_hours), fontWeight = FontWeight.Bold)

      ContentChip(
        text = {
          Text(
            text = stringResource(if (isOpen) R.string.app_open else R.string.app_closed).uppercase(),
            fontWeight = FontWeight.Bold
          )
        },
        color = if (isOpen) ContentChipClosedColor else ContentChipOpenColor,
        contentColor = Color.White
      )
    }

    if (point.locationIsAvailable24Hours) {
      Text(
        text = stringResource(R.string.app_operating_hours_24_7),
        style = MaterialTheme.typography.bodyMedium
      )
    }
  }
}

private val ContentChipClosedColor = Color(0xFF15A01C)
private val ContentChipOpenColor = Color(0xFFDC0000)

@Composable
private fun ContentChip(
  text: @Composable () -> Unit,
  color: Color,
  contentColor: Color,
  modifier: Modifier = Modifier,
  icon: (@Composable () -> Unit)? = null,
) {
  Row(
    modifier = modifier
      .clip(AppTokens.RoundedCornerShapes.PS)
      .background(color)
      .padding(
        horizontal = AppTokens.RoundedCornerShapes.SizePS,
        vertical = AppTokens.RoundedCornerShapes.SizePS / 2
      ),
    verticalAlignment = Alignment.CenterVertically
  ) {
    CompositionLocalProvider(LocalContentColor provides contentColor) {
      icon?.invoke()
      text()
    }
  }
}

@Preview
@Composable
private fun ContentChipPreview() {
  AppTheme {
    ContentChip(
      text = { Text(text = "Open") },
      color = Color.Green,
      contentColor = Color.Black
    )
  }
}

private val ContentImageHeight = 64.dp
private val ContentImageMaxWidth = 96.dp
private val ContentImageBorderWidth = 1.dp

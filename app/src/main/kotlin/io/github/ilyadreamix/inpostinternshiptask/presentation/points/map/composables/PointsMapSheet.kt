package io.github.ilyadreamix.inpostinternshiptask.presentation.points.map.composables

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.runtime.ReadOnlyComposable
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
import io.github.ilyadreamix.inpostinternshiptask.presentation.shared.theme.AppColorEasyAccess
import io.github.ilyadreamix.inpostinternshiptask.presentation.shared.theme.AppColorOnEasyAccess
import io.github.ilyadreamix.inpostinternshiptask.presentation.shared.theme.AppTheme
import io.github.ilyadreamix.inpostinternshiptask.presentation.shared.theme.AppTokens

@Composable
internal fun PointsMapSheetContent(point: PickupPointModel, modifier: Modifier = Modifier) {
  Column(
    modifier = modifier
      .fillMaxWidth()
      .navigationBarsPadding()
      .padding(horizontal = AppTokens.Paddings.SizeScreen)
  ) {
    Row(
      modifier = Modifier.fillMaxWidth(),
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
          style = MaterialTheme.typography.bodyMedium
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

    ContentLocation(point)

    if (point.easyAccess) {

      HorizontalDivider(modifier = Modifier.padding(vertical = AppTokens.Spacings.MD))

      ContentChip(
        text = { Text(text = stringResource(R.string.app_easy_access)) },
        color = AppColorEasyAccess,
        contentColor = AppColorOnEasyAccess,
        icon = { Icon(painter = painterResource(R.drawable.mic_accessible), contentDescription = null) }
      )
    }
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
        color = if (isOpen) ContentChipOpenColor else ContentChipClosedColor,
        contentColor = Color.White
      )
    }

    Text(
      text = point.formatContentOperatingHours(),
      style = MaterialTheme.typography.bodyMedium
    )
  }
}

@ReadOnlyComposable
@Composable
private fun PickupPointModel.formatContentOperatingHours(): String {
  if (locationIsAvailable24Hours) {
    return stringResource(R.string.app_operating_hours_24_7)
  }

  if (operatingHours == null) {
    return openingHours
  }

  val days = listOf(
    stringResource(R.string.app_weekday_monday) to operatingHours.monday,
    stringResource(R.string.app_weekday_tuesday) to operatingHours.tuesday,
    stringResource(R.string.app_weekday_wednesday) to operatingHours.wednesday,
    stringResource(R.string.app_weekday_thursday) to operatingHours.thursday,
    stringResource(R.string.app_weekday_friday) to operatingHours.friday,
    stringResource(R.string.app_weekday_saturday) to operatingHours.saturday,
    stringResource(R.string.app_weekday_sunday) to operatingHours.sunday
  )

  return days
    .filterNot { (_, intervals) -> intervals.isEmpty() }
    .joinToString("\n") { (name, intervals) ->
      "$name " + intervals.joinToString(", ") { interval ->
        val startHours = interval.start / 60
        val startMinutes = interval.start % 60

        val endHours = interval.end / 60
        val endMinutes = interval.end % 60

        return@joinToString "%02d:%02d–%02d:%02d".format(startHours, startMinutes, endHours, endMinutes)
      }
    }
}

@Composable
private fun ContentLocation(point: PickupPointModel, modifier: Modifier = Modifier) {
  val locationType = stringResource(
    when (point.locationType) {
      PickupPointModel.LocationType.Outdoor -> R.string.app_location_outdoor
      PickupPointModel.LocationType.Indoor -> R.string.app_location_indoor
    }
  )

  Column(
    modifier = modifier.fillMaxWidth(),
    verticalArrangement = Arrangement.spacedBy(AppTokens.Spacings.SM)
  ) {
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {

      Text(text = stringResource(R.string.app_location), fontWeight = FontWeight.Bold)

      ContentChip(
        text = { Text(text = locationType) },
        color = MaterialTheme.colorScheme.primary,
        contentColor = MaterialTheme.colorScheme.onPrimary,
        icon = {
          Icon(
            painter = when (point.locationType) {
              PickupPointModel.LocationType.Outdoor -> painterResource(R.drawable.mic_nature)
              PickupPointModel.LocationType.Indoor -> painterResource(R.drawable.mic_apartment)
            },
            contentDescription = null
          )
        }
      )
    }

    point.description?.let { description ->
      Text(
        text = description.capitalize(Locale.current),
        style = MaterialTheme.typography.bodyMedium
      )
    }
  }
}

private val ContentChipOpenColor = Color(0xFF15A01C)
private val ContentChipClosedColor = Color(0xFFDC0000)

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
      .height(ContentChipHeight)
      .clip(AppTokens.RoundedCornerShapes.PS)
      .background(color)
      .padding(horizontal = AppTokens.RoundedCornerShapes.SizePS),
    verticalAlignment = Alignment.CenterVertically,
    horizontalArrangement = Arrangement.spacedBy(AppTokens.Spacings.XS)
  ) {
    CompositionLocalProvider(LocalContentColor provides contentColor) {
      icon?.invoke()
      CompositionLocalProvider(
        value = LocalTextStyle provides MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
        content = text
      )
    }
  }
}

private val ContentChipHeight = 32.dp

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

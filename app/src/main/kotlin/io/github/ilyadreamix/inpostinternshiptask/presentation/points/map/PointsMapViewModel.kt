package io.github.ilyadreamix.inpostinternshiptask.presentation.points.map

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import io.github.ilyadreamix.inpostinternshiptask.domain.points.models.PickupPointModel
import io.github.ilyadreamix.inpostinternshiptask.domain.points.options.ListPickupPointsOption
import io.github.ilyadreamix.inpostinternshiptask.domain.points.usecases.ListPickupPointsUseCase
import io.github.ilyadreamix.inpostinternshiptask.presentation.points.map.data.PointsMapState
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.milliseconds

internal class PointsMapViewModel(private val listPointsUC: ListPickupPointsUseCase) : ViewModel() {

  private val _state = MutableStateFlow(PointsMapState())
  val state = _state.asStateFlow()

  private val cache = LinkedHashMap<String, PointsMapState.Point>(100, 0.7f)
  private var cameraJob: Job? = null

  fun onCameraIdle(centerUpdate: LatLng, zoom: Float) {
    val zoomTooSmall = zoom < ZoomThreshold
    _state.update { it.copy(isZoomWarningVisible = zoomTooSmall) }
    if (zoomTooSmall) {
      return
    }

    cameraJob?.cancel()
    cameraJob = viewModelScope.launch {
      delay(CameraJobDebounce)
      fetchPoints(centerUpdate)
    }
  }

  private suspend fun fetchPoints(center: LatLng) {
    try {
      val relativePoint = PickupPointModel.Location(center.latitude, center.longitude)
      val points = listPointsUC(ListPickupPointsOption(relativePoint = relativePoint)).items

      points.forEach { point ->
        cache[point.name] = PointsMapState.Point(point)
      }

      _state.update { it.copy(isZoomWarningVisible = false, points = cache.values.toList()) }
    } catch (error: Throwable) {
      Log.e(Tag, "fetchPoints: Unexpected error\n${error.message}")
    }
  }

  companion object {
    private const val Tag = "PointsMapViewModel"
    private val CameraJobDebounce = 500.milliseconds
    private const val ZoomThreshold = 9f
  }
}

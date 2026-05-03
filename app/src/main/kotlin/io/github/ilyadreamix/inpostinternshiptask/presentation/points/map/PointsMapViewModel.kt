package io.github.ilyadreamix.inpostinternshiptask.presentation.points.map

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLng
import io.github.ilyadreamix.inpostinternshiptask.domain.points.models.PickupPointModel
import io.github.ilyadreamix.inpostinternshiptask.domain.points.options.ListPickupPointsOption
import io.github.ilyadreamix.inpostinternshiptask.domain.points.usecases.ListPickupPointsUseCase
import io.github.ilyadreamix.inpostinternshiptask.presentation.points.map.composables.PointsMapMarkerData
import io.github.ilyadreamix.inpostinternshiptask.presentation.points.map.data.PointsMapState
import kotlinx.coroutines.CancellationException
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

  private val cache = LinkedHashMap<String, PointsMapMarkerData>() // No mutex: previous job is canceled before new one
                                                                   // starts, so cache writes never overlap
  private var cameraJob: Job? = null

  fun onCameraIdle(center: LatLng) {
    cameraJob?.cancel()
    cameraJob = viewModelScope.launch {
      delay(CameraJobDebounce)
      fetchPoints(center)
    }
  }

  private suspend fun fetchPoints(center: LatLng) {
    try {
      val relativePoint = PickupPointModel.Location(center.latitude, center.longitude)
      val points = listPointsUC(ListPickupPointsOption(relativePoint = relativePoint)).items

      points.forEach { point -> cache[point.name] = PointsMapMarkerData(point) }
      _state.update { it.copy(markers = cache.values.toList(), hasError = false) }
    } catch (error: Throwable) {
      if (error is CancellationException) {
        return
      }

      Log.e(Tag, "fetchPoints: Unexpected error\n${error.message}")
      _state.update { it.copy(hasError = true) }
    }
  }

  fun onCameraStartedMoving() {
    cameraJob?.cancel()
    cameraJob = null
    updateFocusedMarker(null)
  }

  fun updateFocusedMarker(marker: PointsMapMarkerData?) {
    _state.update { it.copy(focusedMarker = marker) }
  }

  companion object {
    private const val Tag = "PointsMapViewModel"
    private val CameraJobDebounce = 500.milliseconds
  }
}

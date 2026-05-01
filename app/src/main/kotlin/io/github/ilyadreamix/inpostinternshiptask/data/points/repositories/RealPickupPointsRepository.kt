package io.github.ilyadreamix.inpostinternshiptask.data.points.repositories

import io.github.ilyadreamix.inpostinternshiptask.data.points.mappers.ListPickupPointsMapper
import io.github.ilyadreamix.inpostinternshiptask.data.points.mappers.PickupPointMapper
import io.github.ilyadreamix.inpostinternshiptask.data.points.sources.api.PickupPointsApi
import io.github.ilyadreamix.inpostinternshiptask.domain.points.options.ListPickupPointsOption
import io.github.ilyadreamix.inpostinternshiptask.domain.points.repositories.PickupPointsRepository
import io.github.ilyadreamix.inpostinternshiptask.domain.points.results.ListPickupPointsResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

internal class RealPickupPointsRepository(
  private val pickupPointsApi: PickupPointsApi,
  private val listPickupPointsMapper: ListPickupPointsMapper,
  private val pickupPointMapper: PickupPointMapper
) : PickupPointsRepository {

  override suspend fun listPickupPoints(option: ListPickupPointsOption): ListPickupPointsResult {
    val status = option.status?.let { pickupPointMapper.modelStatusToApi(it) }
    val country = option.country?.let { pickupPointMapper.modelCountryToApi(it) }
    val relativePoint = option.relativePoint?.let { "${it.latitude},${it.longitude}" }

    val response = withContext(Dispatchers.IO) { pickupPointsApi.listPickupPoints(option.perPage, status, country, relativePoint) }
    return listPickupPointsMapper.apiResponseToResult(response)
  }
}

package io.github.ilyadreamix.inpostinternshiptask.data.points.sources.api.dtos

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class ListPickupPointsResponseApiDto(@SerialName("items") val items: List<PickupPointApiDto>)

package io.github.ilyadreamix.inpostinternshiptask.data.points.sources.api.dtos

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class PickupPointApiDto(
  @SerialName("name") val name: String,
  @SerialName("location") val location: Location,
  @SerialName("status") val status: String,
  @SerialName("distance") val distance: Double? = null,
  @SerialName("address") val address: Address,
  @SerialName("location_247") val location247: Boolean,
  @SerialName("opening_hours") val openingHours: String,
  @SerialName("easy_access_zone") val easyAccessZone: Boolean,
  @SerialName("image_url") val imageUrl: String,
  @SerialName("country") val country: String,
  @SerialName("location_type") val locationType: String
) {
  @Serializable
  data class Location(
    @SerialName("longitude") val longitude: Double,
    @SerialName("latitude") val latitude: Double
  )

  @Serializable
  data class Address(
    @SerialName("line1") val line1: String,
    @SerialName("line2") val line2: String
  )

  companion object {

    const val CountryPoland = "PL"

    const val StatusOperating = "Operating"
    const val StatusDisabled = "Disabled"
    const val StatusCreated = "Created"

    const val LocationTypeOutdoor = "Outdoor"
    const val LocationTypeIndoor = "Indoor"
  }
}

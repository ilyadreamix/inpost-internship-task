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
  @SerialName("location_type") val locationType: String,
  @SerialName("operating_hours_extended") val operatingHoursExtended: OperatingHoursExtended? = null,
  @SerialName("location_description") val description: String? = null,
  @SerialName("address_details") val addressDetails: AddressDetails
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

  @Serializable
  data class OperatingHoursExtended(@SerialName("customer") val customer: Customer? = null) {
    @Serializable
    data class Customer(
      @SerialName("monday") val monday: List<Interval> = emptyList(),
      @SerialName("tuesday") val tuesday: List<Interval> = emptyList(),
      @SerialName("wednesday") val wednesday: List<Interval> = emptyList(),
      @SerialName("thursday") val thursday: List<Interval> = emptyList(),
      @SerialName("friday") val friday: List<Interval> = emptyList(),
      @SerialName("saturday") val saturday: List<Interval> = emptyList(),
      @SerialName("sunday") val sunday: List<Interval> = emptyList(),
    ) {
      @Serializable
      data class Interval(@SerialName("start") val start: Int, @SerialName("end") val end: Int)
    }
  }

  @Serializable
  data class AddressDetails(
    @SerialName("city") val city: String,
    @SerialName("province") val province: String,
    @SerialName("street") val street: String,
    @SerialName("building_number") val buildingNumber: String? = null,
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

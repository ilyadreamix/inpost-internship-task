package io.github.ilyadreamix.inpostinternshiptask.domain.points.models

internal data class PickupPointModel(
  val name: String,
  val location: Location,
  val status: Status,
  val distanceMeters: Double?,
  val address: Address,
  val locationIsAvailable24Hours: Boolean,
  val openingHours: String,
  val easyAccess: Boolean,
  val imageUrl: String,
  val country: Country,
  val locationType: LocationType
) {

  enum class Country { Poland; }

  enum class Status { Operating, Disabled, Created; }

  enum class LocationType { Outdoor, Indoor; }

  data class Address(val streetAndNumber: String, val postalCodeAndRegion: String)

  data class Location(val latitude: Double, val longitude: Double)
}

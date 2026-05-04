package io.github.ilyadreamix.inpostinternshiptask.domain.points.models

import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDateTime

internal data class PickupPointModel(
  val name: String,
  val location: Location,
  val status: Status,
  val distanceMeters: Double?,
  val address: Address,
  val addressDetails: AddressDetails,
  val locationIsAvailable24Hours: Boolean,
  val operatingHours: OperatingHours?,
  val openingHours: String,
  val easyAccess: Boolean,
  val imageUrl: String,
  val country: Country,
  val locationType: LocationType,
  val description: String?
) {

  enum class Country { Poland; }

  enum class Status { Operating, Disabled, Created; }

  enum class LocationType { Outdoor, Indoor; }

  data class Address(val streetAndNumber: String, val postalCodeAndRegion: String)

  data class AddressDetails(
    val city: String,
    val province: String,
    val street: String,
    val buildingNumber: String?
  )

  data class Location(val latitude: Double, val longitude: Double)

  data class OperatingHours(
    val monday: List<Interval>,
    val tuesday: List<Interval>,
    val wednesday: List<Interval>,
    val thursday: List<Interval>,
    val friday: List<Interval>,
    val saturday: List<Interval>,
    val sunday: List<Interval>
  ) {
    data class Interval(val start: Int, val end: Int)
  }
}

internal fun PickupPointModel.isOpen(time: LocalDateTime = LocalDateTime(2026, 5, 10, 23, 30, 0, 0)): Boolean {
  if (locationIsAvailable24Hours || operatingHours == null) {
    return true
  }

  val currentMinutes = time.hour * 60 + time.minute

  val intervals = when (time.dayOfWeek) {
    DayOfWeek.MONDAY -> operatingHours.monday
    DayOfWeek.TUESDAY -> operatingHours.tuesday
    DayOfWeek.WEDNESDAY -> operatingHours.wednesday
    DayOfWeek.THURSDAY -> operatingHours.thursday
    DayOfWeek.FRIDAY -> operatingHours.friday
    DayOfWeek.SATURDAY -> operatingHours.saturday
    DayOfWeek.SUNDAY -> operatingHours.sunday
  }

  return intervals.any { currentMinutes >= it.start && currentMinutes < it.end }
}

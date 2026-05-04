package io.github.ilyadreamix.inpostinternshiptask.data.points.mappers

import io.github.ilyadreamix.inpostinternshiptask.data.points.sources.api.dtos.PickupPointApiDto
import io.github.ilyadreamix.inpostinternshiptask.domain.exceptions.AppException
import io.github.ilyadreamix.inpostinternshiptask.domain.points.models.PickupPointModel

internal class RealPickupPointMapper : PickupPointMapper {
  override fun apiDtoToModel(dto: PickupPointApiDto) = PickupPointModel(
    name = dto.name,
    location = PickupPointModel.Location(dto.location.latitude, dto.location.longitude),
    status = apiStatusToModel(dto.status),
    distanceMeters = dto.distance,
    address = PickupPointModel.Address(streetAndNumber = dto.address.line1, postalCodeAndRegion = dto.address.line2),
    locationIsAvailable24Hours = dto.location247,
    openingHours = dto.openingHours,
    easyAccess = dto.easyAccessZone,
    imageUrl = dto.imageUrl,
    country = apiCountryToModel(dto.country),
    locationType = apiLocationTypeToModel(dto.locationType),
    operatingHours = dto.operatingHoursExtended?.customer?.let {
      PickupPointModel.OperatingHours(
        monday = it.monday.toModel(),
        tuesday = it.tuesday.toModel(),
        wednesday = it.wednesday.toModel(),
        thursday = it.thursday.toModel(),
        friday = it.friday.toModel(),
        saturday = it.saturday.toModel(),
        sunday = it.sunday.toModel()
      )
    },
    description = dto.description,
    addressDetails = PickupPointModel.AddressDetails(
      city = dto.addressDetails.city,
      province = dto.addressDetails.province,
      street = dto.addressDetails.street,
      buildingNumber = dto.addressDetails.buildingNumber
    )
  )

  private fun List<PickupPointApiDto.OperatingHoursExtended.Customer.Interval>.toModel() =
    map { PickupPointModel.OperatingHours.Interval(it.start, it.end) }

  override fun apiCountryToModel(country: String) = when (country) {
    PickupPointApiDto.CountryPoland -> PickupPointModel.Country.Poland
    else -> throw AppException("Unknown country: \"$country\"")
  }

  override fun apiStatusToModel(status: String) = when (status) {
    PickupPointApiDto.StatusOperating -> PickupPointModel.Status.Operating
    PickupPointApiDto.StatusDisabled -> PickupPointModel.Status.Disabled
    PickupPointApiDto.StatusCreated -> PickupPointModel.Status.Created
    else -> throw AppException("Unknown status: \"$status\"")
  }

  override fun apiLocationTypeToModel(locationType: String) = when (locationType) {
    PickupPointApiDto.LocationTypeOutdoor -> PickupPointModel.LocationType.Outdoor
    PickupPointApiDto.LocationTypeIndoor -> PickupPointModel.LocationType.Indoor
    else -> throw AppException("Unknown locationType: \"$locationType\"")
  }

  override fun modelCountryToApi(country: PickupPointModel.Country): String = when (country) {
    PickupPointModel.Country.Poland -> PickupPointApiDto.CountryPoland
  }

  override fun modelStatusToApi(status: PickupPointModel.Status): String = when (status) {
    PickupPointModel.Status.Operating -> PickupPointApiDto.StatusOperating
    PickupPointModel.Status.Disabled -> PickupPointApiDto.StatusDisabled
    PickupPointModel.Status.Created -> PickupPointApiDto.StatusCreated
  }

  override fun modelLocationTypeToApi(locationType: PickupPointModel.LocationType): String = when (locationType) {
    PickupPointModel.LocationType.Outdoor -> PickupPointApiDto.LocationTypeOutdoor
    PickupPointModel.LocationType.Indoor -> PickupPointApiDto.LocationTypeIndoor
  }
}

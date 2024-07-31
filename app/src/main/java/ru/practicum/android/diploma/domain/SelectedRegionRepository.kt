package ru.practicum.android.diploma.domain

import ru.practicum.android.diploma.domain.models.Area

interface SelectedRegionRepository {
    fun selectCountry(country: Area?)
    fun selectRegion(region: Area?)
    fun selectedCountry(): Area?
    fun selectedRegion(): Area?
    fun checkRegionsAreSaved(): Boolean
}

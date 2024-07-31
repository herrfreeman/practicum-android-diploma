package ru.practicum.android.diploma.domain.impl

import ru.practicum.android.diploma.domain.SelectedRegionInteractor
import ru.practicum.android.diploma.domain.SelectedRegionRepository
import ru.practicum.android.diploma.domain.models.Area

class SelectedRegionInteractorImpl(private val repository: SelectedRegionRepository) : SelectedRegionInteractor {
    override fun selectCountry(country: Area?) = repository.selectCountry(country)
    override fun selectRegion(region: Area?) = repository.selectRegion(region)
    override fun selectedCountry(): Area? = repository.selectedCountry()
    override fun selectedRegion(): Area? = repository.selectedRegion()
    override fun checkRegionsAreSaved() = repository.checkRegionsAreSaved()
}

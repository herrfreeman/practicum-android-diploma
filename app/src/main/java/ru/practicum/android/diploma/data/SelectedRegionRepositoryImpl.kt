package ru.practicum.android.diploma.data

import ru.practicum.android.diploma.domain.FilterRepository
import ru.practicum.android.diploma.domain.SelectedRegionRepository
import ru.practicum.android.diploma.domain.models.Area

class SelectedRegionRepositoryImpl(private val filterRepository: FilterRepository) : SelectedRegionRepository {
    private var selectedCountry: Area? = null
    private var selectedRegion: Area? = null

    override fun selectCountry(country: Area?) {
        selectedCountry = country
    }

    override fun selectRegion(region: Area?) {
        selectedRegion = region
    }

    override fun selectedCountry() = selectedCountry

    override fun selectedRegion() = selectedRegion

    override fun checkRegionsAreSaved(): Boolean {
        val currentFilter = filterRepository.currentFilter()
        return selectedCountry == currentFilter.country && selectedRegion == currentFilter.area
    }
}

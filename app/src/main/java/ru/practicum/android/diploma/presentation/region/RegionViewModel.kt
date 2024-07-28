package ru.practicum.android.diploma.presentation.region

import android.net.ConnectivityManager
import android.net.ConnectivityManager.NetworkCallback
import android.net.Network
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.domain.FilterInteractor
import ru.practicum.android.diploma.domain.api.DictionariesInteractor
import ru.practicum.android.diploma.domain.models.Area
import ru.practicum.android.diploma.presentation.workplace.WorkplaceState
import ru.practicum.android.diploma.util.Resource

class RegionViewModel(
    connectivityManager: ConnectivityManager,
    private val filterInteractor: FilterInteractor,
    private val dictionariesInteractor: DictionariesInteractor
) : ViewModel() {

    private var originalList: List<Area>? = null

    private val regionLiveData = MutableLiveData<RegionState>()
    fun getRegionLiveData(): LiveData<RegionState> = regionLiveData

    private val filteredList: MutableList<Area> = ArrayList()

    private val filterState = MutableLiveData<WorkplaceState>()
    fun getFilterState(): MutableLiveData<WorkplaceState> = filterState

    init {
        connectivityManager.registerDefaultNetworkCallback(object : NetworkCallback() {
            override fun onAvailable(network: Network) {
                super.onAvailable(network)
                when (filterState.value) {
                    is WorkplaceState.CountryAndRegionIsPicked -> {
                        loadRegionsList((filterState.value as WorkplaceState.CountryAndRegionIsPicked).country.id)
                    }

                    is WorkplaceState.CountryIsPicked -> {
                        loadRegionsList((filterState.value as WorkplaceState.CountryIsPicked).country.id)
                    }

                    WorkplaceState.NothingIsPicked -> {
                        loadRegionsList(null)
                    }

                    null -> {
                        loadRegionsList(null)
                    }
                }
            }
        })
    }

    fun loadRegionsList(countryId: String?) {
        if (countryId != null) {
            getRegionListByCountryId(countryId)
        } else {
            getRegionList()
        }
    }

    private fun getRegionListByCountryId(countryId: String) {
        viewModelScope.launch {
            val listOfRegions = mutableListOf<Area>()

            dictionariesInteractor.getAreasById(countryId).collect {
                when (it) {
                    is Resource.Success -> {
                        if (it.data != null) {
                            listOfRegions.addAll(collectAllRegions(it.data.areas!!))
                            regionLiveData.postValue(RegionState.Content(listOfRegions))
                            originalList = listOfRegions
                        } else {
                            regionLiveData.postValue(RegionState.Empty)
                            originalList = emptyList()
                        }
                    }

                    is Resource.Error -> {
                        regionLiveData.postValue(RegionState.Error)
                        originalList = emptyList()
                    }
                }
            }
        }
    }

    private fun getRegionList() {
        viewModelScope.launch {
            val listOfRegions = mutableListOf<Area>()

            dictionariesInteractor.getAreas().collect {
                when (it) {
                    is Resource.Success -> {
                        if (it.data != null) {
                            listOfRegions.addAll(collectAllCountries(it.data))
                            regionLiveData.postValue(RegionState.Content(listOfRegions))
                            originalList = listOfRegions
                        } else {
                            regionLiveData.postValue(RegionState.Empty)
                            originalList = emptyList()
                        }
                    }

                    is Resource.Error -> {
                        regionLiveData.postValue(RegionState.Error)
                        originalList = emptyList()
                    }
                }
            }
        }
    }

    private fun collectAllCountries(listOfCountries: List<Area>): List<Area> {
        val result = mutableListOf<Area>()

        for (country in listOfCountries) {
            if (!country.areas.isNullOrEmpty()) {
                result.addAll(collectAllRegions(country.areas))
            }
        }

        return result.sortedBy {
            it.name
        }
    }

    private fun collectAllRegions(listOfRegions: List<Area>): List<Area> {
        val result = mutableListOf<Area>()

        for (region in listOfRegions) {
            result.add(region)
            if (!region.areas.isNullOrEmpty()) {
                for (locality in region.areas) {
                    result.add(locality)
                }
            }
        }

        return result.sortedBy {
            it.name
        }
    }

    fun filter(searchQuery: String?, countryId: String?) {
        filteredList.clear()
        if (!originalList.isNullOrEmpty()) {
            if (searchQuery.isNullOrEmpty()) {
                loadRegionsList(countryId)
            } else {
                for (item in originalList!!) {
                    if (item.name.contains(searchQuery, true)) {
                        filteredList.add(item)
                    }
                }
                if (filteredList.isNotEmpty()) {
                    regionLiveData.postValue(RegionState.Content(filteredList))
                } else {
                    regionLiveData.postValue(RegionState.NoRegion)
                }
            }
        } else {
            regionLiveData.postValue(RegionState.Error)
        }
    }

    fun defineCurrentFilterState() {
        val filterCountry = filterInteractor.currentFilter().country
        val filterArea = filterInteractor.currentFilter().area

        if (filterCountry == null) {
            filterState.postValue(WorkplaceState.NothingIsPicked)
        } else {
            if (filterArea?.parentId.isNullOrEmpty()) {
                filterState.postValue(WorkplaceState.CountryIsPicked(filterCountry))
            } else {
                filterState.postValue(
                    WorkplaceState.CountryAndRegionIsPicked(filterCountry, filterArea!!)
                )
            }
        }
    }

    fun setCountryAndRegion(region: Area) {
        if (region.parentId.isNullOrEmpty()) {
            filterInteractor.setCountry(region)
            filterInteractor.setArea(region)
        } else if (filterInteractor.currentFilter().country != null) {
            filterInteractor.setArea(region)
        } else {
            filterInteractor.setCountry(null)
            filterInteractor.setArea(region)
        }
    }
}

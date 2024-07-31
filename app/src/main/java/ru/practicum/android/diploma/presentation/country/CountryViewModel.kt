package ru.practicum.android.diploma.presentation.country

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.domain.SelectedRegionInteractor
import ru.practicum.android.diploma.domain.api.DictionariesInteractor
import ru.practicum.android.diploma.domain.models.Area
import ru.practicum.android.diploma.util.Resource

class CountryViewModel(
    private val selectedRegionInteractor: SelectedRegionInteractor,
    private val dictionariesInteractor: DictionariesInteractor
) : ViewModel() {

    private val countryLiveData = MutableLiveData<CountryState>()
    fun getCountryLiveData(): LiveData<CountryState> = countryLiveData

    fun loadCountriesList() {
        viewModelScope.launch {
            dictionariesInteractor.getAreas().collect {
                when (it) {
                    is Resource.Success -> {
                        if (it.data != null) {
                            countryLiveData.postValue(CountryState.Content(it.data))
                        } else {
                            countryLiveData.postValue(CountryState.Empty)
                        }
                    }

                    is Resource.Error -> {
                        countryLiveData.postValue(CountryState.Error)
                    }
                }
            }
        }
    }

    fun setCountry(country: Area) {
        selectedRegionInteractor.selectCountry(country)
        selectedRegionInteractor.selectRegion(country)
    }
}

package ru.practicum.android.diploma.presentation.filter

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.domain.FilterInteractor
import ru.practicum.android.diploma.domain.models.Filter

class FilterViewModel(private val filterInteractor: FilterInteractor, application: Application) :
    AndroidViewModel(application) {
    private var currentFilter = Filter()
    private var nextFilter = Filter()
    private var workPlace: String? = null
    private var area: String? = null
    private var industry: String? = null
    private var salary: String? = null
    private var salaryIsRequired = true
    private val stateLiveData = MutableLiveData<FilterState>()
    fun observeState(): LiveData<FilterState> = stateLiveData

    init {
        viewModelScope.launch {
            currentFilter = filterInteractor.currentFilter()
        }
        fillData(currentFilter)
    }

    private fun renderState(state: FilterState) {
        stateLiveData.postValue(state)
    }

    fun clearFilter() {
        viewModelScope.launch {
            filterInteractor.setOnlyWithSalary(false)
            filterInteractor.setIndustry(null)
            filterInteractor.setArea(null)
            filterInteractor.setSalary(null)
            filterInteractor.apply()
            currentFilter = filterInteractor.currentFilter()
            fillData(currentFilter)
        }
    }

    fun setSalaryIsRequired(required: Boolean) {
        salaryIsRequired = required
        viewModelScope.launch {
            filterInteractor.setOnlyWithSalary(required)
        }
    }

    fun setSalary(salary: String) {
        viewModelScope.launch {
            filterInteractor.setSalary(salary)
        }
        this.salary = salary
    }

    fun saveFilter(
        workPlace: String,
        industry: String,
        salary: String,
        checkSalaryRequired: Boolean
    ) {
        this.workPlace = workPlace
        this.industry = industry
        this.salary = salary
        this.salaryIsRequired = checkSalaryRequired
    }

    fun clearWorkplace() {
        area = ""
        viewModelScope.launch {
            filterInteractor.setArea(null)
        }
    }

    fun clearIndustry() {
        industry = ""
        viewModelScope.launch {
            filterInteractor.setIndustry(null)
        }
    }

    fun resetTheChanges() {
        viewModelScope.launch {
            filterInteractor.restore()
        }
    }

    fun clearSalary() {
        salary = ""
        viewModelScope.launch {
            filterInteractor.setSalary(null)
        }
    }

    fun checkNewFilter() {
        viewModelScope.launch {
            nextFilter = filterInteractor.newFilter()
            currentFilter =filterInteractor.currentFilter()
        }
        if (currentFilter != nextFilter) {
            fillData(nextFilter)
        } else {
            fillData(currentFilter)
        }
    }

    private fun fillData(filter: Filter) {
        renderState(FilterState.Filtered(filter))
    }

    fun showSaveButton() {
        renderState(FilterState.readyToSave)
    }

    fun saveNewFilter() {
        viewModelScope.launch {
            filterInteractor.apply()
        }
    }

}
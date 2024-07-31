package ru.practicum.android.diploma.data

import android.content.Context
import com.google.gson.Gson
import ru.practicum.android.diploma.domain.FilterRepository
import ru.practicum.android.diploma.domain.models.Area
import ru.practicum.android.diploma.domain.models.Filter
import ru.practicum.android.diploma.domain.models.Industry

class FilterRepostoryImpl(val context: Context) : FilterRepository {
    private val sharedPreferences = context.getSharedPreferences("filter_storage", Context.MODE_PRIVATE)
    private var currentFilter: Filter = Filter()
    private var appliedFilter: Filter = Filter()

    init {
        loadCurrentFilter()
        loadAppliedFilter()
        appliedFilter = currentFilter
    }

    private fun loadCurrentFilter() {
        currentFilter = Gson().fromJson(
            sharedPreferences.getString(CURRENT_FILTER, ""),
            Filter::class.java
        ) ?: Filter()
    }

    private fun saveCurrentFilter() {
        sharedPreferences
            .edit()
            .putString(CURRENT_FILTER, Gson().toJson(currentFilter))
            .apply()
    }

    private fun loadAppliedFilter() {
        appliedFilter = Gson().fromJson(
            sharedPreferences.getString(APPLIED_FILTER, ""),
            Filter::class.java
        ) ?: Filter()
    }

    private fun saveAppliedFilter() {
        sharedPreferences
            .edit()
            .putString(APPLIED_FILTER, Gson().toJson(appliedFilter))
            .apply()
    }

    override fun currentFilter(): Filter = currentFilter
    override fun appliedFilter(): Filter = appliedFilter

    override fun setCountry(country: Area?) {
        currentFilter = currentFilter.copy(country = country)
        saveCurrentFilter()
    }

    override fun setArea(area: Area?) {
        currentFilter = currentFilter.copy(area = area)
        saveCurrentFilter()
    }

    override fun setIndustry(industry: Industry?) {
        currentFilter = currentFilter.copy(industry = industry)
        saveCurrentFilter()
    }

    override fun setSalary(salary: String?) {
        currentFilter = currentFilter.copy(salary = salary)
        saveCurrentFilter()
    }

    override fun setOnlyWithSalary(onlyWithSalary: Boolean) {
        currentFilter = currentFilter.copy(onlyWithSalary = onlyWithSalary)
        saveCurrentFilter()
    }

    override fun apply() {
        if (appliedFilter != currentFilter) {
            appliedFilter = currentFilter
            saveAppliedFilter()
        }
    }

    override fun flushCurrentFilter() {
        currentFilter = Filter()
        saveCurrentFilter()
    }

    companion object {
        const val APPLIED_FILTER = "APPLIED_FILTER"
        const val CURRENT_FILTER = "CURRENT_FILTER"
    }
}

package ru.practicum.android.diploma.domain

import ru.practicum.android.diploma.domain.models.Area
import ru.practicum.android.diploma.domain.models.Filter
import ru.practicum.android.diploma.domain.models.Industry

interface FilterRepository {
    fun currentFilter(): Filter
    fun appliedFilter(): Filter
    fun setCountry(country: Area?)
    fun setArea(area: Area?)
    fun setIndustry(industry: Industry?)
    fun setSalary(salary: String?)
    fun setOnlyWithSalary(onlyWithSalary: Boolean)
    fun apply()
    fun flushCurrentFilter()
}

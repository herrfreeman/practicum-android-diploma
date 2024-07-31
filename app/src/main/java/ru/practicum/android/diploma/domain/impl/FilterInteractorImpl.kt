package ru.practicum.android.diploma.domain.impl

import ru.practicum.android.diploma.domain.FilterInteractor
import ru.practicum.android.diploma.domain.FilterRepository
import ru.practicum.android.diploma.domain.models.Area
import ru.practicum.android.diploma.domain.models.Filter
import ru.practicum.android.diploma.domain.models.Industry

class FilterInteractorImpl(private val repository: FilterRepository) : FilterInteractor {
    override fun currentFilter(): Filter = repository.currentFilter()

    override fun appliedFilter(): Filter = repository.appliedFilter()

    override fun setCountry(country: Area?) = repository.setCountry(country)

    override fun setArea(area: Area?) = repository.setArea(area)

    override fun setIndustry(industry: Industry?) = repository.setIndustry(industry)

    override fun setSalary(salary: String?) = repository.setSalary(salary)

    override fun setOnlyWithSalary(onlyWithSalary: Boolean) = repository.setOnlyWithSalary(onlyWithSalary)

    override fun apply() = repository.apply()

    override fun flushCurrentFilter() = repository.flushCurrentFilter()

}

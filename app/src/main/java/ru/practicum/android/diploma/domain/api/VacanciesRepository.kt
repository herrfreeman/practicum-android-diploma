package ru.practicum.android.diploma.domain.api

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.domain.models.Vacancy
import ru.practicum.android.diploma.util.Resource


interface VacanciesRepository {

    fun searchVacancies(expression: String, page: Int = 0, perPage: Int = 20): Flow<Resource<VacanciesSearchResult>>

    fun updateToFullVacancy(vacancy: Vacancy): Flow<Resource<Vacancy>>

}

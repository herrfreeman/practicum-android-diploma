package ru.practicum.android.diploma.domain.models

import java.io.Serializable

data class Vacancy(
    val id: String,
    val name: String,
    val employerName: String,
    val employerLogoPath: String,
    val employerCity: String,
    val employment: String,
    val description: String,
    val contactsEmail: String?,
    val contactsName: String?,
    val contactsPhones: String?,
    val experienceName: String?,
    val keySkills: String?,
    val salaryFrom: Int?,
    val salaryTo: Int?,
    val salaryGross: Boolean?,
    val salaryCurrencyName: String?,
    val schedule: String?,
    val callTrackingEnabled: Boolean?,
    val isFavorite: Boolean,
) : Serializable {
    companion object {
        private const val serialVersionUID = 1L
        const val EXTRAS_KEY = "VACANCY_OBJECT"
    }
}

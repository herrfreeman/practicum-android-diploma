package ru.practicum.android.diploma.di

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import ru.practicum.android.diploma.domain.models.Vacancy
import ru.practicum.android.diploma.presentation.country.CountryViewModel
import ru.practicum.android.diploma.presentation.favorites.FavoritesViewModel
import ru.practicum.android.diploma.presentation.industry.IndustryViewModel
import ru.practicum.android.diploma.presentation.filter.FilterViewModel
import ru.practicum.android.diploma.presentation.search.SearchViewModel
import ru.practicum.android.diploma.presentation.region.RegionViewModel
import ru.practicum.android.diploma.presentation.vacancy.VacancyDetailsViewModel
import ru.practicum.android.diploma.presentation.workplace.WorkplaceViewModel

val uiModule = module {

    viewModel<SearchViewModel> {
        SearchViewModel(
            vacanciesInteractor = get(),
            filterInteractor = get(),
            application = get(),
        )
    }

    viewModel { (vacancy: Vacancy, vacancyNeedToUpdate: Boolean) ->
        VacancyDetailsViewModel(
            vacancy = vacancy,
            vacancyNeedToUpdate = vacancyNeedToUpdate,
            vacanciesInteractor = get(),
            sharingInteractor = get(),
            favoritesInteractor = get(),
            application = get()
        )
    }

    viewModel<FavoritesViewModel> {
        FavoritesViewModel(
            favoritesInteractor = get(),
        )
    }

    viewModel<WorkplaceViewModel> {
        WorkplaceViewModel(
            filterInteractor = get(),
            selectedRegionInteractor = get(),
            dictionariesInteractor = get(),
        )
    }

    viewModel<CountryViewModel> {
        CountryViewModel(
            selectedRegionInteractor = get(),
            dictionariesInteractor = get(),
        )
    }

    viewModel<RegionViewModel> {
        RegionViewModel(
            filterInteractor = get(),
            selectedRegionInteractor = get(),
            dictionariesInteractor = get(),
        )
    }

    viewModel<IndustryViewModel> {
        IndustryViewModel(
            dictionariesInteractor = get(),
            filterInteractor = get(),
            application = get(),
        )
    }

    viewModel<FilterViewModel> {
        FilterViewModel(
            filterInteractor = get(),
            application = get(),
        )
    }
}

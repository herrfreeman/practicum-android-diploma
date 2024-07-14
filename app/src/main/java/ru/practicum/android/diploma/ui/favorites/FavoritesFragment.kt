package ru.practicum.android.diploma.ui.favorites

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.FragmentFavoritesBinding
import ru.practicum.android.diploma.domain.models.Vacancy
import ru.practicum.android.diploma.presentation.favorites.FavoritesState
import ru.practicum.android.diploma.presentation.favorites.FavoritesViewModel
import ru.practicum.android.diploma.ui.VacancyAdapter

class FavoritesFragment : Fragment() {

    private var _binding: FragmentFavoritesBinding? = null
    private val binding
        get() = _binding!!

    private val viewModel by viewModel<FavoritesViewModel>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentFavoritesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getStateLiveData().observe(viewLifecycleOwner) {
            when (it) {
                is FavoritesState.Content -> showContent(it.vacanciesList)
                is FavoritesState.Empty -> showEmptyMessage()
                is FavoritesState.Error -> showError()
                is FavoritesState.Loading -> showLoading()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.updateFavoritesList()
    }

    private fun showContent(listOfVacancies: List<Vacancy>) {
        val adapter = VacancyAdapter(listOfVacancies) {

        }

        binding.favoritesList.adapter = adapter

        binding.errorImage.isVisible = false
        binding.errorText.isVisible = false
        binding.loading.isVisible = false
        binding.favoritesList.isVisible = true
    }

    private fun showEmptyMessage() {
        binding.errorImage.setImageResource(R.drawable.favorites_empty_list_image)
        binding.errorText.setText(R.string.empty_favorites_list)

        binding.errorImage.isVisible = true
        binding.errorText.isVisible = true
        binding.loading.isVisible = false
        binding.favoritesList.isVisible = false
    }

    private fun showError() {
        binding.errorImage.setImageResource(R.drawable.empty_list_image)
        binding.errorText.setText(R.string.favorites_error)

        binding.errorImage.isVisible = true
        binding.errorText.isVisible = true
        binding.loading.isVisible = false
        binding.favoritesList.isVisible = false
    }

    private fun showLoading() {
        binding.errorImage.isVisible = false
        binding.errorText.isVisible = false
        binding.loading.isVisible = true
        binding.favoritesList.isVisible = false
    }

}

package ru.practicum.android.diploma.ui.industry

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.FragmentIndustryBinding
import ru.practicum.android.diploma.domain.models.Industry
import ru.practicum.android.diploma.presentation.industry.IndustryViewModel
import ru.practicum.android.diploma.ui.adapters.IndustryAdapter
import ru.practicum.android.diploma.util.ErrorType

class IndustryFragment : Fragment() {
    private val industryViewModel by viewModel<IndustryViewModel>()
    private var industryAdapter = IndustryAdapter {
        binding.selectButton.isVisible = it != null
    }

    private var _binding: FragmentIndustryBinding? = null
    private val binding
        get() = _binding!!

    private fun render(state: IndustryState) {
        when (state) {
            is IndustryState.Content -> showContent(state.industries)
            is IndustryState.Loading -> showLoading()
            is IndustryState.Empty -> showEmpty(state.message)
            is IndustryState.Error -> showError(state.error)
        }
    }

    private fun showContent(industryList: List<Industry>) {
        with(binding) {
            progressBar.isVisible = false
            centralImageHolder.isVisible = false
            recyclerView.isVisible = true
            stateTextView.isVisible = false
            selectButton.isVisible = false
        }
        industryAdapter.setData(industryList)
    }

    private fun showLoading() {
        with(binding) {
            progressBar.isVisible = true
            centralImageHolder.isVisible = false
            recyclerView.isVisible = false
            stateTextView.isVisible = false
            selectButton.isVisible = false
        }
    }

    private fun showEmpty(message: String) {
        showImageAndTextState()
        binding.centralImageHolder.setImageResource(R.drawable.empty_list_image)
        binding.stateTextView.text = message
    }

    private fun showError(errorType: ErrorType) {
        showImageAndTextState()

        when (errorType) {
            ErrorType.NoConnection -> {
                binding.stateTextView.text = getString(R.string.internet_is_not_available)
                binding.centralImageHolder.setImageResource(R.drawable.no_internet_image)
            }

            ErrorType.ServerError -> {
                binding.stateTextView.text = getString(R.string.server_error)
                binding.centralImageHolder.setImageResource(R.drawable.server_error)
            }

            else -> {
                binding.stateTextView.text = getString(R.string.favorites_error)
                binding.centralImageHolder.setImageResource(R.drawable.favorites_empty_list_image)
            }
        }
    }

    private fun showImageAndTextState() {
        with(binding) {
            progressBar.isVisible = false
            centralImageHolder.isVisible = true
            recyclerView.isVisible = false
            stateTextView.isVisible = true
            selectButton.isVisible = false
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentIndustryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.topAppBar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }

        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = industryAdapter
        binding.recyclerView.animation = null

        binding.inputEditText.addTextChangedListener(
            afterTextChanged = { text ->
                if (text.isNullOrEmpty()) {
                    binding.searchFrame.setEndIconDrawable(R.drawable.search_icon)
                } else {
                    binding.searchFrame.setEndIconDrawable(R.drawable.close_icon)
                }
                industryViewModel.searchDebounce(text.toString())
            }
        )

        binding.searchFrame.setEndIconOnClickListener {
            if (!binding.inputEditText.text.isNullOrEmpty()) {
                binding.inputEditText.setText(getString(R.string.empty_string))
            }
        }

        industryViewModel.observeState().observe(viewLifecycleOwner) {
            render(it)
        }

        binding.selectButton.setOnClickListener {
            industryViewModel.setIndustryToFilter(industryAdapter.getSelectedIndustry())
            findNavController().popBackStack()
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

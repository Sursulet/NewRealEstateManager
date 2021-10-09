package com.sursulet.realestatemanager.ui.search

import android.R
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.android.material.chip.Chip
import com.sursulet.realestatemanager.databinding.SearchFragmentBinding
import com.sursulet.realestatemanager.utils.Constants.PERIODS
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchFragment : Fragment() {

    companion object {
        fun newInstance() = SearchFragment()
    }

    private var _binding: SearchFragmentBinding? = null
    private val binding get() = _binding!!
    private val viewModel: SearchViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = SearchFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            val adapter =
                ArrayAdapter(requireContext(), R.layout.simple_list_item_1, PERIODS)
            (searchPeriod.editText as? AutoCompleteTextView)?.setAdapter(adapter)

            searchIsAvailable.setOnCheckedChangeListener { _, isChecked ->
                viewModel.onEvent(SearchEvent.Available(isChecked))
            }
            searchPhotos.setOnCheckedChangeListener { group, checkedId ->
                if (checkedId != -1) {
                    val chip: Chip = group.findViewById(checkedId)
                    viewModel.onEvent(SearchEvent.NbPhotos(chip.text.toString()))
                } else {
                    viewModel.onEvent(SearchEvent.NbPhotos(""))
                }
            }

            searchType.editText?.doOnTextChanged { text, _, _, _ ->
                viewModel.onEvent(
                    SearchEvent.Type(
                        text.toString()
                    )
                )
            }
            searchMinPrice.editText?.apply {
                doOnTextChanged { text, _, _, _ ->
                    text?.let { setSelection(it.length) }
                    viewModel.onEvent(SearchEvent.MinPrice(text.toString()))
                }
            }
            searchMaxPrice.editText?.apply {
                doOnTextChanged { text, _, _, _ ->
                    text?.let { setSelection(it.length) }
                    viewModel.onEvent(SearchEvent.MaxPrice(text.toString()))
                }
            }
            searchMinSurface.editText?.apply {
                doOnTextChanged { text, _, _, _ ->
                    text?.let { setSelection(it.length) }
                    viewModel.onEvent(SearchEvent.MinSurface(text.toString()))
                }
            }
            searchMaxSurface.editText?.apply {
                doOnTextChanged { text, _, _, _ ->
                    text?.let { setSelection(it.length) }
                    viewModel.onEvent(SearchEvent.MaxSurface(text.toString()))
                }
            }
            searchStreet.editText?.apply {
                doOnTextChanged { text, _, _, _ ->
                    text?.let { setSelection(it.length) }
                    viewModel.onEvent(SearchEvent.Street(text.toString()))
                }
            }
            searchExtras.editText?.apply {
                doOnTextChanged { text, _, _, _ ->
                    text?.let { setSelection(it.length) }
                    viewModel.onEvent(SearchEvent.Extras(text.toString()))
                }
            }
            searchPostCode.editText?.apply {
                doOnTextChanged { text, _, _, _ ->
                    text?.let { setSelection(it.length) }
                    viewModel.onEvent(SearchEvent.PostCode(text.toString()))
                }
            }
            searchCity.editText?.apply {
                doOnTextChanged { text, _, _, _ ->
                    text?.let { setSelection(it.length) }
                    viewModel.onEvent(SearchEvent.City(text.toString()))
                }
            }
            searchState.editText?.apply {
                doOnTextChanged { text, _, _, _ ->
                    text?.let { setSelection(it.length) }
                    viewModel.onEvent(SearchEvent.State(text.toString()))
                }
            }
            searchCountry.editText?.apply {
                doOnTextChanged { text, _, _, _ ->
                    text?.let { setSelection(it.length) }
                    viewModel.onEvent(SearchEvent.Country(text.toString()))
                }
            }
            searchNearest.editText?.apply {
                doOnTextChanged { text, _, _, _ ->
                    text?.let { setSelection(it.length) }
                    viewModel.onEvent(SearchEvent.Nearest(text.toString()))
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
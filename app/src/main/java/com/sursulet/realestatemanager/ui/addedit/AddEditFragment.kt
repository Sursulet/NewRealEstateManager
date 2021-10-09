package com.sursulet.realestatemanager.ui.addedit

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.sursulet.realestatemanager.databinding.AddEditFragmentBinding
import com.sursulet.realestatemanager.ui.gallery.GalleryFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class AddEditFragment : Fragment() {

    companion object {
        fun newInstance() = AddEditFragment()
    }

    private var _binding: AddEditFragmentBinding? = null
    private val binding get() = _binding!!
    private val viewModel: AddEditViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = AddEditFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            addEditAddPhotos.setOnClickListener { viewModel.onEvent(AddEditEvent.OnSave) }
            addEditIsAvailable.setOnCheckedChangeListener { _, isChecked ->
                viewModel.onEvent(AddEditEvent.IsAvailable(isChecked))
            }
            addEditType.editText?.doOnTextChanged { text, _, _, _ ->
                viewModel.onEvent(
                    AddEditEvent.Type(
                        text.toString()
                    )
                )
            }
            addEditPrice.editText?.doOnTextChanged { text, _, _, _ ->
                viewModel.onEvent(
                    AddEditEvent.Price(
                        text.toString()
                    )
                )
            }
            addEditSurface.editText?.apply {
                doOnTextChanged { text, _, _, _ ->
                    text?.let { setSelection(it.length) }
                    viewModel.onEvent(AddEditEvent.Surface(text.toString()))
                }
            }
            addEditRooms.editText?.apply {
                doOnTextChanged { text, _, _, _ ->
                    text?.let { setSelection(it.length) }
                    viewModel.onEvent(AddEditEvent.Rooms(text.toString()))
                }
            }
            addEditBedrooms.editText?.apply {
                doOnTextChanged { text, _, _, _ ->
                    text?.let { setSelection(it.length) }
                    viewModel.onEvent(AddEditEvent.Bedrooms(text.toString()))
                }
            }
            addEditBathrooms.editText?.apply {
                doOnTextChanged { text, _, _, _ ->
                    text?.let { setSelection(it.length) }
                    viewModel.onEvent(AddEditEvent.Bathrooms(text.toString()))
                }
            }
            addEditDescription.editText?.apply {
                doOnTextChanged { text, _, _, _ ->
                    text?.let { setSelection(it.length) }
                    viewModel.onEvent(AddEditEvent.Description(text.toString()))
                }
            }
            addEditStreet.editText?.apply {
                doOnTextChanged { text, _, _, _ ->
                    text?.let { setSelection(it.length) }
                    viewModel.onEvent(AddEditEvent.Street(text.toString()))
                }
            }
            addEditExtras.editText?.apply {
                doOnTextChanged { text, _, _, _ ->
                    text?.let { setSelection(it.length) }
                    viewModel.onEvent(AddEditEvent.Extras(text.toString()))
                }
            }
            addEditPostCode.editText?.apply {
                doOnTextChanged { text, _, _, _ ->
                    text?.let { setSelection(it.length) }
                    viewModel.onEvent(AddEditEvent.PostCode(text.toString()))
                }
            }
            addEditCity.editText?.apply {
                doOnTextChanged { text, _, _, _ ->
                    text?.let { setSelection(it.length) }
                    viewModel.onEvent(AddEditEvent.City(text.toString()))
                }
            }
            addEditState.editText?.apply {
                doOnTextChanged { text, _, _, _ ->
                    text?.let { setSelection(it.length) }
                    viewModel.onEvent(AddEditEvent.State(text.toString()))
                }
            }
            addEditCountry.editText?.apply {
                doOnTextChanged { text, _, _, _ ->
                    text?.let { setSelection(it.length) }
                    viewModel.onEvent(AddEditEvent.Country(text.toString()))
                }
            }
            addEditNearest.editText?.apply {
                doOnTextChanged { text, _, _, _ ->
                    text?.let { setSelection(it.length) }
                    viewModel.onEvent(AddEditEvent.Nearest(text.toString()))
                }
            }
            addEditAgent.editText?.apply {
                doOnTextChanged { text, _, _, _ ->
                    text?.let { setSelection(it.length) }
                    viewModel.onEvent(AddEditEvent.Agent(text.toString()))
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.uiState.collect { state ->
                binding.apply {
                    addEditType.error = state.error.type
                    addEditPrice.error = state.error.price
                    addEditSurface.error = state.error.surface
                    addEditRooms.error = state.error.rooms
                    addEditDescription.error = state.error.description
                    addEditStreet.error = state.error.street
                    addEditCity.error = state.error.city
                    addEditState.error = state.error.state
                    addEditPostCode.error = state.error.postCode
                    addEditCountry.error = state.error.country
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.navigation.collect { action ->
                when (action) {
                    AddEditNavigation.GalleryActivity -> {
                        showDialog()
                    }
                    AddEditNavigation.GalleryFragment -> {
                        showDialog()
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun showDialog() {
        val newFragment = GalleryFragment()
        newFragment.show(childFragmentManager, "dialog")
    }

}
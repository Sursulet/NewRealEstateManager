package com.sursulet.realestatemanager.ui.addedit

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
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

            addEditIsAvailable.setOnCheckedChangeListener { _, isChecked ->
                viewModel.onEvent(AddEditEvent.IsAvailable(isChecked))
            }
            addEditType.editText?.doOnTextChanged { text, _, _, _ ->
                viewModel.onEvent(AddEditEvent.Type(text.toString()))
            }
            addEditPrice.editText?.doOnTextChanged { text, _, _, _ ->
                viewModel.onEvent(AddEditEvent.Price(text.toString()))
            }
            addEditSurface.editText?.doOnTextChanged { text, _, _, _ ->
                viewModel.onEvent(AddEditEvent.Surface(text.toString()))
            }
            addEditRooms.editText?.doOnTextChanged { text, _, _, _ ->
                viewModel.onEvent(AddEditEvent.Rooms(text.toString()))
            }
            addEditBedrooms.editText?.doOnTextChanged { text, _, _, _ ->
                viewModel.onEvent(AddEditEvent.Bedrooms(text.toString()))
            }
            addEditBathrooms.editText?.doOnTextChanged { text, _, _, _ ->
                viewModel.onEvent(AddEditEvent.Bathrooms(text.toString()))
            }
            addEditDescription.editText?.doOnTextChanged { text, _, _, _ ->
                viewModel.onEvent(AddEditEvent.Description(text.toString()))
            }
            addEditStreet.editText?.doOnTextChanged { text, _, _, _ ->
                viewModel.onEvent(AddEditEvent.Street(text.toString()))
            }
            addEditExtras.editText?.doOnTextChanged { text, _, _, _ ->
                viewModel.onEvent(AddEditEvent.Extras(text.toString()))
            }
            addEditPostCode.editText?.doOnTextChanged { text, _, _, _ ->
                viewModel.onEvent(AddEditEvent.PostCode(text.toString()))
            }
            addEditCity.editText?.doOnTextChanged { text, _, _, _ ->
                viewModel.onEvent(AddEditEvent.City(text.toString()))
            }
            addEditState.editText?.doOnTextChanged { text, _, _, _ ->
                viewModel.onEvent(AddEditEvent.State(text.toString()))
            }
            addEditCountry.editText?.doOnTextChanged { text, _, _, _ ->
                viewModel.onEvent(AddEditEvent.Country(text.toString()))
            }
            addEditNearest.editText?.doOnTextChanged { text, _, _, _ ->
                viewModel.onEvent(AddEditEvent.Nearest(text.toString()))
            }
            addEditAgent.editText?.doOnTextChanged { text, _, _, _ ->
                viewModel.onEvent(AddEditEvent.Agent(text.toString()))
            }
        }
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.uiState.collect { state ->
                binding.apply {

                    setFragmentResult(
                        "requestAddEditKey",
                        bundleOf("bundleAddEditKey" to state.title)
                    )
                    if (state.estate != null) addEditSave.visibility = View.VISIBLE

                    addEditSave.setOnClickListener { viewModel.onEvent(AddEditEvent.OnSave) }
                    addEditActionAddPhotos.setOnClickListener { viewModel.onEvent(AddEditEvent.AddPhotos) }
                    addEditIsAvailable.isChecked = state.isAvailable
                    addEditIsAvailable.text =
                        if (state.created.isNotBlank()) {
                            if (state.isAvailable) "This property has been available since ${state.created}" else "This property is no longer available since ${state.sold}"
                        } else {
                            if (state.isAvailable) "This property will be available from today" else "This property will no longer be available from today"
                        }

                    if (state.type != addEditType.editText?.text.toString()) {
                        addEditType.editText?.setText(state.type)
                    }
                    if (state.price != addEditPrice.editText?.text.toString()) {
                        addEditPrice.editText?.setText(state.price)
                    }
                    if (state.surface != addEditSurface.editText?.text.toString()) {
                        addEditSurface.editText?.setText(state.surface)
                    }
                    if (state.rooms != addEditRooms.editText?.text.toString()) {
                        addEditRooms.editText?.setText(state.rooms)
                    }
                    if (state.bathrooms != addEditBathrooms.editText?.text.toString()) {
                        addEditBathrooms.editText?.setText(state.bathrooms)
                    }
                    if (state.bedrooms != addEditBedrooms.editText?.text.toString()) {
                        addEditBedrooms.editText?.setText(state.bedrooms)
                    }
                    if (state.description != addEditDescription.editText?.text.toString()) {
                        addEditDescription.editText?.setText(state.description)
                    }
                    if (state.street != addEditStreet.editText?.text.toString()) {
                        addEditStreet.editText?.setText(state.street)
                    }
                    if (state.city != addEditCity.editText?.text.toString()) {
                        addEditCity.editText?.setText(state.city)
                    }
                    if (state.state != addEditState.editText?.text.toString()) {
                        addEditState.editText?.setText(state.state)
                    }
                    if (state.postCode != addEditPostCode.editText?.text.toString()) {
                        addEditPostCode.editText?.setText(state.postCode)
                    }
                    if (state.country != addEditCountry.editText?.text.toString()) {
                        addEditCountry.editText?.setText(state.country)
                    }
                    if (state.agent != addEditAgent.editText?.text.toString()) {
                        addEditAgent.editText?.setText(state.agent)
                    }

                    addEditType.error = state.error.type
                    addEditPrice.error = state.error.price
                    addEditSurface.error = state.error.surface
                    addEditRooms.error = state.error.rooms
                    addEditDescription.error = state.error.description
                    addEditStreet.error = state.error.street
                    addEditCity.error = state.error.city
                    addEditPostCode.error = state.error.postCode
                    addEditCountry.error = state.error.country
                    addEditAgent.error = state.error.agent
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.navigation.collect { action ->
                when (action) {
                    AddEditNavigation.GalleryDialogFragment -> {
                        showDialog()
                    }
                    AddEditNavigation.DetailActivity -> {
                        requireActivity().finish()
                    }
                    AddEditNavigation.DetailFragment -> {
                        requireActivity().supportFragmentManager.beginTransaction()
                            .remove(this@AddEditFragment).commit()
                    }
                    is AddEditNavigation.Message -> {
                        Toast.makeText(requireContext(), action.value, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        childFragmentManager.setFragmentResultListener("isSaveRequest", this) { _, bundle ->
            val result = bundle.getBoolean("isSaveBundle")
            viewModel.onEvent(AddEditEvent.NotSave(result))
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun showDialog() {
        val newFragment = GalleryFragment.newInstance()
        newFragment.show(childFragmentManager, "gallery dialog")
    }

}
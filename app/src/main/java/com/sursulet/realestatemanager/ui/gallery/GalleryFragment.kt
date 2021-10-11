package com.sursulet.realestatemanager.ui.gallery

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.sursulet.realestatemanager.R
import com.sursulet.realestatemanager.databinding.GalleryFragmentBinding
import com.sursulet.realestatemanager.ui.adapters.PhotoAdapter
import com.sursulet.realestatemanager.ui.photo.PhotoDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class GalleryFragment : DialogFragment() {

    companion object {
        fun newInstance() = GalleryFragment()
    }

    private var _binding: GalleryFragmentBinding?= null
    private val binding get() = _binding!!
    private val viewModel: GalleryViewModel by viewModels()

    @Inject
    lateinit var galleryAdapter: PhotoAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = GalleryFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        binding.apply {
            toolbar.apply {
                setNavigationOnClickListener { viewModel.onEvent(GalleryEvent.OnClose) }
                title = "Gallery"
                inflateMenu(R.menu.save_menu)
                isEnabled = false
                setOnMenuItemClickListener {
                    viewModel.onEvent(GalleryEvent.OnSave)
                    return@setOnMenuItemClickListener true
                }
            }
        }

        binding.apply {
            actionAdd.setOnClickListener { showDialog() }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.uiState.collect { state ->
                setupRecyclerView(state.isTwoPane)
                galleryAdapter.submitList(state.photos)
            }
        }

        galleryAdapter.setOnItemClickListener {
            viewModel.onEvent(GalleryEvent.OnEdit(it))
            showDialog()
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.navigation.collect { action ->
                when (action) {
                    GalleryNavigation.CloseFragment -> {
                        setFragmentResult("isSaveRequest", bundleOf("isSaveBundle" to true))
                        dismiss()
                    }
                    GalleryNavigation.Cancel -> {
                        setFragmentResult("isSaveRequest", bundleOf("isSaveBundle" to false))
                        dismiss()
                    }
                    is GalleryNavigation.EmptyGallery -> {
                        MaterialAlertDialogBuilder(requireContext())
                            .setMessage(action.value)
                            .setPositiveButton(resources.getString(R.string.ok)) { dialog, _ ->
                                dialog.dismiss()
                            }
                            .show()
                    }
                }
            }
        }

    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.MATCH_PARENT
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupRecyclerView(twoPane: Boolean) {
        val grid = if (twoPane) { 10 } else { 4 }
        binding.galleryRecyclerview.apply {
            adapter = galleryAdapter
            layoutManager = GridLayoutManager(requireContext(),grid)
        }
    }

    private fun showDialog() {
        val newFragment = PhotoDialogFragment()
        newFragment.show(childFragmentManager, "dialog")
    }
}
package com.sursulet.realestatemanager.ui.gallery

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.sursulet.realestatemanager.R
import com.sursulet.realestatemanager.databinding.GalleryFragmentBinding
import com.sursulet.realestatemanager.ui.adapters.PhotoAdapter
import com.sursulet.realestatemanager.ui.photo.PhotoDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

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
        setupRecyclerView()
        galleryAdapter.setOnItemClickListener {
            viewModel.onEvent(GalleryEvent.OnEdit(it))
            showDialog()
        }

        binding.apply {
            toolbar.apply {
                setNavigationOnClickListener { dismiss() }
                title = "Add Photo"
                inflateMenu(R.menu.save_menu)
                isEnabled = false
                setOnMenuItemClickListener {
                    viewModel.onEvent(GalleryEvent.OnSave)
                    return@setOnMenuItemClickListener true
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.navigation.collect {
                Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
                requireActivity().finish()
            }
        }

        binding.apply {
            actionAdd.setOnClickListener { showDialog() }

            galleryRecyclerview.apply {
                adapter = galleryAdapter
                layoutManager = GridLayoutManager(requireContext(), 4)
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.uiState.collect {
                galleryAdapter.submitList(it.photos)
            }
        }

        galleryAdapter.setOnItemClickListener {
            viewModel.onEvent(GalleryEvent.OnEdit(it))
            showDialog()
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

    private fun setupRecyclerView() {
        binding.galleryRecyclerview.apply {
            adapter = galleryAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun showDialog() {
        val newFragment = PhotoDialogFragment()
        newFragment.show(childFragmentManager, "dialog")
    }
}
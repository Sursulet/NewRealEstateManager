package com.sursulet.realestatemanager.ui.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.MarkerOptions
import com.sursulet.realestatemanager.R
import com.sursulet.realestatemanager.databinding.DetailFragmentBinding
import com.sursulet.realestatemanager.ui.adapters.PhotoAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class DetailFragment : Fragment() {

    companion object {
        fun newInstance() = DetailFragment()
    }

    private var _binding: DetailFragmentBinding? = null
    private val binding get() = _binding!!
    private val viewModel: DetailViewModel by viewModels()

    @Inject
    lateinit var galleryAdapter: PhotoAdapter
    private var map: GoogleMap?=null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DetailFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()

        childFragmentManager.commit {
            val fragment = SupportMapFragment()
            fragment.getMapAsync { googleMap -> map = googleMap }
            replace(R.id.detail_map, fragment, null)
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.uiState.collect { state ->

                galleryAdapter.submitList(state.media)

                binding.apply {
                    detailDescription.text = state.description
                    detailRooms.text = state.rooms
                    detailBathrooms.text = state.bathrooms
                    detailBedrooms.text = state.bedrooms
                    detailSurface.text = state.surface
                    detailLocation.text = state.location
                }

                map?.let { map ->
                    state.coordinates?.let {
                        map.addMarker(MarkerOptions().position(it))
                        map.moveCamera(CameraUpdateFactory.newLatLngZoom(it, 12f))
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupRecyclerView() {
        binding.detailMedia.apply {
            adapter = galleryAdapter
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        }
    }

}
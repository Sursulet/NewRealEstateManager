package com.sursulet.realestatemanager.ui.maps

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.snackbar.Snackbar
import com.sursulet.realestatemanager.R
import com.sursulet.realestatemanager.databinding.MapsFragmentBinding
import com.sursulet.realestatemanager.utils.Others.showSnackBar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class MapsFragment : Fragment() {

    companion object {
        fun newInstance() = MapsFragment()
    }

    private var _binding: MapsFragmentBinding? = null
    private val binding get() = _binding!!
    private val viewModel: MapsViewModel by viewModels()

    private lateinit var map: GoogleMap
    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (isGranted) {
                viewModel.onEvent(MapsEvent.LastKnownLocation)
                Log.i("Permission: ", "Granted")
                binding.root.showSnackBar(
                    binding.root,
                    getString(R.string.permission_granted),
                    Snackbar.LENGTH_SHORT,
                    null
                ) {}
            } else {
                Log.i("Permission: ", "Denied")
                requestPermission()
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = MapsFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.onEvent(MapsEvent.StartUpdatingLocation)

        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync { googleMap ->

            map = googleMap
            viewModel.onEvent(MapsEvent.MapReady)
            with(googleMap.uiSettings) { isZoomControlsEnabled = true }

            map.setOnCameraIdleListener {
                val zoom = map.cameraPosition.zoom
                viewModel.onEvent(MapsEvent.Zoom(value = zoom))
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.uiState.collect { state ->
                if (state.isMapReady) {
                    map.clear()

                    val lastLocation = state.lastLocation.let { LatLng(it.latitude, it.longitude) }
                    //map.addMarker(MarkerOptions().position(lastLocation))
                    map.moveCamera(CameraUpdateFactory.newLatLngZoom(lastLocation, state.zoomLvl))

                    state.markers.map { marker ->
                        map.addMarker(MarkerOptions().position(marker.coordinates))
                    }
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        requestPermission()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun requestPermission() {
        when {
            ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED -> {
                viewModel.onEvent(MapsEvent.LastKnownLocation)
            }

            ActivityCompat.shouldShowRequestPermissionRationale(
                requireActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) -> {
                binding.root.showSnackBar(
                    binding.root,
                    getString(R.string.permission_required),
                    Snackbar.LENGTH_INDEFINITE,
                    getString(R.string.ok)
                ) {
                    requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
                }
            }

            else -> {
                requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
            }
        }
    }
}
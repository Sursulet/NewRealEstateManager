package com.sursulet.realestatemanager.ui.maps

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.sursulet.realestatemanager.R
import com.sursulet.realestatemanager.databinding.MapsActivityBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class MapsActivity : AppCompatActivity() {

    private lateinit var binding: MapsActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = MapsActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.mapsToolbar.setNavigationOnClickListener { finish() }
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.maps_container, MapsFragment.newInstance())
                .commitNow()
        }
    }
}
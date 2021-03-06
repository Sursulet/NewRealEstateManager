package com.sursulet.realestatemanager.ui.gallery

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.sursulet.realestatemanager.R
import com.sursulet.realestatemanager.databinding.GalleryActivityBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class GalleryActivity : AppCompatActivity() {

    private lateinit var binding: GalleryActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = GalleryActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.galleryToolbar.setNavigationOnClickListener { finish() }
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.gallery_container, GalleryFragment.newInstance())
                .commitNow()
        }
    }
}
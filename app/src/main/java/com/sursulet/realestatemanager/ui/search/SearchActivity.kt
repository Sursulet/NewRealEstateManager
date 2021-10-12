package com.sursulet.realestatemanager.ui.search

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.sursulet.realestatemanager.R
import com.sursulet.realestatemanager.databinding.SearchActivityBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchActivity : AppCompatActivity() {

    private lateinit var binding: SearchActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = SearchActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.searchToolbar.setNavigationOnClickListener { finish() }

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.search_container, SearchFragment.newInstance())
                .commitNow()
        }
    }
}
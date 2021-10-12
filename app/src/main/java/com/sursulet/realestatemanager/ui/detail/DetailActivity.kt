package com.sursulet.realestatemanager.ui.detail

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.sursulet.realestatemanager.R
import com.sursulet.realestatemanager.databinding.DetailActivityBinding
import com.sursulet.realestatemanager.ui.addedit.AddEditActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class DetailActivity : AppCompatActivity() {

    private lateinit var binding: DetailActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DetailActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.detailToolbar.setNavigationOnClickListener { finish() }
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.detail_container, DetailFragment.newInstance())
                .commitNow()
        }

        binding.detailActionEdit.setOnClickListener {
            startActivity(Intent(this,AddEditActivity::class.java))
        }
    }
}
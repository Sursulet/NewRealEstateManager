package com.sursulet.realestatemanager.ui.addedit

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.sursulet.realestatemanager.R
import com.sursulet.realestatemanager.databinding.AddEditActivityBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class AddEditActivity : AppCompatActivity() {

    private lateinit var binding: AddEditActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = AddEditActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.addEditToolbar.setNavigationOnClickListener { finish() }
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.add_edit_container, AddEditFragment.newInstance())
                .commitNow()
        }

        supportFragmentManager.setFragmentResultListener("requestAddEditKey", this) { _, bundle ->
            val result = bundle.getString("bundleAddEditKey")
            binding.addEditToolbar.title = result
        }
    }
}
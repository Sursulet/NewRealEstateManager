package com.sursulet.realestatemanager.ui.addedit

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.sursulet.realestatemanager.R
import com.sursulet.realestatemanager.databinding.AddEditActivityBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddEditActivity : AppCompatActivity() {

    private lateinit var binding: AddEditActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = AddEditActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.toolbar.setNavigationOnClickListener { finish() }
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.add_edit_container, AddEditFragment.newInstance())
                .commitNow()
        }
    }
}
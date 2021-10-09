package com.sursulet.realestatemanager.ui.loan

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.sursulet.realestatemanager.R
import com.sursulet.realestatemanager.databinding.LoanActivityBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoanActivity : AppCompatActivity() {

    private lateinit var binding: LoanActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = LoanActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        binding.toolbar.setNavigationOnClickListener { finish() }

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container_loan, LoanFragment.newInstance())
                .commitNow()
        }
    }
}
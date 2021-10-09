package com.sursulet.realestatemanager

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.sursulet.realestatemanager.databinding.MainActivityBinding
import com.sursulet.realestatemanager.ui.addedit.AddEditActivity
import com.sursulet.realestatemanager.ui.addedit.AddEditFragment
import com.sursulet.realestatemanager.ui.detail.DetailActivity
import com.sursulet.realestatemanager.ui.detail.DetailFragment
import com.sursulet.realestatemanager.ui.loan.LoanActivity
import com.sursulet.realestatemanager.ui.loan.LoanFragment
import com.sursulet.realestatemanager.ui.main.MainEvent
import com.sursulet.realestatemanager.ui.main.MainFragment
import com.sursulet.realestatemanager.ui.main.MainNavigation
import com.sursulet.realestatemanager.ui.main.MainViewModel
import com.sursulet.realestatemanager.ui.maps.MapsActivity
import com.sursulet.realestatemanager.ui.maps.MapsFragment
import com.sursulet.realestatemanager.ui.search.SearchActivity
import com.sursulet.realestatemanager.ui.search.SearchFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: MainActivityBinding
    private val viewModel: MainViewModel by viewModels()

    private var isTowPane: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = MainActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        if (savedInstanceState == null) {
            if (binding.mainContent.container != null) {
                isTowPane = true
                viewModel.onEvent(MainEvent.TwoPaneScreen)
            }

            supportFragmentManager.beginTransaction()
                .replace(R.id.main_container, MainFragment.newInstance(isTowPane))
                .commitNow()
        }

        binding.apply {
            mainContent.toolbar.apply {
                setNavigationOnClickListener { drawerLayout.openDrawer(GravityCompat.START) }

                setOnMenuItemClickListener { item ->
                    when (item.itemId) {
                        R.id.action_add -> {
                            viewModel.onEvent(MainEvent.Add)
                            true
                        }
                        R.id.action_edit -> {
                            viewModel.onEvent(MainEvent.Edit)
                            true
                        }
                        R.id.action_search -> {
                            viewModel.onEvent(MainEvent.Search)
                            true
                        }
                        else -> false
                    }
                }
            }

            navView.setNavigationItemSelectedListener { item ->
                when (item.itemId) {
                    R.id.action_maps -> {
                        viewModel.onEvent(MainEvent.Maps)
                    }
                    R.id.action_loan -> {
                        viewModel.onEvent(MainEvent.Loan)
                    }
                }

                //item.isChecked = true
                drawerLayout.closeDrawer(GravityCompat.START)
                true
            }
        }

        supportFragmentManager.setFragmentResultListener("requestKey", this) { _, bundle ->
            val result = bundle.getLong("bundleKey")
            viewModel.onEvent(MainEvent.Detail(result))
        }

        lifecycleScope.launch {
            viewModel.navigation.collect { action ->
                when (action) {
                    is MainNavigation.DetailActivity -> {
                        openActivity(Intent(this@MainActivity, DetailActivity::class.java))
                    }
                    is MainNavigation.DetailFragment -> {
                        openFragment(DetailFragment.newInstance())
                    }
                    is MainNavigation.AddEditActivity -> {
                        openActivity(Intent(this@MainActivity, AddEditActivity::class.java))
                    }
                    is MainNavigation.AddEditFragment -> {
                        openFragment(AddEditFragment.newInstance())
                    }
                    is MainNavigation.SearchActivity -> {
                        openActivity(Intent(this@MainActivity, SearchActivity::class.java))
                    }
                    is MainNavigation.SearchFragment -> {
                        openFragment(SearchFragment.newInstance())
                    }
                    is MainNavigation.MapsActivity -> {
                        openActivity(Intent(this@MainActivity, MapsActivity::class.java))
                    }
                    is MainNavigation.MapsFragment -> {
                        openFragment(MapsFragment.newInstance())
                    }
                    is MainNavigation.LoanActivity -> {
                        openActivity(Intent(this@MainActivity, LoanActivity::class.java))
                    }
                    is MainNavigation.LoanFragment -> {
                        openFragment(LoanFragment.newInstance())
                    }
                    is MainNavigation.Message -> {
                        Toast.makeText(this@MainActivity, action.msg, Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
    }

    private fun openActivity(intent: Intent) {
        startActivity(intent)
    }

    private fun openFragment(newInstance: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.container, newInstance)
            .commitNow()
    }
}
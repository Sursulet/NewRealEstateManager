package com.sursulet.realestatemanager.ui.main

sealed class MainNavigation {
    data class Message(val msg: String) : MainNavigation()
    object DetailActivity : MainNavigation()
    object DetailFragment : MainNavigation()
    object AddEditActivity : MainNavigation()
    object AddEditFragment : MainNavigation()
    object SearchActivity : MainNavigation()
    object SearchFragment : MainNavigation()
    object MapsActivity : MainNavigation()
    object MapsFragment : MainNavigation()
    object LoanActivity : MainNavigation()
    object LoanFragment : MainNavigation()
}

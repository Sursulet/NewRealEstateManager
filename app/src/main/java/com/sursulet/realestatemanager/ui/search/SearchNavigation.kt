package com.sursulet.realestatemanager.ui.search

sealed class SearchNavigation {
    object MainActivity : SearchNavigation()
    object MainFragment: SearchNavigation()
}

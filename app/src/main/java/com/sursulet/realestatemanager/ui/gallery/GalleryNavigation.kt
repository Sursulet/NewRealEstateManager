package com.sursulet.realestatemanager.ui.gallery

sealed class GalleryNavigation {
    object MainActivity : GalleryNavigation()
    object MainFragment: GalleryNavigation()
    object DetailActivity : GalleryNavigation()
    object DetailFragment : GalleryNavigation()
}

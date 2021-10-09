package com.sursulet.realestatemanager.ui.addedit

sealed class AddEditNavigation {
    object GalleryActivity : AddEditNavigation()
    object GalleryFragment : AddEditNavigation()
}

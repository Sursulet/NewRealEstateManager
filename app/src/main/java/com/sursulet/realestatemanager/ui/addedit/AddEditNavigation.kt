package com.sursulet.realestatemanager.ui.addedit

sealed class AddEditNavigation {
    object DetailActivity : AddEditNavigation()
    object DetailFragment : AddEditNavigation()
    object GalleryDialogFragment : AddEditNavigation()
    data class Message(val value: String) : AddEditNavigation()
}

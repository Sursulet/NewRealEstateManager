package com.sursulet.realestatemanager.data

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.sursulet.realestatemanager.data.local.model.Address
import com.sursulet.realestatemanager.data.local.model.Photo
import com.sursulet.realestatemanager.data.local.model.RealEstate
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.net.HttpURLConnection
import java.net.URL
import javax.inject.Inject
import javax.inject.Provider

class Callback @Inject constructor(
    private val database: Provider<RealEstateDatabase>,
    private val applicationScope: CoroutineScope
) : RoomDatabase.Callback() {

    override fun onCreate(db: SupportSQLiteDatabase) {
        super.onCreate(db)

        val realEstateDao = database.get().realEstateDao()
        val photoDao = database.get().photoDao()

        applicationScope.launch {
            val urlPhotos = listOf<String>(
                "https://cdn.pixabay.com/photo/2016/06/24/10/47/house-1477041_1280.jpg",
                "https://cdn.pixabay.com/photo/2017/03/22/17/39/kitchen-2165756_1280.jpg"
            )

            var uri = Uri.parse("https://cdn.pixabay.com/photo/2016/06/24/10/47/house-1477041_1280.jpg")

            realEstateDao.insert(
                RealEstate(
                    id = 1,
                    type = "Penthouse",
                    price = 2987200f,
                    description = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Aenean aliquam in ex vitae mollis. Duis semper lobortis gravida. Nam imperdiet sodales nisl. Donec cursus, erat nec fermentum consequat, urna orci blandit enim, at volutpat nunc est non nulla. Nunc commodo blandit neque, lacinia molestie dui pellentesque nec. Proin ultrices ex quis porttitor dignissim. Praesent tempor ex urna, ac accumsan eros finibus sed. Etiam accumsan imperdiet dictum.",
                    address = Address(
                        street = "Rue de Rivoli",
                        postCode = "75001",
                        city = "Paris",
                        country = "FRANCE",
                        extras = "",
                        state = ""
                    ),//"740 Park Avenue, Appt 6/7A, New York, NY 10021, United States",
                    surface = 750.0,
                    rooms = 8,
                    bathrooms = 2,
                    bedrooms = 2,
                    nearest = "school",
                    sold = null,
                    agent = "PEACH"
                )
            )

            photoDao.insert(
                Photo(
                    id = 1,
                    title = "Three",
                    realEstateId = 1,
                    image = getBitmapFromURL("https://cdn.pixabay.com/photo/2013/10/09/02/27/lake-192990_1280.jpg")
                )
            )

            photoDao.insert(
                Photo(
                    id = 22,
                    title = "Kitchen",
                    image = getBitmapFromURL("https://cdn.pixabay.com/photo/2017/02/24/12/22/kitchen-2094707_1280.jpg"),
                    realEstateId = 1
                )
            )
        }
    }

    private fun getBitmapFromURL(src: String): Bitmap {
        val url = URL(src)
        val connection = url.openConnection() as HttpURLConnection
        connection.doInput = true
        connection.connect()
        val input = connection.inputStream
        return BitmapFactory.decodeStream(input)
    }
}
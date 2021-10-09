package com.sursulet.realestatemanager.data.local.dao

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.google.common.truth.Truth
import com.sursulet.realestatemanager.data.RealEstateDatabase
import com.sursulet.realestatemanager.data.local.model.Photo
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
@SmallTest
class PhotoDaoTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var db: RealEstateDatabase
    private lateinit var dao: PhotoDao

    @Before
    fun setup() {
        db = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            RealEstateDatabase::class.java
        ).allowMainThreadQueries().build()

        dao = db.photoDao()
    }

    @After
    fun teardown() {
        db.close()
    }

    @Test
    fun insertPhotos() = runBlockingTest {
        val item1 = Photo(1, "Flat", mockk(),2)
        val item2 = Photo(2, "Penthouse", mockk(),1)
        val item3 = Photo(3, "Duplex", mockk(),1)

        val items = listOf(item1, item2, item3)

        dao.insertAll(items)

        val list = dao.getPhotos(1)

        Truth.assertThat(list.size).isEqualTo(2)
        Truth.assertThat(list[0]).isEqualTo(item2)
        Truth.assertThat(list[1]).isEqualTo(item3)
    }

    @Test
    fun insertPhoto() = runBlockingTest {
        val bitmap: Bitmap = mockk()
        val byteArray = "Bitmap".toByteArray(Charsets.UTF_8)
        every { bitmap.compress(Bitmap.CompressFormat.PNG,100, any()) } returns true
        every { BitmapFactory.decodeByteArray(byteArray,0,any()) } returns bitmap
        val item = Photo(1, "Flat", bitmap,1)
        dao.insert(item)

        val all = dao.getPhotos(1)

        Truth.assertThat(all).contains(item)
    }

    @Test
    fun deletePhoto() = runBlockingTest {
        val item = Photo(1, "Flat", mockk(),1)
        dao.insert(item)
        dao.delete(item)

        val all = dao.getPhotos(1)

        Truth.assertThat(all).doesNotContain(item)
    }
}
package com.sursulet.realestatemanager.data.local.dao

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.google.common.truth.Truth.assertThat
import com.sursulet.realestatemanager.data.RealEstateDatabase
import com.sursulet.realestatemanager.data.local.model.RealEstate
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
class RealEstateDaoTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var db: RealEstateDatabase
    private lateinit var dao: RealEstateDao

    @Before
    fun setup() {
        db = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            RealEstateDatabase::class.java
        ).allowMainThreadQueries().build()

        dao = db.realEstateDao()
    }

    @After
    fun teardown() {
        db.close()
    }

    @Test
    fun insertRealEstate() = runBlockingTest {
        val item = RealEstate(1, "Flat", 100f)
        dao.insert(item)

        val list = dao.getRealEstates()

        assertThat(list.size).isEqualTo(1)
        assertThat(list).contains(item)
    }

    @Test
    fun updateRealEstate() = runBlockingTest {
        val item = RealEstate(1, "Flat", 100f)
        dao.insert(item)
        val newItem = item.copy(price = 230f)
        dao.update(newItem)

        val list = dao.getRealEstates()

        assertThat(list.size).isEqualTo(1)
        assertThat(list).doesNotContain(item)
        assertThat(list).contains(newItem)
    }
}
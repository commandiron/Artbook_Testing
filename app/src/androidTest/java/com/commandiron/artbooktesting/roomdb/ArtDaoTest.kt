package com.commandiron.artbooktesting.roomdb

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.filters.SmallTest
import com.commandiron.artbooktesting.getOrAwaitValue
import com.commandiron.artbooktesting.model.Art
import com.google.common.truth.Truth.assertThat
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject
import javax.inject.Named

@SmallTest
@ExperimentalCoroutinesApi
@HiltAndroidTest
class ArtDaoTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Inject
    @Named("testDatabase")
    lateinit var database : ArtDatabase

    private lateinit var dao: ArtDao

    @Before
    fun setup(){
        hiltRule.inject()

        dao = database.artDao()
    }


    @After
    fun teardown(){
        database.close()
    }

    @Test
    fun insertArtTesting() = runBlockingTest {

        val exampleArt = Art("Test", "Test_Artist_Name",1989,"",1)
        dao.insertArt(exampleArt)

        val list = dao.getAllArts().getOrAwaitValue()

        assertThat(list).contains(exampleArt)
    }

    @Test
    fun deleteArtTesting() = runBlockingTest {
        val exampleArt = Art("Test", "Test_Artist_Name",1989,"",1)
        dao.insertArt(exampleArt)

        dao.deleteSingleArt(exampleArt)

        val list = dao.getAllArts().getOrAwaitValue()

        assertThat(list).doesNotContain(list)
    }
}
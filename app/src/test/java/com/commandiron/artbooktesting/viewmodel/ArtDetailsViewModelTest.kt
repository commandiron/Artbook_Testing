package com.commandiron.artbooktesting.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.commandiron.artbooktesting.MainCoroutineRule
import com.commandiron.artbooktesting.getOrAwaitValueTest
import com.commandiron.artbooktesting.repo.FakeArtsRepository
import com.commandiron.artbooktesting.util.Status
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class ArtDetailsViewModelTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    private lateinit var artDetailsViewModel: ArtDetailsViewModel

    @Before
    fun setup(){
        artDetailsViewModel = ArtDetailsViewModel(FakeArtsRepository())
    }

    @Test
    fun `insert art without year returns error` () {
        artDetailsViewModel.makeArt("Command Iron", "Snake", "")
        val value = artDetailsViewModel.insertArtMessage.getOrAwaitValueTest()
        assertThat(value.status).isEqualTo(Status.ERROR)
    }

    @Test
    fun `insert art without name returns error` () {
        artDetailsViewModel.makeArt("", "Snake", "1989")
        val value = artDetailsViewModel.insertArtMessage.getOrAwaitValueTest()
        assertThat(value.status).isEqualTo(Status.ERROR)
    }

    @Test
    fun `insert art without artistName returns error` () {
        artDetailsViewModel.makeArt("Command Iron", "", "1989")
        val value = artDetailsViewModel.insertArtMessage.getOrAwaitValueTest()
        assertThat(value.status).isEqualTo(Status.ERROR)
    }
}
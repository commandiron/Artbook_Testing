package com.commandiron.artbooktesting.view

import android.view.View
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.filters.MediumTest
import com.commandiron.artbooktesting.R
import com.commandiron.artbooktesting.adapter.ImageRecyclerViewAdapter
import com.commandiron.artbooktesting.fragmentfactory.ArtsFragmentFactory
import com.commandiron.artbooktesting.launchFragmentInHiltContainer
import com.commandiron.artbooktesting.model.Art
import com.commandiron.artbooktesting.repo.FakeTestArtsRepository
import com.commandiron.artbooktesting.viewmodel.ArtDetailsViewModel
import com.commandiron.artbooktesting.viewmodel.ImageApiViewModel
import com.google.common.truth.Truth.assertThat
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito
import javax.inject.Inject

@MediumTest
@HiltAndroidTest
@ExperimentalCoroutinesApi
class ImageApiFragmentTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get: Rule
    var hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var fragmentFactory: ArtsFragmentFactory

    @Before
    fun setup(){
        hiltRule.inject()
    }

    @Test
    fun selectImageTest(){

        val navController = Mockito.mock(NavController::class.java)
        val selectedImageURl = "emirdemirli.com"
        val testViewModel = ImageApiViewModel(FakeTestArtsRepository())

        launchFragmentInHiltContainer<ImageApiFragment>(factory = fragmentFactory){
            Navigation.setViewNavController(requireView(),navController)
            viewModel = testViewModel

            imageRecyclerViewAdapter.images = listOf(selectedImageURl)
        }

        Espresso.onView(ViewMatchers.withId(R.id.searchArtRecyclerView)).perform(
            RecyclerViewActions.actionOnItemAtPosition<ImageRecyclerViewAdapter.ImageViewHolder>(0, ViewActions.click())
        )

        Mockito.verify(navController).navigate(
            ImageApiFragmentDirections.actionImageApiFragmentToArtDetailsFragment(selectedImageURl,"","","")
        )
    }

}
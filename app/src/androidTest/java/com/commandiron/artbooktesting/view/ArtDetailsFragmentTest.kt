package com.commandiron.artbooktesting.view

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.filters.MediumTest
import com.commandiron.artbooktesting.R
import com.commandiron.artbooktesting.fragmentfactory.ArtsFragmentFactory
import com.commandiron.artbooktesting.getOrAwaitValue
import com.commandiron.artbooktesting.launchFragmentInHiltContainer
import com.commandiron.artbooktesting.model.Art
import com.commandiron.artbooktesting.repo.FakeTestArtsRepository
import com.commandiron.artbooktesting.roomdb.ArtDao
import com.commandiron.artbooktesting.roomdb.ArtDatabase
import com.commandiron.artbooktesting.viewmodel.ArtDetailsViewModel
import com.commandiron.artbooktesting.viewmodel.ArtsViewModel
import com.google.common.truth.Truth.assertThat
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito
import javax.inject.Inject
import javax.inject.Named

@MediumTest
@HiltAndroidTest
@ExperimentalCoroutinesApi
class ArtDetailsFragmentTest {

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
    fun testNavigationFromArtDetailsToImageAPI(){

        val navController = Mockito.mock(NavController::class.java)

        launchFragmentInHiltContainer<ArtDetailsFragment>(factory = fragmentFactory){
            Navigation.setViewNavController(requireView(),navController)
        }

        Espresso.onView(ViewMatchers.withId(R.id.artImageView)).perform(ViewActions.click())

        Mockito.verify(navController).navigate(
           ArtDetailsFragmentDirections.actionArtDetailsFragmentToImageApiFragment()
        )
    }

    @Test
    fun testOnBackPressed(){

        val navController = Mockito.mock(NavController::class.java)

        launchFragmentInHiltContainer<ArtDetailsFragment>(factory = fragmentFactory){
            Navigation.setViewNavController(requireView(),navController)
        }

        Espresso.pressBack()

        Mockito.verify(navController).popBackStack()
    }

    @Test
    fun testSave(){

        val testViewModel = ArtDetailsViewModel(FakeTestArtsRepository())

        launchFragmentInHiltContainer<ArtDetailsFragment>(factory = fragmentFactory){
            viewModel = testViewModel
        }

        Espresso.onView(ViewMatchers.withId(R.id.artNameEditText)).perform(ViewActions.replaceText("Mona Lisa"))
        Espresso.onView(ViewMatchers.withId(R.id.artArtistNameEditText)).perform(ViewActions.replaceText("Da Vinci"))
        Espresso.onView(ViewMatchers.withId(R.id.artYearEditText)).perform(ViewActions.replaceText("1500"))
        Espresso.onView(ViewMatchers.withId(R.id.art_save)).perform(ViewActions.click())

        assertThat(testViewModel.artList.getOrAwaitValue()).contains(
            Art("Mona Lisa","Da Vinci",1500,"")
        )
    }
}
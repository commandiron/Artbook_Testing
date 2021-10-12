package com.commandiron.artbooktesting.viewmodel

import com.commandiron.artbooktesting.repo.FakeArtsRepository
import org.junit.Before

class ImageApiViewModelTest {

    private lateinit var imageApiViewModel: ImageApiViewModel

    @Before
    fun setup(){
        imageApiViewModel = ImageApiViewModel(FakeArtsRepository())
    }
}
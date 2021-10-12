package com.commandiron.artbooktesting.viewmodel

import com.commandiron.artbooktesting.repo.ArtsRepository
import com.commandiron.artbooktesting.repo.FakeArtsRepository
import org.junit.Before
import org.junit.Test

class ArtsViewModelTest {

    private lateinit var artsViewModel: ArtsViewModel

    @Before
    fun setup(){
        artsViewModel = ArtsViewModel(FakeArtsRepository())
    }


}
package com.commandiron.artbooktesting.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.commandiron.artbooktesting.model.Art
import com.commandiron.artbooktesting.repo.ArtsRepositoryInterface
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ArtsViewModel @Inject constructor(
    private val repository: ArtsRepositoryInterface
): ViewModel()  {

    val artList = repository.getAllArts()

    fun deleteSingleArt(art: Art) = viewModelScope.launch {
        repository.deleteSingleArt(art)
    }
}
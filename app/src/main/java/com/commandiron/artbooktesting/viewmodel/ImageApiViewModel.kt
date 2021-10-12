package com.commandiron.artbooktesting.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.commandiron.artbooktesting.model.ImageResponse
import com.commandiron.artbooktesting.repo.ArtsRepositoryInterface
import com.commandiron.artbooktesting.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ImageApiViewModel @Inject constructor(
    private val repository: ArtsRepositoryInterface
): ViewModel()  {

    private val images = MutableLiveData<Resource<ImageResponse>>()
    val imageList : LiveData<Resource<ImageResponse>>
        get() = images

    fun searchImage(searchString: String){
        if(searchString.isEmpty()){
            return
        }

        images.value = Resource.loading(null)
        viewModelScope.launch {
            val response = repository.searchImage(searchString)
            images.value = response
        }
    }
}
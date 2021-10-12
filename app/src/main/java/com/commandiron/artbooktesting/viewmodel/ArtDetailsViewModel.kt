package com.commandiron.artbooktesting.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.commandiron.artbooktesting.model.Art
import com.commandiron.artbooktesting.repo.ArtsRepositoryInterface
import com.commandiron.artbooktesting.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class ArtDetailsViewModel @Inject constructor(
    private val repository: ArtsRepositoryInterface
): ViewModel()  {

    val artList = repository.getAllArts()

    private val selectedImage = MutableLiveData<String>()
    val selectedImageUrl : LiveData<String>
        get() = selectedImage

    private var insertArtMsg = MutableLiveData<Resource<Art>>()
    val insertArtMessage : LiveData<Resource<Art>>
        get() = insertArtMsg

    fun resetInsertArtMsg(){
        insertArtMsg = MutableLiveData<Resource<Art>>()
    }

    fun setSelectedImage(url: String){
        selectedImage.postValue(url)
    }

    fun makeArt(name: String, artistName: String, year: String){
        if(name.isEmpty() || artistName.isEmpty() || year.isEmpty()){
            insertArtMsg.postValue(Resource.error("Enter name, artist, year", null))
            return
        }

        val yearInt = try {
            year.toInt()
        }catch (e: Exception){
            insertArtMsg.postValue(Resource.error("Year should be number", null))
            return
        }

        val art = Art(name, artistName, yearInt, selectedImage.value ?: "",)
        insertArt(art)
        setSelectedImage("")
        insertArtMsg.postValue(Resource.success(art))
    }
    fun insertArt(art: Art) = viewModelScope.launch {
        repository.insertArt(art)
    }
}
package com.commandiron.artbooktesting.repo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.commandiron.artbooktesting.model.Art
import com.commandiron.artbooktesting.model.ImageResponse
import com.commandiron.artbooktesting.util.Resource

class FakeTestArtsRepository: ArtsRepositoryInterface {

    private val arts = mutableListOf<Art>()
    private val artsLiveData = MutableLiveData<List<Art>>(arts)

    override suspend fun insertArt(art: Art) {
        arts.add(art)
        refleshData()
    }

    override suspend fun deleteSingleArt(art: Art) {
        arts.remove(art)
        refleshData()
    }

    override fun getAllArts(): LiveData<List<Art>> {
        return artsLiveData
    }

    override suspend fun searchImage(imageString: String): Resource<ImageResponse> {
        return Resource.success(ImageResponse(listOf(),0,0))
    }

    private fun refleshData(){
        artsLiveData.postValue(arts)
    }
}
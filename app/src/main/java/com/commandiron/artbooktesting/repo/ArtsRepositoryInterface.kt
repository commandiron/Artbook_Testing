package com.commandiron.artbooktesting.repo

import androidx.lifecycle.LiveData
import com.commandiron.artbooktesting.model.Art
import com.commandiron.artbooktesting.model.ImageResponse
import com.commandiron.artbooktesting.util.Resource

interface ArtsRepositoryInterface {

    suspend fun insertArt(art: Art)
    suspend fun deleteSingleArt(art: Art)
    fun getAllArts() : LiveData<List<Art>>
    suspend fun searchImage(imageString: String): Resource<ImageResponse>

}
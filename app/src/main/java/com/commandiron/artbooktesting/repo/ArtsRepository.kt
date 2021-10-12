package com.commandiron.artbooktesting.repo

import androidx.lifecycle.LiveData
import com.commandiron.artbooktesting.api.RetrofitApi
import com.commandiron.artbooktesting.model.Art
import com.commandiron.artbooktesting.model.ImageResponse
import com.commandiron.artbooktesting.roomdb.ArtDao
import com.commandiron.artbooktesting.util.Resource
import retrofit2.Response
import java.lang.Exception
import javax.inject.Inject

class ArtsRepository@Inject constructor(
    private val artDao: ArtDao,
    private val retrofitApi: RetrofitApi): ArtsRepositoryInterface {

    override suspend fun insertArt(art: Art) {
        artDao.insertArt(art)
    }

    override suspend fun deleteSingleArt(art: Art) {
        artDao.deleteSingleArt(art)
    }

    override fun getAllArts(): LiveData<List<Art>> = artDao.getAllArts()

    override suspend fun searchImage(imageString: String): Resource<ImageResponse>{
        return try {
            val response = retrofitApi.imageSearch(imageString)
            if(response.isSuccessful){
                response.body()?.let {
                    return@let Resource.success(it)
                } ?: Resource.error("Error", null)
            }else{
                Resource.error("Error", null)
            }
        }catch (e: Exception){
            Resource.error("No data!", null)
        }
    }

}
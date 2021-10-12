package com.commandiron.artbooktesting.roomdb

import androidx.lifecycle.LiveData
import androidx.room.*
import com.commandiron.artbooktesting.model.Art

@Dao
interface ArtDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertArt(art: Art)

    @Delete
    suspend fun deleteSingleArt(art: Art)

    @Query("SELECT * FROM arts")
    fun getAllArts(): LiveData<List<Art>>

}

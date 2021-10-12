package com.commandiron.artbooktesting.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "arts")
data class Art (

    val artName: String,
    val artArtistName: String,
    val artYear: Int,
    val artImageUrl: String,

    @PrimaryKey(autoGenerate = true)
    val id: Int? = null

    )
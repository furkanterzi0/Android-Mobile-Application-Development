package com.furkanterzi.roomanddatabase.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Recipe(
    @ColumnInfo(name = "name")
    var name: String,

    @ColumnInfo(name = "food_detail")
    var foodDetail: String,

    @ColumnInfo(name = "image")
    var image: ByteArray
){
    @PrimaryKey(autoGenerate = true)
    var id = 0
}
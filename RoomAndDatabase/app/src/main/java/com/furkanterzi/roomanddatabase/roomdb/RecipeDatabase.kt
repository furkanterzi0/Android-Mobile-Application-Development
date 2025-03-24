package com.furkanterzi.roomanddatabase.roomdb

import androidx.room.Database
import androidx.room.RoomDatabase
import com.furkanterzi.roomanddatabase.model.Recipe

@Database(entities = [Recipe::class], version = 1)
abstract class RecipeDatabase : RoomDatabase() {
    abstract fun recipeDAO(): RecipeDAO
}
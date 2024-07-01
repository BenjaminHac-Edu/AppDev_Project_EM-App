package com.example.fussball_em_2024_app.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.fussball_em_2024_app.entity.FavouriteTeam

@Database(entities = [FavouriteTeam::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun favouriteTeamDao() : FavouriteTeamDao
}
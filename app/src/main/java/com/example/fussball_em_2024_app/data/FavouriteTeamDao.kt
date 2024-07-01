package com.example.fussball_em_2024_app.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.fussball_em_2024_app.entity.FavouriteTeam

@Dao
interface FavouriteTeamDao {
    @Query("SELECT * FROM favouriteTeam WHERE leagueName LIKE :league")
    fun findByLeagueName(league: String): List<FavouriteTeam>

    @Query("SELECT * FROM favouriteTeam WHERE leagueName LIKE :league AND teamName LIKE :teamName")
    fun findByLeagueAndTeamName(league: String, teamName: String) : FavouriteTeam?

    @Insert
    fun insert(vararg favouriteTeam: FavouriteTeam)

    @Delete
    fun delete(favouriteTeam: FavouriteTeam)
}
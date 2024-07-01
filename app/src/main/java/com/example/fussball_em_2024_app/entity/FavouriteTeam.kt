package com.example.fussball_em_2024_app.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity
data class FavouriteTeam(
    @PrimaryKey(autoGenerate = true) val ftid: Int = 0,
    @ColumnInfo(name = "leagueName") val leagueName: String,
    @ColumnInfo(name = "teamName") val teamName: String
)

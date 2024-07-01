package com.example.fussball_em_2024_app

import com.example.fussball_em_2024_app.model.Match
import com.example.fussball_em_2024_app.model.Team
import com.example.fussball_em_2024_app.model.TeamInfo
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path


val httpLoggingInterceptor = HttpLoggingInterceptor().apply {
    level = HttpLoggingInterceptor.Level.BODY
}

val okHttpClient = OkHttpClient.Builder()
    .addInterceptor(httpLoggingInterceptor)
    .build()
val gson = GsonBuilder()
    .setDateFormat("yyyy-MM-dd'T'HH:mm:ss") // The format should correspond to the date string
    .create()

val retrofit = Retrofit.Builder()
    .baseUrl("https://api.openligadb.de/")
    .client(okHttpClient)
    .addConverterFactory(GsonConverterFactory.create(gson))
    .build()


val matchService = retrofit.create(ApiService::class.java)


interface ApiService {
    @GET("getmatchdata/em/2024/")
    suspend fun getLatestMatch(): List<Match>

    @GET("getmatchdata/{id}")
    suspend fun getMatch(@Path("id") id: Int):Match?

    @GET("getnextmatchbyleagueshortcut/EM")
    suspend fun getNextMatch():Match

    @GET("getlastmatchbyleagueshortcut/EM")
    suspend fun getLastMatch():Match

    @GET("getavailableteams/EM/2024")
    suspend fun getTeams():List<Team>

    @GET("getbltable/EM/2024")
    suspend fun getTeamsDetails():List<TeamInfo>

    @GET("getnextmatchbyleagueteam/4708/{id}")
    suspend fun getNextMatchByTeam(@Path("id") id: Int):Match

    @GET("getlastmatchbyleagueteam/4708/{id}")
    suspend fun getLastMatchByTeam(@Path("id")id: Int):Match
}




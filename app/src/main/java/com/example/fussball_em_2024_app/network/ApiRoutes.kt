package com.example.fussball_em_2024_app.network

import com.example.fussball_em_2024_app.model.League
import com.example.fussball_em_2024_app.model.Match
import com.example.fussball_em_2024_app.model.Team
import com.example.fussball_em_2024_app.model.TeamInfo
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiRoutes {
    @GET("getavailableleagues")
    suspend fun getAllLeagues(): List<League>

    @GET("getmatchdata/{leagueShortcut}/{leagueSeason}/")
    suspend fun getLatestMatch(@Path("leagueShortcut") leagueShortcut: String, @Path("leagueSeason") leagueSeason: String): List<Match>

    @GET("getmatchdata/{id}")
    suspend fun getMatch(@Path("id") id: Int): Match?

    @GET("getnextmatchbyleagueshortcut/{leagueShortcut}")
    suspend fun getNextMatch(@Path("leagueShortcut") leagueShortcut: String): Match

    @GET("getlastmatchbyleagueshortcut/{leagueShortcut}")
    suspend fun getLastMatch(@Path("leagueShortcut") leagueShortcut: String): Match

    @GET("getavailableteams/{leagueShortcut}/{leagueSeason}")
    suspend fun getTeams(@Path("leagueShortcut") leagueShortcut: String, @Path("leagueSeason") leagueSeason: String):List<Team>

    @GET("getbltable/{leagueShortcut}/{leagueSeason}")
    suspend fun getTeamsDetails(@Path("leagueShortcut") leagueShortcut: String, @Path("leagueSeason") leagueSeason: String):List<TeamInfo>

    @GET("getnextmatchbyleagueteam/{leagueId}/{id}")
    suspend fun getNextMatchByTeam(@Path("leagueId")leagueId: Int, @Path("id") id: Int): Match

    @GET("getlastmatchbyleagueteam/{leagueId}/{id}")
    suspend fun getLastMatchByTeam(@Path("leagueId")leagueId: Int, @Path("id")id: Int): Match
    @GET("getmatchesbyteam/{teamName}/{pastWeeks}/0")
    suspend fun getLastMatchesByTeam(@Path("teamName") teamName: String, @Path("pastWeeks") pastWeeks: Int): List<Match>
}
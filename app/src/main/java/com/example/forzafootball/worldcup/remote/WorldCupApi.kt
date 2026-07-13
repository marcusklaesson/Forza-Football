package com.example.forzafootball.worldcup.remote

import com.example.forzafootball.worldcup.model.WorldCupTeam
import retrofit2.http.GET

interface WorldCupApi {

    @GET("world-cup-teams.json")
    suspend fun getWorldCupTeams(): List<WorldCupTeam>
}
package com.example.forzafootball.data.remote

import com.example.forzafootball.data.remote.model.WorldCupTeam
import retrofit2.http.GET

interface WorldCupApi {

    @GET("world-cup-teams.json")
    suspend fun getWorldCupTeams(): List<WorldCupTeam>
}


package com.example.forzafootball.data.repository

import com.example.forzafootball.data.remote.NetworkModule
import com.example.forzafootball.data.remote.WorldCupApi
import com.example.forzafootball.data.remote.model.WorldCupTeam
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class WorldCupTeamsRepository(
    private val api: WorldCupApi = NetworkModule.worldCupApi,
) {
    suspend fun getWorldCupTeams(): Result<List<WorldCupTeam>> = withContext(Dispatchers.IO) {
        runCatching { api.getWorldCupTeams() }
    }
}


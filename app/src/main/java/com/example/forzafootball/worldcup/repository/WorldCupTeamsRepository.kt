package com.example.forzafootball.worldcup.repository

import com.example.forzafootball.data.remote.NetworkModule
import com.example.forzafootball.worldcup.model.WorldCupTeam
import com.example.forzafootball.worldcup.remote.WorldCupApi
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class WorldCupTeamsRepository(
    private val api: WorldCupApi = NetworkModule.worldCupApi,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO,
) {
    suspend fun getWorldCupTeams(): Result<List<WorldCupTeam>> = withContext(ioDispatcher) {
        runCatching { api.getWorldCupTeams() }
    }
}
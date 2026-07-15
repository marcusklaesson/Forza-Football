package com.example.forzafootball.worldcup.misc

import com.example.forzafootball.worldcup.model.WorldCupTeam

internal object PreviewSampleData {

    val argentina = WorldCupTeam(
        name = "Argentina",
        teamId = 55373,
        population = 46_000_000,
        worldCupTitles = 3,
        registeredPlayers = 2_700_000,
        worldCupParticipations = 18,
        worldRank = 1,
    )

    val brazil = WorldCupTeam(
        name = "Brazil",
        teamId = 4709,
        population = 216_000_000,
        worldCupTitles = 5,
        registeredPlayers = 13_000_000,
        worldCupParticipations = 22,
        worldRank = 4,
    )

    val france = WorldCupTeam(
        name = "France",
        teamId = 35010,
        population = 68_000_000,
        worldCupTitles = 2,
        registeredPlayers = 2_200_000,
        worldCupParticipations = 16,
        worldRank = 2,
    )

    val teams: List<WorldCupTeam> = listOf(argentina, france, brazil)
}
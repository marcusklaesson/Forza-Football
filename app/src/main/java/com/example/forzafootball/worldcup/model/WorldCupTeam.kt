package com.example.forzafootball.worldcup.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * https://frz-campaign-forzafootball.s3.eu-west-1.amazonaws.com/world-cup-teams.json
 */
@Serializable
data class WorldCupTeam(
    @SerialName("name")
    val name: String,
    @SerialName("team_id")
    val teamId: Long,
    @SerialName("population")
    val population: Long,
    @SerialName("world_cup_titles")
    val worldCupTitles: Int,
    @SerialName("registered_players")
    val registeredPlayers: Long,
    @SerialName("wc_participations")
    val worldCupParticipations: Int,
    @SerialName("world_rank")
    val worldRank: Int,
)
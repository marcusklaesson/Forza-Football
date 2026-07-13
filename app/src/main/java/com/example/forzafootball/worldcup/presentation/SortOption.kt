package com.example.forzafootball.worldcup.presentation

import com.example.forzafootball.worldcup.model.WorldCupTeam

enum class SortOption(
    val label: String,
    val comparator: Comparator<WorldCupTeam>,
) {
    NAME(
        label = "Name (A–Z)",
        comparator = compareBy(String.CASE_INSENSITIVE_ORDER) { it.name },
    ),
    WORLD_RANK(
        label = "World rank",
        comparator = compareBy { it.worldRank },
    ),
    WORLD_CUP_TITLES(
        label = "World Cup titles",
        comparator = compareByDescending { it.worldCupTitles },
    ),
    WC_PARTICIPATIONS(
        label = "WC participations",
        comparator = compareByDescending { it.worldCupParticipations },
    ),
    POPULATION(
        label = "Population",
        comparator = compareByDescending { it.population },
    ),
    REGISTERED_PLAYERS(
        label = "Registered players",
        comparator = compareByDescending { it.registeredPlayers },
    );

    /** Sorts [teams] by this option, using team name as a stable property. */
    fun sort(teams: List<WorldCupTeam>): List<WorldCupTeam> =
        teams.sortedWith(comparator.thenBy(String.CASE_INSENSITIVE_ORDER) { it.name })

    /** The value of this sort property for [team], formatted for display. */
    fun valueOf(team: WorldCupTeam): String = when (this) {
        NAME -> ""
        WORLD_RANK -> "#${team.worldRank}"
        WORLD_CUP_TITLES -> team.worldCupTitles.toString()
        WC_PARTICIPATIONS -> team.worldCupParticipations.toString()
        POPULATION -> formatCompact(team.population)
        REGISTERED_PLAYERS -> formatCompact(team.registeredPlayers)
    }
}

/** Formats large numbers compactly, e.g. 2_700_000 -> "2.7M". */
private fun formatCompact(value: Long): String = when {
    value >= 1_000_000 -> {
        val millions = value / 100_000 / 10.0
        if (millions % 1.0 == 0.0) "${millions.toInt()}M" else "${millions}M"
    }

    value >= 1_000 -> {
        val thousands = value / 100 / 10.0
        if (thousands % 1.0 == 0.0) "${thousands.toInt()}K" else "${thousands}K"
    }

    else -> value.toString()
}


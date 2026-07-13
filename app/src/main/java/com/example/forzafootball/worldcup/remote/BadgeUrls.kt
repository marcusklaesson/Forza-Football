package com.example.forzafootball.worldcup.remote

object BadgeUrls {
    private const val BASE_URL = "https://images.multiball.forzafootball.net/"

    fun teamBadge(teamId: Long): String = "${BASE_URL}badges/team/thumbnail/$teamId.png"
}
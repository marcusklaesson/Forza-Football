package com.example.forzafootball.worldcup.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.forzafootball.core.extension.formatCompact
import com.example.forzafootball.ui.theme.ForzaFootballTheme
import com.example.forzafootball.ui.theme.components.AppBottomSheet
import com.example.forzafootball.ui.theme.components.TeamBadge
import com.example.forzafootball.worldcup.misc.PreviewSampleData
import com.example.forzafootball.worldcup.model.WorldCupTeam
import com.example.forzafootball.worldcup.remote.BadgeUrls

@Composable
fun TeamDetailsBottomSheet(
    team: WorldCupTeam?,
    onDismiss: () -> Unit,
) {
    AppBottomSheet(
        visible = team != null,
        onDismiss = onDismiss,
    ) {
        team ?: return@AppBottomSheet
        TeamDetailsContent(team = team)
    }
}

@Composable
private fun TeamDetailsContent(team: WorldCupTeam) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
            .padding(bottom = 32.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            TeamBadge(
                teamName = team.name,
                imageUrl = BadgeUrls.teamBadge(team.teamId),
                size = 56.dp,
            )
            Text(
                team.name,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
        }
        HorizontalDivider()
        DetailRow("World rank", "#${team.worldRank}")
        DetailRow("World Cup titles", team.worldCupTitles.toString())
        DetailRow("Participations", team.worldCupParticipations.toString())
        DetailRow("Population", formatCompact(team.population))
        DetailRow("Registered players", formatCompact(team.registeredPlayers))
    }
}

@Composable
private fun DetailRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.SemiBold,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun TeamDetailsContentPreview() {
    ForzaFootballTheme {
        TeamDetailsContent(team = PreviewSampleData.argentina)
    }
}


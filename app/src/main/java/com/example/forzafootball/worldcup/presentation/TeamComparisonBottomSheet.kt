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
import androidx.compose.ui.text.style.TextAlign
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
fun TeamComparisonBottomSheet(
    teams: List<WorldCupTeam>,
    onDismiss: () -> Unit,
) {
    AppBottomSheet(
        visible = teams.size == 2,
        onDismiss = onDismiss,
    ) {
        val (left, right) = teams
        TeamComparisonContent(left = left, right = right)
    }
}

@Composable
private fun TeamComparisonContent(left: WorldCupTeam, right: WorldCupTeam) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .padding(bottom = 32.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Row(modifier = Modifier.fillMaxWidth()) {
            TeamHeader(left, Modifier.weight(1f))
            TeamHeader(right, Modifier.weight(1f))
        }

        HorizontalDivider()

        ComparisonRow("World rank", "#${left.worldRank}", "#${right.worldRank}")
        ComparisonRow("World Cup Titles", "${left.worldCupTitles}", "${right.worldCupTitles}")
        ComparisonRow(
            "Participations",
            "${left.worldCupParticipations}",
            "${right.worldCupParticipations}"
        )
        ComparisonRow(
            "Population",
            formatCompact(left.population),
            formatCompact(right.population)
        )
        ComparisonRow(
            "Registered players",
            formatCompact(left.registeredPlayers),
            formatCompact(right.registeredPlayers)
        )
    }
}

@Composable
private fun TeamHeader(team: WorldCupTeam, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        TeamBadge(
            teamName = team.name,
            imageUrl = BadgeUrls.teamBadge(team.teamId),
            size = 56.dp,
        )
        Text(
            text = team.name,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
        )
    }
}

@Composable
private fun ComparisonRow(label: String, leftValue: String, rightValue: String) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center,
        )
        Row(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = leftValue,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.SemiBold,
                textAlign = TextAlign.Center,
                modifier = Modifier.weight(1f),
            )
            Text(
                text = rightValue,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.SemiBold,
                textAlign = TextAlign.Center,
                modifier = Modifier.weight(1f),
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun TeamComparisonContentPreview() {
    ForzaFootballTheme {
        TeamComparisonContent(
            left = PreviewSampleData.argentina,
            right = PreviewSampleData.brazil,
        )
    }
}


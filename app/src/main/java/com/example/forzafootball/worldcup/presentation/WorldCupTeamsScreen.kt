package com.example.forzafootball.worldcup.presentation

import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.forzafootball.ui.theme.ForzaFootballTheme
import com.example.forzafootball.ui.theme.components.ErrorScreen
import com.example.forzafootball.ui.theme.components.Loading
import com.example.forzafootball.ui.theme.components.TeamBadge
import com.example.forzafootball.worldcup.misc.PreviewSampleData
import com.example.forzafootball.worldcup.model.WorldCupTeam
import com.example.forzafootball.worldcup.remote.BadgeUrls

@Composable
fun WorldCupTeamsScreen(
    modifier: Modifier = Modifier,
    viewModel: WorldCupViewModel = viewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Box(modifier = modifier.fillMaxSize()) {
        when {
            uiState.isLoading -> Loading()

            uiState.errorMessage != null -> {
                ErrorScreen(
                    text = uiState.errorMessage,
                    onRetry = viewModel::loadTeams,
                )
            }

            uiState.teams.isEmpty() -> EmptyState()

            else -> {
                TeamsList(
                    teams = uiState.teams,
                    sortOption = uiState.sortOption,
                    comparisonTeams = uiState.comparisonTeams,
                    onSortOptionSelected = viewModel::onSortOptionSelected,
                    onTeamClicked = viewModel::onTeamSelected,
                    onTeamLongPressed = viewModel::onTeamLongPressed,
                )
            }
        }
    }

    TeamDetailsBottomSheet(
        team = uiState.selectedTeam,
        onDismiss = viewModel::onTeamDetailsDismissed,
    )

    TeamComparisonBottomSheet(
        teams = uiState.comparisonTeams,
        onDismiss = viewModel::onComparisonDismissed,
    )
}

@Composable
fun TeamsList(
    teams: List<WorldCupTeam>,
    sortOption: SortOption,
    comparisonTeams: List<WorldCupTeam>,
    onSortOptionSelected: (SortOption) -> Unit,
    onTeamClicked: (WorldCupTeam) -> Unit,
    onTeamLongPressed: (WorldCupTeam) -> Unit,
) {
    val listState = rememberLazyListState()

    LaunchedEffect(sortOption) {
        listState.scrollToItem(0)
    }

    Column(modifier = Modifier.fillMaxSize()) {
        SortDropdown(
            selected = sortOption,
            onOptionSelected = onSortOptionSelected,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
        )

        Text(
            text = "Tap a team for details · long-press two teams to compare",
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(horizontal = 16.dp),
        )

        LazyColumn(
            state = listState,
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(8.dp),
        ) {
            items(teams, key = { it.teamId }) { team ->
                TeamRow(
                    team = team,
                    sortOption = sortOption,
                    isSelectedForComparison = comparisonTeams.any { it.teamId == team.teamId },
                    onClick = { onTeamClicked(team) },
                    onLongClick = { onTeamLongPressed(team) },
                )
                HorizontalDivider()
            }
        }
    }
}

@Composable
private fun TeamRow(
    team: WorldCupTeam,
    sortOption: SortOption,
    isSelectedForComparison: Boolean,
    onClick: () -> Unit,
    onLongClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .combinedClickable(
                onClick = onClick,
                onLongClick = onLongClick,
            )
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        TeamBadge(
            teamName = team.name,
            imageUrl = BadgeUrls.teamBadge(team.teamId),
        )
        Text(
            text = team.name,
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.weight(1f),
        )
        if (isSelectedForComparison) {
            Icon(
                imageVector = Icons.Default.CheckCircle,
                contentDescription = "Selected for comparison",
                tint = MaterialTheme.colorScheme.primary,
            )
        }
        Text(
            text = sortOption.valueOf(team),
            style = MaterialTheme.typography.titleMedium,
        )
    }
}

@Composable
private fun EmptyState() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = "No teams available",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun TeamsListPreview() {
    ForzaFootballTheme {
        TeamsList(
            teams = PreviewSampleData.teams,
            sortOption = SortOption.WORLD_RANK,
            comparisonTeams = PreviewSampleData.teams.take(1),
            onSortOptionSelected = {},
            onTeamClicked = {},
            onTeamLongPressed = {},
        )
    }
}


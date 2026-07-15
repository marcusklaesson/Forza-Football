package com.example.forzafootball.worldcup.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
                    onRetryCallback = viewModel::loadTeams,
                )
            }

            else -> {
                TeamsList(
                    uiState = uiState,
                    onSortDropdownCallback = viewModel::onSortOptionSelected,
                    onTeamLongPressed = viewModel::onTeamLongPressed,
                    onComparisonDismissed = viewModel::onComparisonDismissed
                )
            }
        }
    }
}

@Composable
fun TeamsList(
    uiState: WorldCupUiState,
    onSortDropdownCallback: (SortOption) -> Unit = {},
    onTeamLongPressed: (WorldCupTeam) -> Unit = {},
    onComparisonDismissed: () -> Unit = {},
) {
    val listState = rememberLazyListState()
    var selectedTeam by remember { mutableStateOf<WorldCupTeam?>(null) }

    LaunchedEffect(uiState.sortOption) {
        listState.scrollToItem(0)
    }

    Column(modifier = Modifier.fillMaxSize()) {
        SortDropdown(
            selected = uiState.sortOption,
            onOptionSelected = onSortDropdownCallback,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
        )

        LazyColumn(
            state = listState,
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(8.dp),
        ) {
            items(uiState.teams, key = { it.teamId }) { team ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .combinedClickable(
                            onClick = { selectedTeam = team },
                            onLongClick = { onTeamLongPressed(team) },
                        )
                        .background(
                            if (uiState.comparisonTeams.any { it.teamId == team.teamId }) {
                                MaterialTheme.colorScheme.inversePrimary
                            } else {
                                MaterialTheme.colorScheme.surface
                            }
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
                    Column(horizontalAlignment = Alignment.End) {
                        Text(
                            text = uiState.sortOption.valueOf(team),
                            style = MaterialTheme.typography.titleMedium,
                        )
                    }
                }
                HorizontalDivider()
            }
        }
    }

    TeamDetailsBottomSheet(
        team = selectedTeam,
        onDismiss = { selectedTeam = null },
    )

    TeamComparisonBottomSheet(
        teams = uiState.comparisonTeams,
        onDismiss = onComparisonDismissed,
    )
}

@Preview(showBackground = true)
@Composable
private fun TeamsListPreview() {
    ForzaFootballTheme {
        TeamsList(
            uiState = WorldCupUiState(
                teams = PreviewSampleData.teams,
                sortOption = SortOption.WORLD_RANK,
            ),
        )
    }
}


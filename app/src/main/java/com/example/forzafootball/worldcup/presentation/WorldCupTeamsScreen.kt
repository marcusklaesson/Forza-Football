package com.example.forzafootball.worldcup.presentation

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
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.forzafootball.worldcup.remote.BadgeUrls

@Composable
fun WorldCupTeamsScreen(
    modifier: Modifier = Modifier,
    viewModel: WorldCupViewModel = viewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Box(modifier = modifier.fillMaxSize()) {
        when {
            uiState.isLoading -> {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }

            uiState.errorMessage != null -> {
                ErrorScreen(
                    text = uiState.errorMessage,
                    onRetryCallback = viewModel::loadTeams,
                )
            }

            else -> {
                TeamsList(
                    uiState = uiState,
                    onSortDropdownCallback = viewModel::onSortOptionSelected
                )
            }
        }
    }
}

@Composable
fun TeamsList(uiState: WorldCupUiState, onSortDropdownCallback: (SortOption) -> Unit = {}) {
    val listState = rememberLazyListState()

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
                    modifier = Modifier.padding(8.dp),
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
}

@Composable
fun ErrorScreen(text: String?, onRetryCallback: () -> Unit = {}) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Text(
                text = text ?: "",
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.error,
            )
            Button(onClick = onRetryCallback) {
                Text("Retry")
            }
        }
    }
}


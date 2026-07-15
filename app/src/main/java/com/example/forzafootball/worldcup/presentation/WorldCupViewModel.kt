package com.example.forzafootball.worldcup.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.forzafootball.worldcup.model.WorldCupTeam
import com.example.forzafootball.worldcup.repository.WorldCupTeamsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class WorldCupUiState(
    val isLoading: Boolean = false,
    val teams: List<WorldCupTeam> = emptyList(),
    val sortOption: SortOption = SortOption.WORLD_RANK,
    val errorMessage: String? = null,
    val comparisonTeams: List<WorldCupTeam> = emptyList(),
)

class WorldCupViewModel(
    private val repository: WorldCupTeamsRepository = WorldCupTeamsRepository(),
) : ViewModel() {

    private val _uiState = MutableStateFlow(WorldCupUiState())
    val uiState: StateFlow<WorldCupUiState> = _uiState.asStateFlow()


    init {
        loadTeams()
    }

    fun loadTeams() {
        _uiState.update { it.copy(isLoading = true, errorMessage = null) }
        viewModelScope.launch {
            repository.getWorldCupTeams()
                .onSuccess { teams ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            teams = it.sortOption.sort(teams),
                            errorMessage = null,
                        )
                    }
                }
                .onFailure { throwable ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = throwable.message ?: "Something went wrong",
                        )
                    }
                }
        }
    }

    fun onSortOptionSelected(option: SortOption) {
        _uiState.update {
            it.copy(sortOption = option, teams = option.sort(it.teams))
        }
    }

    fun onTeamLongPressed(team: WorldCupTeam) {
        _uiState.update { state ->
            val current = state.comparisonTeams
            val updated = when {
                current.any { it.teamId == team.teamId } ->
                    // remove if already selected
                    current.filterNot { it.teamId == team.teamId }

                current.size >= 2 ->
                    // replace the oldest with the new one
                    listOf(current[1], team)

                else -> current + team // add
            }
            state.copy(comparisonTeams = updated)
        }
    }

    fun onComparisonDismissed() {
        _uiState.update { it.copy(comparisonTeams = emptyList()) }
    }
}


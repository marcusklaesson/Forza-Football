package com.example.forzafootball.ui.worldcup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.forzafootball.data.remote.model.WorldCupTeam
import com.example.forzafootball.data.repository.WorldCupTeamsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class WorldCupUiState(
    val isLoading: Boolean = false,
    val teams: List<WorldCupTeam> = emptyList(),
    val errorMessage: String? = null,
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
                        it.copy(isLoading = false, teams = teams, errorMessage = null)
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
}


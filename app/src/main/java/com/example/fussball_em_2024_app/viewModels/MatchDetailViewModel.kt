package com.example.fussball_em_2024_app.viewModels

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.State
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.fussball_em_2024_app.matchService
import com.example.fussball_em_2024_app.model.Match
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MatchDetailViewModelFactory(private val matchId: Int) : ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MatchDetailViewModel::class.java)) {
            return MatchDetailViewModel(matchId) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

class MatchDetailViewModel(private val matchId: Int) : ViewModel() {
    private val _matchState= mutableStateOf(SingleMatchState())
    val matchState: State<SingleMatchState> = _matchState

    init{
        fetchMatchesPeriodically()
    }

    private fun fetchMatchesPeriodically() {
        viewModelScope.launch {
            while (true) {
                fetchMatch(matchId)
                delay(60_000)  // Fetch data every 60 seconds
            }
        }
    }

    private suspend fun fetchMatch(matchId: Int) {
        try {
            val response = matchService.getMatch(matchId)
            if (response != null) {
                _matchState.value = _matchState.value.copy(
                    match = response,
                    loading = false,
                    error = null
                )
            }
        } catch (e: Exception) {
            _matchState.value = _matchState.value.copy(
                loading = false,
                error = "Error fetching match: ${e.message}"
            )
        }
    }

    data class SingleMatchState(
        val loading:Boolean= true,
        val match: Match? = null,
        val error:String?=null)
}
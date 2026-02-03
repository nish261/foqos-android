package com.foqos.android.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.foqos.android.data.model.BlockingSession
import com.foqos.android.data.model.FoqosStats
import com.foqos.android.data.repository.FoqosRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class StatsViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = FoqosRepository(application)
    
    val stats: StateFlow<FoqosStats> = repository.getStatsFlow()
        .map { it ?: FoqosStats() }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = FoqosStats()
        )
    
    val recentSessions: StateFlow<List<BlockingSession>> = repository.getAllSessions()
        .map { sessions ->
            sessions
                .filter { it.endTime != null }
                .sortedByDescending { it.startTime }
                .take(20)
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )
}

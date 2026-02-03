package com.foqos.android.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.foqos.android.data.model.BlockingProfile
import com.foqos.android.data.model.BlockingSession
import com.foqos.android.data.repository.FoqosRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class SessionViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = FoqosRepository(application)
    
    val activeSession: StateFlow<BlockingSession?> = repository.getActiveSessionFlow()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = null
        )
    
    val allSessions: StateFlow<List<BlockingSession>> = repository.getAllSessions()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )
    
    fun startSession(profile: BlockingProfile) {
        viewModelScope.launch {
            repository.startSession(profile)
        }
    }
    
    fun endSession(sessionId: String, completedSuccessfully: Boolean = true) {
        viewModelScope.launch {
            repository.endSession(sessionId, completedSuccessfully)
        }
    }
    
    fun pauseSession(sessionId: String) {
        viewModelScope.launch {
            repository.pauseSession(sessionId)
        }
    }
    
    fun resumeSession(sessionId: String) {
        viewModelScope.launch {
            repository.resumeSession(sessionId)
        }
    }
    
    fun getSessionDuration(session: BlockingSession): Long {
        val endTime = session.endTime ?: System.currentTimeMillis()
        return endTime - session.startTime - session.totalPauseTimeMs
    }
    
    fun getRemainingTime(session: BlockingSession): Long? {
        return session.timerEndTime?.let { endTime ->
            maxOf(0, endTime - System.currentTimeMillis())
        }
    }
}

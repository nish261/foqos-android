package com.foqos.android.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.foqos.android.data.model.BlockingProfile
import com.foqos.android.data.repository.FoqosRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ProfileViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = FoqosRepository(application)
    
    val profiles: StateFlow<List<BlockingProfile>> = repository.getAllProfiles()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )
    
    val activeProfile: StateFlow<BlockingProfile?> = repository.getActiveProfileFlow()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = null
        )
    
    fun createProfile(profile: BlockingProfile) {
        viewModelScope.launch {
            repository.insertProfile(profile)
        }
    }
    
    fun updateProfile(profile: BlockingProfile) {
        viewModelScope.launch {
            repository.updateProfile(profile)
        }
    }
    
    fun deleteProfile(profile: BlockingProfile) {
        viewModelScope.launch {
            repository.deleteProfile(profile)
        }
    }
    
    fun activateProfile(profileId: String) {
        viewModelScope.launch {
            repository.activateProfile(profileId)
        }
    }
}

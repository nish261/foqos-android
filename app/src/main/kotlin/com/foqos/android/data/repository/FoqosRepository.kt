package com.foqos.android.data.repository

import android.content.Context
import com.foqos.android.data.database.FoqosDatabase
import com.foqos.android.data.model.BlockingProfile
import com.foqos.android.data.model.BlockingSession
import com.foqos.android.data.model.FoqosStats
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull

class FoqosRepository(context: Context) {
    private val database = FoqosDatabase.getDatabase(context)
    private val profileDao = database.profileDao()
    private val sessionDao = database.sessionDao()
    private val statsDao = database.statsDao()
    
    // Profiles
    fun getAllProfiles(): Flow<List<BlockingProfile>> = profileDao.getAllProfiles()
    
    suspend fun getProfileById(id: String): BlockingProfile? = profileDao.getProfileById(id)
    
    suspend fun getActiveProfile(): BlockingProfile? = profileDao.getActiveProfile()
    
    fun getActiveProfileFlow(): Flow<BlockingProfile?> = profileDao.getActiveProfileFlow()
    
    suspend fun insertProfile(profile: BlockingProfile) = profileDao.insertProfile(profile)
    
    suspend fun updateProfile(profile: BlockingProfile) = profileDao.updateProfile(profile)
    
    suspend fun deleteProfile(profile: BlockingProfile) = profileDao.deleteProfile(profile)
    
    suspend fun activateProfile(profileId: String) {
        profileDao.deactivateAllProfiles()
        profileDao.setProfileActive(profileId)
    }
    
    suspend fun deactivateAllProfiles() = profileDao.deactivateAllProfiles()
    
    // Sessions
    fun getAllSessions(): Flow<List<BlockingSession>> = sessionDao.getAllSessions()
    
    suspend fun getActiveSession(): BlockingSession? = sessionDao.getActiveSession()
    
    fun getActiveSessionFlow(): Flow<BlockingSession?> = sessionDao.getActiveSessionFlow()
    
    suspend fun getSessionById(id: String): BlockingSession? = sessionDao.getSessionById(id)
    
    suspend fun insertSession(session: BlockingSession) = sessionDao.insertSession(session)
    
    suspend fun updateSession(session: BlockingSession) = sessionDao.updateSession(session)
    
    suspend fun deleteSession(session: BlockingSession) = sessionDao.deleteSession(session)
    
    suspend fun startSession(profile: BlockingProfile): BlockingSession {
        // Deactivate any existing active session first
        getActiveSession()?.let { activeSession ->
            endSession(activeSession.id, completedSuccessfully = false)
        }
        
        // Create and insert new session
        val session = BlockingSession(
            profileId = profile.id,
            profileName = profile.name,
            startTime = System.currentTimeMillis(),
            strategy = profile.strategy,
            timerDurationMinutes = profile.timerDurationMinutes,
            timerEndTime = profile.timerDurationMinutes?.let {
                System.currentTimeMillis() + (it * 60 * 1000)
            }
        )
        
        insertSession(session)
        activateProfile(profile.id)
        
        return session
    }
    
    suspend fun endSession(sessionId: String, completedSuccessfully: Boolean = true) {
        getSessionById(sessionId)?.let { session ->
            val updatedSession = session.copy(
                endTime = System.currentTimeMillis(),
                completedSuccessfully = completedSuccessfully
            )
            updateSession(updatedSession)
            deactivateAllProfiles()
            
            // Update stats
            updateStatsAfterSession(updatedSession)
        }
    }
    
    suspend fun pauseSession(sessionId: String) {
        getSessionById(sessionId)?.let { session ->
            updateSession(session.copy(pauseTime = System.currentTimeMillis()))
        }
    }
    
    suspend fun resumeSession(sessionId: String) {
        getSessionById(sessionId)?.let { session ->
            val pauseDuration = session.pauseTime?.let {
                System.currentTimeMillis() - it
            } ?: 0
            
            updateSession(session.copy(
                resumeTime = System.currentTimeMillis(),
                totalPauseTimeMs = session.totalPauseTimeMs + pauseDuration,
                pauseTime = null
            ))
        }
    }
    
    suspend fun incrementBlockedLaunch(sessionId: String) {
        getSessionById(sessionId)?.let { session ->
            updateSession(session.copy(blockedLaunches = session.blockedLaunches + 1))
            statsDao.incrementBlockedLaunches()
        }
    }
    
    // Stats
    suspend fun getStats(): FoqosStats {
        return statsDao.getStats() ?: FoqosStats().also {
            statsDao.insertStats(it)
        }
    }
    
    fun getStatsFlow(): Flow<FoqosStats?> = statsDao.getStatsFlow()
    
    private suspend fun updateStatsAfterSession(session: BlockingSession) {
        val stats = getStats()
        val focusTime = session.endTime?.let { end ->
            end - session.startTime - session.totalPauseTimeMs
        } ?: 0
        
        val updatedStats = stats.copy(
            totalSessions = stats.totalSessions + 1,
            totalFocusTimeMs = stats.totalFocusTimeMs + focusTime,
            totalBlockedLaunches = stats.totalBlockedLaunches + session.blockedLaunches,
            lastSessionDate = System.currentTimeMillis()
        )
        
        // Update streak
        val daysSinceLastSession = (System.currentTimeMillis() - stats.lastSessionDate) / (24 * 60 * 60 * 1000)
        val newStreak = when {
            daysSinceLastSession <= 1 -> stats.currentStreak + 1
            else -> 1
        }
        
        val finalStats = updatedStats.copy(
            currentStreak = newStreak,
            longestStreak = maxOf(newStreak, stats.longestStreak)
        )
        
        statsDao.updateStats(finalStats)
    }
}

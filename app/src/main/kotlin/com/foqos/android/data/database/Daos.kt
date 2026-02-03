package com.foqos.android.data.database

import androidx.room.*
import com.foqos.android.data.model.BlockingProfile
import com.foqos.android.data.model.BlockingSession
import com.foqos.android.data.model.FoqosStats
import kotlinx.coroutines.flow.Flow

@Dao
interface ProfileDao {
    @Query("SELECT * FROM profiles ORDER BY createdAt DESC")
    fun getAllProfiles(): Flow<List<BlockingProfile>>
    
    @Query("SELECT * FROM profiles WHERE id = :id")
    suspend fun getProfileById(id: String): BlockingProfile?
    
    @Query("SELECT * FROM profiles WHERE isActive = 1 LIMIT 1")
    suspend fun getActiveProfile(): BlockingProfile?
    
    @Query("SELECT * FROM profiles WHERE isActive = 1 LIMIT 1")
    fun getActiveProfileFlow(): Flow<BlockingProfile?>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProfile(profile: BlockingProfile)
    
    @Update
    suspend fun updateProfile(profile: BlockingProfile)
    
    @Delete
    suspend fun deleteProfile(profile: BlockingProfile)
    
    @Query("UPDATE profiles SET isActive = 0")
    suspend fun deactivateAllProfiles()
    
    @Query("UPDATE profiles SET isActive = 1 WHERE id = :id")
    suspend fun setProfileActive(id: String)
}

@Dao
interface SessionDao {
    @Query("SELECT * FROM sessions ORDER BY startTime DESC")
    fun getAllSessions(): Flow<List<BlockingSession>>
    
    @Query("SELECT * FROM sessions WHERE endTime IS NULL LIMIT 1")
    suspend fun getActiveSession(): BlockingSession?
    
    @Query("SELECT * FROM sessions WHERE endTime IS NULL LIMIT 1")
    fun getActiveSessionFlow(): Flow<BlockingSession?>
    
    @Query("SELECT * FROM sessions WHERE id = :id")
    suspend fun getSessionById(id: String): BlockingSession?
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSession(session: BlockingSession)
    
    @Update
    suspend fun updateSession(session: BlockingSession)
    
    @Delete
    suspend fun deleteSession(session: BlockingSession)
    
    @Query("SELECT COUNT(*) FROM sessions WHERE endTime IS NOT NULL")
    suspend fun getCompletedSessionsCount(): Int
    
    @Query("SELECT SUM(endTime - startTime - totalPauseTimeMs) FROM sessions WHERE endTime IS NOT NULL")
    suspend fun getTotalFocusTimeMs(): Long?
    
    @Query("DELETE FROM sessions WHERE endTime IS NOT NULL AND startTime < :olderThan")
    suspend fun deleteOldSessions(olderThan: Long)
}

@Dao
interface StatsDao {
    @Query("SELECT * FROM stats WHERE id = 1")
    suspend fun getStats(): FoqosStats?
    
    @Query("SELECT * FROM stats WHERE id = 1")
    fun getStatsFlow(): Flow<FoqosStats?>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStats(stats: FoqosStats)
    
    @Update
    suspend fun updateStats(stats: FoqosStats)
    
    @Query("UPDATE stats SET totalBlockedLaunches = totalBlockedLaunches + 1 WHERE id = 1")
    suspend fun incrementBlockedLaunches()
    
    @Query("UPDATE stats SET currentStreak = :streak WHERE id = 1")
    suspend fun updateCurrentStreak(streak: Int)
    
    @Query("UPDATE stats SET longestStreak = :streak WHERE id = 1")
    suspend fun updateLongestStreak(streak: Int)
}

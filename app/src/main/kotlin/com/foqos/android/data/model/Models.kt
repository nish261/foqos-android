package com.foqos.android.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.UUID

@Entity(tableName = "profiles")
data class BlockingProfile(
    @PrimaryKey
    val id: String = UUID.randomUUID().toString(),
    val name: String,
    val blockedApps: List<String>, // Package names
    val blockedWebsites: List<String>,
    val strategy: BlockingStrategy,
    val nfcTagId: String? = null,
    val qrCodeId: String? = null,
    val timerDurationMinutes: Int? = null,
    val requirePhysicalUnlock: Boolean = false,
    val physicalUnlockNFCTagId: String? = null,
    val physicalUnlockQRCodeId: String? = null,
    val createdAt: Long = System.currentTimeMillis(),
    val isActive: Boolean = false
)

enum class BlockingStrategy {
    MANUAL,          // Start/stop manually
    NFC,            // Start/stop with NFC tag
    QR,             // Start/stop with QR code
    NFC_MANUAL,     // Start manually, stop with NFC
    QR_MANUAL,      // Start manually, stop with QR
    NFC_TIMER,      // Start with timer, stop early with NFC
    QR_TIMER        // Start with timer, stop early with QR
}

@Entity(tableName = "sessions")
data class BlockingSession(
    @PrimaryKey
    val id: String = UUID.randomUUID().toString(),
    val profileId: String,
    val profileName: String,
    val startTime: Long,
    val endTime: Long? = null,
    val strategy: BlockingStrategy,
    val timerDurationMinutes: Int? = null,
    val timerEndTime: Long? = null,
    val pauseTime: Long? = null,
    val resumeTime: Long? = null,
    val totalPauseTimeMs: Long = 0,
    val blockedLaunches: Int = 0,
    val completedSuccessfully: Boolean = false
)

@Entity(tableName = "stats")
data class FoqosStats(
    @PrimaryKey
    val id: Int = 1, // Single row
    val totalSessions: Int = 0,
    val totalFocusTimeMs: Long = 0,
    val totalBlockedLaunches: Int = 0,
    val currentStreak: Int = 0,
    val longestStreak: Int = 0,
    val lastSessionDate: Long = 0
)

// Type converters for Room
class Converters {
    private val gson = Gson()
    
    @TypeConverter
    fun fromStringList(value: List<String>): String {
        return gson.toJson(value)
    }
    
    @TypeConverter
    fun toStringList(value: String): List<String> {
        val listType = object : TypeToken<List<String>>() {}.type
        return gson.fromJson(value, listType)
    }
    
    @TypeConverter
    fun fromBlockingStrategy(value: BlockingStrategy): String {
        return value.name
    }
    
    @TypeConverter
    fun toBlockingStrategy(value: String): BlockingStrategy {
        return BlockingStrategy.valueOf(value)
    }
}

// App info for app picker
data class AppInfo(
    val packageName: String,
    val appName: String,
    val icon: android.graphics.drawable.Drawable? = null,
    val isSelected: Boolean = false
)

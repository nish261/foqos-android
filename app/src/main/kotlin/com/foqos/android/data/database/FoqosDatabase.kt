package com.foqos.android.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.foqos.android.data.model.BlockingProfile
import com.foqos.android.data.model.BlockingSession
import com.foqos.android.data.model.Converters
import com.foqos.android.data.model.FoqosStats

@Database(
    entities = [BlockingProfile::class, BlockingSession::class, FoqosStats::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class FoqosDatabase : RoomDatabase() {
    
    abstract fun profileDao(): ProfileDao
    abstract fun sessionDao(): SessionDao
    abstract fun statsDao(): StatsDao
    
    companion object {
        @Volatile
        private var INSTANCE: FoqosDatabase? = null
        
        fun getDatabase(context: Context): FoqosDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    FoqosDatabase::class.java,
                    "foqos_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}

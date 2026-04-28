package com.example.project.feature.decision.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [
        DecisionEntity::class,
        OptionEntity::class,
        FactorEntity::class,
        HistoryEntity::class
    ],
    version = 2,
    exportSchema = false
)
abstract class DecidrDatabase : RoomDatabase() {
    abstract val decidrDao: DecidrDao
}

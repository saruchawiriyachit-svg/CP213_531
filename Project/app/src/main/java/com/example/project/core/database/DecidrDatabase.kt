package com.example.project.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.project.feature.decision.data.local.DecisionDao
import com.example.project.feature.decision.data.local.DecisionEntity

@Database(entities = [DecisionEntity::class], version = 1, exportSchema = false)
abstract class DecidrDatabase : RoomDatabase() {
    abstract fun decisionDao(): DecisionDao
}

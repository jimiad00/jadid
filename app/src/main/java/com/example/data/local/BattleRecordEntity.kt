package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "battle_records")
data class BattleRecordEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val winnerName: String,
    val loserName: String,
    val confidence: Int,
    val verdictSummary: String,
    val strategicEdge: String,
    val combatPhases: String,
    val engineType: String,
    val timestamp: Long = System.currentTimeMillis()
)

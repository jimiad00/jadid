package com.example.data.model

data class BattleVerdict(
    val winnerName: String,
    val loserName: String,
    val confidence: Int,
    val verdictSummary: String,
    val strategicEdge: String,
    val combatPhases: String,
    val engineType: String,
    val timestamp: Long = System.currentTimeMillis()
)

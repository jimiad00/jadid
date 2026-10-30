package com.example.data.model

import androidx.compose.ui.graphics.Color

data class Character(
    val id: Int,
    val name: String,
    val category: String,
    val bounty: String,
    val fruit: String,
    val haki: String,
    val strength: Int,
    val speed: Int,
    val durability: Int,
    val intelligence: Int,
    val experience: Int,
    val gradientStart: Color,
    val gradientEnd: Color,
    val iconName: String
) {
    val overallRating: Int
        get() = ((strength * 1.2 + speed * 1.15 + durability * 1.1 + intelligence * 0.9 + experience * 0.95) / 5.3).toInt().coerceIn(70, 100)
}

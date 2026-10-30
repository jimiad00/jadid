package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.Brightness7
import androidx.compose.material.icons.filled.Casino
import androidx.compose.material.icons.filled.Coronavirus
import androidx.compose.material.icons.filled.Dangerous
import androidx.compose.material.icons.filled.Diamond
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FlashOn
import androidx.compose.material.icons.filled.Hearing
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.Pets
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.RemoveRedEye
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.SportsMartialArts
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Waves
import androidx.compose.material.icons.filled.Whatshot
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.Character
import com.example.ui.theme.ArenaCardBg
import com.example.ui.theme.ArenaCardBorder
import com.example.ui.theme.ArenaSurface
import com.example.ui.theme.CinzelFontFamily
import com.example.ui.theme.DeepViolet
import com.example.ui.theme.HakiGold
import com.example.ui.theme.NeonPurple
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

@Composable
fun CharacterAvatar(
    character: Character,
    size: Dp = 56.dp,
    iconSize: Dp = 26.dp,
    modifier: Modifier = Modifier
) {
    val icon = getCharacterIcon(character.iconName)

    Box(
        modifier = modifier
            .size(size)
            .clip(RoundedCornerShape(16.dp))
            .background(
                Brush.linearGradient(
                    listOf(character.gradientStart, character.gradientEnd)
                )
            )
            .border(1.5.dp, Color.White.copy(alpha = 0.25f), RoundedCornerShape(16.dp)),
        contentAlignment = Alignment.Center
    ) {
        // Background watermarked text
        Text(
            text = "OP",
            fontFamily = CinzelFontFamily,
            fontSize = (size.value * 0.45f).sp,
            fontWeight = FontWeight.Black,
            color = Color.White.copy(alpha = 0.12f),
            modifier = Modifier.align(Alignment.BottomEnd).padding(end = 4.dp, bottom = 2.dp)
        )

        Icon(
            imageVector = icon,
            contentDescription = character.name,
            tint = Color.White,
            modifier = Modifier.size(iconSize)
        )
    }
}

fun getCharacterIcon(iconName: String): ImageVector {
    return when (iconName) {
        "sun" -> Icons.Default.Brightness7
        "swords" -> Icons.Default.SportsMartialArts
        "flame" -> Icons.Default.Whatshot
        "bolt", "flash" -> Icons.Default.Bolt
        "target" -> Icons.Default.Casino
        "dragon" -> Icons.Default.AutoAwesome
        "cloud" -> Icons.Default.Waves
        "vortex" -> Icons.Default.Coronavirus
        "wave" -> Icons.Default.Waves
        "crown" -> Icons.Default.Diamond
        "fist" -> Icons.Default.SportsMartialArts
        "shield" -> Icons.Default.Security
        "heart" -> Icons.Default.Favorite
        "magnet" -> Icons.Default.Hearing
        "fire" -> Icons.Default.LocalFireDepartment
        "volcano" -> Icons.Default.LocalFireDepartment
        "snowflake" -> Icons.Default.Star
        "eye" -> Icons.Default.RemoveRedEye
        "radar" -> Icons.Default.Public
        "wing" -> Icons.Default.Dangerous
        "paw" -> Icons.Default.Pets
        "water" -> Icons.Default.Waves
        "thunder" -> Icons.Default.FlashOn
        else -> Icons.Default.SportsMartialArts
    }
}

@Composable
fun ArenaGlassCard(
    modifier: Modifier = Modifier,
    borderColor: Color = ArenaCardBorder,
    onClick: (() -> Unit)? = null,
    content: @Composable () -> Unit
) {
    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = ArenaCardBg
        ),
        modifier = modifier
            .border(1.dp, borderColor, RoundedCornerShape(20.dp))
            .then(
                if (onClick != null) Modifier.clickable { onClick() } else Modifier
            )
    ) {
        Box(modifier = Modifier.padding(16.dp)) {
            content()
        }
    }
}

@Composable
fun StatProgressBar(
    label: String,
    value: Int,
    modifier: Modifier = Modifier,
    fillBrush: Brush = Brush.horizontalGradient(listOf(DeepViolet, NeonPurple))
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.bodySmall,
                color = TextSecondary,
                modifier = Modifier.weight(1f)
            )
            Text(
                text = "$value/100",
                style = MaterialTheme.typography.bodySmall,
                fontWeight = FontWeight.Bold,
                color = NeonPurple
            )
        }
        Spacer(modifier = Modifier.height(4.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(6.dp)
                .clip(RoundedCornerShape(3.dp))
                .background(ArenaSurface)
                .border(0.5.dp, Color.White.copy(alpha = 0.08f), RoundedCornerShape(3.dp))
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(fraction = (value / 100f).coerceIn(0f, 1f))
                    .height(6.dp)
                    .clip(RoundedCornerShape(3.dp))
                    .background(fillBrush)
            )
        }
    }
}

@Composable
fun CategoryChip(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(if (isSelected) DeepViolet.copy(alpha = 0.45f) else ArenaSurface)
            .border(
                1.dp,
                if (isSelected) NeonPurple else ArenaCardBorder.copy(alpha = 0.4f),
                RoundedCornerShape(12.dp)
            )
            .clickable { onClick() }
            .padding(horizontal = 14.dp, vertical = 7.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
            color = if (isSelected) NeonPurple else TextMuted
        )
    }
}

@Composable
fun HakiBadge(
    text: String,
    modifier: Modifier = Modifier,
    isGold: Boolean = false
) {
    val bg = if (isGold) HakiGold.copy(alpha = 0.15f) else DeepViolet.copy(alpha = 0.2f)
    val border = if (isGold) HakiGold.copy(alpha = 0.5f) else NeonPurple.copy(alpha = 0.4f)
    val textColor = if (isGold) HakiGold else NeonPurple

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background(bg)
            .border(0.8.dp, border, RoundedCornerShape(8.dp))
            .padding(horizontal = 8.dp, vertical = 3.dp)
    ) {
        Text(
            text = text,
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold,
            color = textColor,
            fontFamily = CinzelFontFamily
        )
    }
}

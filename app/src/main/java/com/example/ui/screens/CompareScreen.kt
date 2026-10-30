package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.Character
import com.example.data.repository.CharacterRepository
import com.example.ui.components.ArenaGlassCard
import com.example.ui.components.RadarChart
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
fun CompareScreen(
    fighter1: Character,
    fighter2: Character,
    onSelectFighter1: (Character) -> Unit,
    onSelectFighter2: (Character) -> Unit,
    onDeployToArena: () -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded1 by remember { mutableStateOf(false) }
    var expanded2 by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(top = 12.dp, bottom = 96.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Header
        item {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "STAT MATRIX RADAR",
                    fontFamily = CinzelFontFamily,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Black,
                    color = NeonPurple,
                    letterSpacing = 1.sp
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Multi-attribute comparison across Strength, Speed, Durability, Intelligence, and Experience.",
                    style = MaterialTheme.typography.bodySmall,
                    color = TextSecondary,
                    textAlign = TextAlign.Center
                )
            }
        }

        // Selection Pickers Row
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // Selector 1
                Box(modifier = Modifier.weight(1f)) {
                    SelectorBox(
                        label = "Fighter 1",
                        name = fighter1.name,
                        accentColor = DeepViolet,
                        onClick = { expanded1 = true }
                    )

                    DropdownMenu(
                        expanded = expanded1,
                        onDismissRequest = { expanded1 = false },
                        modifier = Modifier
                            .background(ArenaSurface)
                            .border(1.dp, ArenaCardBorder, RoundedCornerShape(8.dp))
                    ) {
                        CharacterRepository.allCharacters.forEach { char ->
                            DropdownMenuItem(
                                text = { Text(char.name, color = TextPrimary, fontSize = 12.sp) },
                                onClick = {
                                    onSelectFighter1(char)
                                    expanded1 = false
                                }
                            )
                        }
                    }
                }

                // Selector 2
                Box(modifier = Modifier.weight(1f)) {
                    SelectorBox(
                        label = "Fighter 2",
                        name = fighter2.name,
                        accentColor = NeonPurple,
                        onClick = { expanded2 = true }
                    )

                    DropdownMenu(
                        expanded = expanded2,
                        onDismissRequest = { expanded2 = false },
                        modifier = Modifier
                            .background(ArenaSurface)
                            .border(1.dp, ArenaCardBorder, RoundedCornerShape(8.dp))
                    ) {
                        CharacterRepository.allCharacters.forEach { char ->
                            DropdownMenuItem(
                                text = { Text(char.name, color = TextPrimary, fontSize = 12.sp) },
                                onClick = {
                                    onSelectFighter2(char)
                                    expanded2 = false
                                }
                            )
                        }
                    }
                }
            }
        }

        // Radar Canvas Card
        item {
            ArenaGlassCard(
                borderColor = NeonPurple.copy(alpha = 0.4f),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    // Legend
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(bottom = 6.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(10.dp)
                                    .clip(CircleShape)
                                    .background(DeepViolet)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = fighter1.name,
                                style = MaterialTheme.typography.bodySmall,
                                fontWeight = FontWeight.Bold,
                                color = DeepViolet,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(10.dp)
                                    .clip(CircleShape)
                                    .background(NeonPurple)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = fighter2.name,
                                style = MaterialTheme.typography.bodySmall,
                                fontWeight = FontWeight.Bold,
                                color = NeonPurple,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }

                    // Canvas Radar
                    RadarChart(character1 = fighter1, character2 = fighter2)
                }
            }
        }

        // Stat Differential Comparative Bars Card
        item {
            ArenaGlassCard(
                borderColor = ArenaCardBorder,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text(
                        text = "ATTRIBUTE BREAKDOWN",
                        fontFamily = CinzelFontFamily,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )

                    ComparativeStatRow(
                        label = "Raw Strength",
                        val1 = fighter1.strength,
                        val2 = fighter2.speed
                    )
                    ComparativeStatRow(
                        label = "Combat Speed",
                        val1 = fighter1.speed,
                        val2 = fighter2.speed
                    )
                    ComparativeStatRow(
                        label = "Physical Durability",
                        val1 = fighter1.durability,
                        val2 = fighter2.durability
                    )
                    ComparativeStatRow(
                        label = "Battle IQ & Tactics",
                        val1 = fighter1.intelligence,
                        val2 = fighter2.intelligence
                    )
                    ComparativeStatRow(
                        label = "Experience & Mastery",
                        val1 = fighter1.experience,
                        val2 = fighter2.experience
                    )
                }
            }
        }

        // Jump to Arena Action
        item {
            Button(
                onClick = onDeployToArena,
                colors = ButtonDefaults.buttonColors(containerColor = NeonPurple),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .testTag("deploy_to_arena_button")
            ) {
                Icon(
                    imageVector = Icons.Default.Bolt,
                    contentDescription = null,
                    tint = HakiGold,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Deploy Pair to Battle Arena",
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
            }
        }
    }
}

@Composable
fun SelectorBox(
    label: String,
    name: String,
    accentColor: Color,
    onClick: () -> Unit
) {
    Column {
        Text(
            text = label.uppercase(),
            fontSize = 9.sp,
            fontWeight = FontWeight.Bold,
            color = accentColor,
            letterSpacing = 1.sp
        )
        Spacer(modifier = Modifier.height(4.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .background(ArenaSurface)
                .border(1.dp, ArenaCardBorder, RoundedCornerShape(12.dp))
                .clickable { onClick() }
                .padding(horizontal = 12.dp, vertical = 10.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = name,
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
                color = TextPrimary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.weight(1f)
            )
            Icon(
                imageVector = Icons.Default.ArrowDropDown,
                contentDescription = null,
                tint = accentColor,
                modifier = Modifier.size(18.dp)
            )
        }
    }
}

@Composable
fun ComparativeStatRow(
    label: String,
    val1: Int,
    val2: Int
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "$val1",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = DeepViolet
            )
            Text(
                text = label,
                fontSize = 11.sp,
                color = TextSecondary
            )
            Text(
                text = "$val2",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = NeonPurple
            )
        }

        Spacer(modifier = Modifier.height(4.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            // Left bar (Fighter 1) aligned to the right
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(6.dp)
                    .clip(RoundedCornerShape(3.dp))
                    .background(ArenaSurface),
                contentAlignment = Alignment.CenterEnd
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(fraction = (val1 / 100f).coerceIn(0f, 1f))
                        .height(6.dp)
                        .clip(RoundedCornerShape(3.dp))
                        .background(DeepViolet)
                )
            }

            // Right bar (Fighter 2) aligned to the left
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(6.dp)
                    .clip(RoundedCornerShape(3.dp))
                    .background(ArenaSurface),
                contentAlignment = Alignment.CenterStart
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(fraction = (val2 / 100f).coerceIn(0f, 1f))
                        .height(6.dp)
                        .clip(RoundedCornerShape(3.dp))
                        .background(NeonPurple)
                )
            }
        }
    }
}

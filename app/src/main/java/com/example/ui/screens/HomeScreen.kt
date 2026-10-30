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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.Diamond
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.SportsMartialArts
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.BattleRecordEntity
import com.example.data.model.Character
import com.example.data.repository.CharacterRepository
import com.example.ui.ArenaTab
import com.example.ui.components.ArenaGlassCard
import com.example.ui.components.CharacterAvatar
import com.example.ui.components.HakiBadge
import com.example.ui.theme.ArenaCardBg
import com.example.ui.theme.ArenaCardBorder
import com.example.ui.theme.ArenaSurface
import com.example.ui.theme.CinzelFontFamily
import com.example.ui.theme.DeepViolet
import com.example.ui.theme.EmeraldHaki
import com.example.ui.theme.HakiGold
import com.example.ui.theme.NeonPurple
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

@Composable
fun HomeScreen(
    onNavigateTab: (ArenaTab) -> Unit,
    onOpenDossier: (Character) -> Unit,
    historyList: List<BattleRecordEntity>,
    modifier: Modifier = Modifier
) {
    val featuredLegends = listOf(
        CharacterRepository.allCharacters[0], // Luffy
        CharacterRepository.allCharacters[3], // Shanks
        CharacterRepository.allCharacters[5], // Kaido
        CharacterRepository.allCharacters[9]  // Roger
    )

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(top = 12.dp, bottom = 96.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Hero Banner
        item {
            ArenaGlassCard(
                borderColor = NeonPurple.copy(alpha = 0.4f),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("hero_banner_card")
            ) {
                Column {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        HakiBadge("Yonko & Admiral Powerscaling", isGold = false)
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = "Who Rules the Seas?",
                        fontFamily = CinzelFontFamily,
                        fontSize = 26.sp,
                        fontWeight = FontWeight.Black,
                        color = TextPrimary,
                        lineHeight = 32.sp
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Pit legendary emperors, warlords, and admirals against one another. Powered by real Gemini AI with comprehensive Conqueror's Haki scaling, devil fruit awakenings, and tactical matchup logic.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextSecondary,
                        lineHeight = 20.sp
                    )

                    Spacer(modifier = Modifier.height(18.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Button(
                            onClick = { onNavigateTab(ArenaTab.ARENA) },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = NeonPurple
                            ),
                            shape = RoundedCornerShape(14.dp),
                            modifier = Modifier
                                .weight(1f)
                                .height(46.dp)
                                .testTag("enter_arena_button")
                        ) {
                            Icon(
                                imageVector = Icons.Default.Bolt,
                                contentDescription = null,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                "Enter Arena",
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp
                            )
                        }

                        OutlinedButton(
                            onClick = { onNavigateTab(ArenaTab.LEGENDS) },
                            shape = RoundedCornerShape(14.dp),
                            border = ButtonDefaults.outlinedButtonBorder.copy(
                                brush = Brush.linearGradient(listOf(DeepViolet, NeonPurple))
                            ),
                            modifier = Modifier
                                .weight(1f)
                                .height(46.dp)
                                .testTag("browse_legends_button")
                        ) {
                            Icon(
                                imageVector = Icons.Default.Book,
                                contentDescription = null,
                                tint = NeonPurple,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                "30 Legends",
                                color = TextPrimary,
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 13.sp
                            )
                        }
                    }
                }
            }
        }

        // Quick Stats Row
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                QuickStatCard(
                    title = "30+",
                    subtitle = "Fighters",
                    accentColor = NeonPurple,
                    modifier = Modifier.weight(1f)
                )
                QuickStatCard(
                    title = "3 TIERS",
                    subtitle = "Adv Haki",
                    accentColor = HakiGold,
                    modifier = Modifier.weight(1f)
                )
                QuickStatCard(
                    title = "GEMINI",
                    subtitle = "AI Engine",
                    accentColor = EmeraldHaki,
                    modifier = Modifier.weight(1f)
                )
            }
        }

        // Featured Legends Section
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.LocalFireDepartment,
                        contentDescription = null,
                        tint = NeonPurple,
                        modifier = Modifier.size(18.dp)
                    )
                    Text(
                        text = "Featured Yonko & Legends",
                        fontFamily = CinzelFontFamily,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                }

                Text(
                    text = "View All →",
                    style = MaterialTheme.typography.bodySmall,
                    color = NeonPurple,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .clickable { onNavigateTab(ArenaTab.LEGENDS) }
                        .padding(4.dp)
                )
            }
        }

        // Featured Cards Row
        item {
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(featuredLegends) { character ->
                    FeaturedLegendCard(
                        character = character,
                        onClick = { onOpenDossier(character) }
                    )
                }
            }
        }

        // Quick Chronicle Preview
        item {
            ArenaGlassCard(
                borderColor = ArenaCardBorder.copy(alpha = 0.5f),
                onClick = { onNavigateTab(ArenaTab.HISTORY) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.History,
                                contentDescription = null,
                                tint = HakiGold,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Arena Chronicle",
                                fontFamily = CinzelFontFamily,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary
                            )
                        }

                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                            contentDescription = null,
                            tint = TextMuted,
                            modifier = Modifier.size(16.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    if (historyList.isNotEmpty()) {
                        val latest = historyList.first()
                        Text(
                            text = "Latest Clash: ${latest.winnerName} defeated ${latest.loserName}",
                            style = MaterialTheme.typography.bodySmall,
                            fontWeight = FontWeight.Bold,
                            color = NeonPurple
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = latest.verdictSummary,
                            style = MaterialTheme.typography.bodySmall,
                            color = TextMuted,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
                        )
                    } else {
                        Text(
                            text = "No recorded battle chronicles yet. Step into the arena to test legendary powerscaling!",
                            style = MaterialTheme.typography.bodySmall,
                            color = TextSecondary
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun QuickStatCard(
    title: String,
    subtitle: String,
    accentColor: Color,
    modifier: Modifier = Modifier
) {
    ArenaGlassCard(
        modifier = modifier,
        borderColor = accentColor.copy(alpha = 0.35f)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = title,
                fontFamily = CinzelFontFamily,
                fontSize = 17.sp,
                fontWeight = FontWeight.Black,
                color = accentColor
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = subtitle,
                fontSize = 10.sp,
                color = TextSecondary,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Composable
fun FeaturedLegendCard(
    character: Character,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    ArenaGlassCard(
        onClick = onClick,
        modifier = modifier.width(135.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            CharacterAvatar(
                character = character,
                size = 52.dp,
                iconSize = 24.dp
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = character.name,
                style = MaterialTheme.typography.bodySmall,
                fontWeight = FontWeight.Bold,
                color = TextPrimary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = character.category,
                style = MaterialTheme.typography.labelSmall,
                color = NeonPurple,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

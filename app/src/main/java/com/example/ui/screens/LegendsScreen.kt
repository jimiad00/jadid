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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import com.example.data.model.Character
import com.example.data.repository.CharacterRepository
import com.example.ui.components.ArenaGlassCard
import com.example.ui.components.CategoryChip
import com.example.ui.components.CharacterAvatar
import com.example.ui.theme.ArenaCardBorder
import com.example.ui.theme.ArenaSurface
import com.example.ui.theme.CinzelFontFamily
import com.example.ui.theme.DeepViolet
import com.example.ui.theme.NeonPurple
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

@Composable
fun LegendsScreen(
    searchQuery: String,
    onSearchQueryChanged: (String) -> Unit,
    selectedAffiliation: String,
    onAffiliationSelected: (String) -> Unit,
    onOpenDossier: (Character) -> Unit,
    modifier: Modifier = Modifier
) {
    val affiliations = listOf("All", "Straw Hat Pirates", "Marines", "Yonko", "Legend", "Cross Guild")

    val filteredCharacters = CharacterRepository.allCharacters.filter { character ->
        val matchesQuery = searchQuery.isBlank() ||
                character.name.contains(searchQuery, ignoreCase = true) ||
                character.category.contains(searchQuery, ignoreCase = true) ||
                character.fruit.contains(searchQuery, ignoreCase = true)

        val matchesAffiliation = selectedAffiliation == "All" ||
                character.category.equals(selectedAffiliation, ignoreCase = true)

        matchesQuery && matchesAffiliation
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        Spacer(modifier = Modifier.height(12.dp))

        // Screen Header
        Text(
            text = "GRAND LINE ARCHIVES",
            fontFamily = CinzelFontFamily,
            fontSize = 22.sp,
            fontWeight = FontWeight.Black,
            color = NeonPurple,
            letterSpacing = 1.sp
        )
        Text(
            text = "Explore 30 canonical legends with full combat metrics and Haki mastery.",
            style = MaterialTheme.typography.bodySmall,
            color = TextSecondary
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Search Input
        OutlinedTextField(
            value = searchQuery,
            onValueChange = onSearchQueryChanged,
            placeholder = {
                Text("Search pirate, marine, devil fruit...", fontSize = 12.sp, color = TextMuted)
            },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = null,
                    tint = NeonPurple,
                    modifier = Modifier.size(18.dp)
                )
            },
            trailingIcon = {
                if (searchQuery.isNotEmpty()) {
                    IconButton(onClick = { onSearchQueryChanged("") }) {
                        Icon(
                            imageVector = Icons.Default.Clear,
                            contentDescription = "Clear search",
                            tint = TextMuted,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            },
            singleLine = true,
            shape = RoundedCornerShape(14.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = ArenaSurface,
                unfocusedContainerColor = ArenaSurface,
                focusedBorderColor = NeonPurple,
                unfocusedBorderColor = ArenaCardBorder,
                focusedTextColor = TextPrimary,
                unfocusedTextColor = TextPrimary
            ),
            modifier = Modifier
                .fillMaxWidth()
                .testTag("legends_search_input")
        )

        Spacer(modifier = Modifier.height(10.dp))

        // Affiliation Filter Chips
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            items(affiliations) { aff ->
                CategoryChip(
                    text = aff,
                    isSelected = selectedAffiliation == aff,
                    onClick = { onAffiliationSelected(aff) }
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Grid of Legends
        if (filteredCharacters.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 40.dp),
                contentAlignment = Alignment.TopCenter
            ) {
                Text(
                    text = "No legends found matching \"$searchQuery\"",
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextMuted
                )
            }
        } else {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                contentPadding = PaddingValues(bottom = 96.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(filteredCharacters, key = { it.id }) { character ->
                    LegendGridCard(
                        character = character,
                        onClick = { onOpenDossier(character) }
                    )
                }
            }
        }
    }
}

@Composable
fun LegendGridCard(
    character: Character,
    onClick: () -> Unit
) {
    ArenaGlassCard(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .testTag("legend_card_${character.id}")
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            CharacterAvatar(
                character = character,
                size = 54.dp,
                iconSize = 26.dp
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

            Spacer(modifier = Modifier.height(10.dp))

            // Stat overview bar
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = "STR ${character.strength}", fontSize = 9.sp, color = TextSecondary)
                Text(text = "SPD ${character.speed}", fontSize = 9.sp, color = NeonPurple, fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(3.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(4.dp)
                    .clip(RoundedCornerShape(2.dp))
                    .background(ArenaSurface)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(fraction = ((character.strength + character.speed) / 200f).coerceIn(0f, 1f))
                        .height(4.dp)
                        .clip(RoundedCornerShape(2.dp))
                        .background(
                            Brush.horizontalGradient(listOf(DeepViolet, NeonPurple))
                        )
                )
            }
        }
    }
}

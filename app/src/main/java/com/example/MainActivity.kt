package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.PieChart
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.SportsMartialArts
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.ArenaTab
import com.example.ui.ArenaViewModel
import com.example.ui.dialogs.CharacterDossierDialog
import com.example.ui.dialogs.CharacterPickerBottomSheet
import com.example.ui.dialogs.SettingsDialog
import com.example.ui.screens.ArenaScreen
import com.example.ui.screens.CompareScreen
import com.example.ui.screens.HistoryScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.LegendsScreen
import com.example.ui.theme.ArenaBackground
import com.example.ui.theme.ArenaCardBorder
import com.example.ui.theme.ArenaSurface
import com.example.ui.theme.CinzelFontFamily
import com.example.ui.theme.DeepViolet
import com.example.ui.theme.EmeraldHaki
import com.example.ui.theme.HakiGold
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.theme.NeonPurple
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

class MainActivity : ComponentActivity() {
    private val viewModel: ArenaViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                GrandLineArenaApp(viewModel = viewModel)
            }
        }
    }
}

@Composable
fun GrandLineArenaApp(viewModel: ArenaViewModel) {
    val activeTab by viewModel.activeTab.collectAsState()
    val fighter1 by viewModel.fighter1.collectAsState()
    val fighter2 by viewModel.fighter2.collectAsState()
    val isSimulating by viewModel.isSimulating.collectAsState()
    val battleVerdict by viewModel.battleVerdict.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()
    val selectedAffiliation by viewModel.selectedAffiliation.collectAsState()
    val dossierCharacter by viewModel.dossierCharacter.collectAsState()
    val compareFighter1 by viewModel.compareFighter1.collectAsState()
    val compareFighter2 by viewModel.compareFighter2.collectAsState()
    val historyRecords by viewModel.historyRecords.collectAsState()
    val showSettingsDialog by viewModel.showSettingsDialog.collectAsState()
    val pickingFighterSlot by viewModel.pickingFighterSlot.collectAsState()
    val customApiKey by viewModel.customApiKey.collectAsState()

    // Handle back button when not on Home screen
    BackHandler(enabled = activeTab != ArenaTab.HOME) {
        viewModel.setTab(ArenaTab.HOME)
    }

    Scaffold(
        containerColor = ArenaBackground,
        topBar = {
            ArenaTopBar(
                onLogoClick = { viewModel.setTab(ArenaTab.HOME) },
                onSettingsClick = { viewModel.openSettings() }
            )
        },
        bottomBar = {
            ArenaBottomBar(
                currentTab = activeTab,
                onTabSelected = { viewModel.setTab(it) }
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (activeTab) {
                ArenaTab.HOME -> {
                    HomeScreen(
                        onNavigateTab = { viewModel.setTab(it) },
                        onOpenDossier = { viewModel.openDossier(it) },
                        historyList = historyRecords
                    )
                }
                ArenaTab.ARENA -> {
                    ArenaScreen(
                        fighter1 = fighter1,
                        fighter2 = fighter2,
                        isSimulating = isSimulating,
                        verdict = battleVerdict,
                        onPickFighter = { viewModel.openFighterPicker(it) },
                        onRandomize = { viewModel.randomizeFighters() },
                        onSimulate = { viewModel.simulateBattle() }
                    )
                }
                ArenaTab.LEGENDS -> {
                    LegendsScreen(
                        searchQuery = searchQuery,
                        onSearchQueryChanged = { viewModel.setSearchQuery(it) },
                        selectedAffiliation = selectedAffiliation,
                        onAffiliationSelected = { viewModel.setAffiliation(it) },
                        onOpenDossier = { viewModel.openDossier(it) }
                    )
                }
                ArenaTab.COMPARE -> {
                    CompareScreen(
                        fighter1 = compareFighter1,
                        fighter2 = compareFighter2,
                        onSelectFighter1 = { viewModel.setCompareFighter1(it) },
                        onSelectFighter2 = { viewModel.setCompareFighter2(it) },
                        onDeployToArena = { viewModel.sendComparisonToArena() }
                    )
                }
                ArenaTab.HISTORY -> {
                    HistoryScreen(
                        records = historyRecords,
                        onClearHistory = { viewModel.clearHistory() }
                    )
                }
            }
        }
    }

    // Modal dialogs & bottom sheets
    pickingFighterSlot?.let { slot ->
        CharacterPickerBottomSheet(
            slot = slot,
            onSelectCharacter = { char ->
                viewModel.selectFighter(slot, char)
            },
            onDismiss = { viewModel.closeFighterPicker() }
        )
    }

    dossierCharacter?.let { char ->
        CharacterDossierDialog(
            character = char,
            onSetAsFighter1 = {
                viewModel.selectFighter(1, char)
                viewModel.closeDossier()
                viewModel.setTab(ArenaTab.ARENA)
            },
            onSetAsFighter2 = {
                viewModel.selectFighter(2, char)
                viewModel.closeDossier()
                viewModel.setTab(ArenaTab.ARENA)
            },
            onDismiss = { viewModel.closeDossier() }
        )
    }

    if (showSettingsDialog) {
        SettingsDialog(
            initialApiKey = customApiKey,
            onSaveApiKey = { key -> viewModel.saveCustomApiKey(key) },
            onClearApiKey = { viewModel.clearCustomApiKey() },
            onDismiss = { viewModel.closeSettings() }
        )
    }
}

@Composable
fun ArenaTopBar(
    onLogoClick: () -> Unit,
    onSettingsClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .statusBarsPadding()
            .background(ArenaSurface.copy(alpha = 0.95f))
            .border(0.5.dp, ArenaCardBorder.copy(alpha = 0.4f))
            .padding(horizontal = 16.dp, vertical = 10.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // App Title & Brand Icon
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .clickable { onLogoClick() }
                .testTag("app_brand_header")
        ) {
            Box(
                modifier = Modifier
                    .size(38.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(
                        Brush.linearGradient(listOf(DeepViolet, NeonPurple))
                    )
                    .border(1.dp, Color.White.copy(alpha = 0.3f), RoundedCornerShape(12.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.SportsMartialArts,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(22.dp)
                )
            }

            Spacer(modifier = Modifier.width(10.dp))

            Column {
                Text(
                    text = "GRAND LINE ARENA",
                    fontFamily = CinzelFontFamily,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Black,
                    color = NeonPurple,
                    letterSpacing = 0.8.sp
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(6.dp)
                            .clip(CircleShape)
                            .background(EmeraldHaki)
                    )
                    Spacer(modifier = Modifier.width(5.dp))
                    Text(
                        text = "SIMULATOR • AI V3.5",
                        fontSize = 9.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = TextSecondary,
                        letterSpacing = 1.sp
                    )
                }
            }
        }

        // Settings Button
        IconButton(
            onClick = onSettingsClick,
            modifier = Modifier
                .size(38.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(ArenaSurface)
                .border(1.dp, ArenaCardBorder, RoundedCornerShape(10.dp))
                .testTag("settings_button")
        ) {
            Icon(
                imageVector = Icons.Default.Tune,
                contentDescription = "Arena Configuration",
                tint = NeonPurple,
                modifier = Modifier.size(18.dp)
            )
        }
    }
}

@Composable
fun ArenaBottomBar(
    currentTab: ArenaTab,
    onTabSelected: (ArenaTab) -> Unit
) {
    NavigationBar(
        containerColor = ArenaSurface.copy(alpha = 0.96f),
        tonalElevation = 8.dp,
        modifier = Modifier
            .border(0.5.dp, ArenaCardBorder.copy(alpha = 0.4f))
            .testTag("bottom_navigation_bar")
    ) {
        val tabs = listOf(
            Triple(ArenaTab.HOME, Icons.Default.Home, "Home"),
            Triple(ArenaTab.ARENA, Icons.Default.Bolt, "Arena"),
            Triple(ArenaTab.LEGENDS, Icons.Default.Book, "Legends"),
            Triple(ArenaTab.COMPARE, Icons.Default.PieChart, "Compare"),
            Triple(ArenaTab.HISTORY, Icons.Default.History, "History")
        )

        tabs.forEach { (tab, icon, label) ->
            val isSelected = currentTab == tab
            NavigationBarItem(
                selected = isSelected,
                onClick = { onTabSelected(tab) },
                icon = {
                    Icon(
                        imageVector = icon,
                        contentDescription = label,
                        modifier = Modifier.size(20.dp)
                    )
                },
                label = {
                    Text(
                        text = label,
                        fontSize = 10.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Color.White,
                    selectedTextColor = NeonPurple,
                    unselectedIconColor = TextMuted,
                    unselectedTextColor = TextMuted,
                    indicatorColor = DeepViolet
                ),
                modifier = Modifier.testTag("nav_item_${label.lowercase()}")
            )
        }
    }
}

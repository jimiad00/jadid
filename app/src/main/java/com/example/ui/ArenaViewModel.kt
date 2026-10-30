package com.example.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.engine.BattleSimulatorEngine
import com.example.data.local.AppDatabase
import com.example.data.local.ArenaPreferences
import com.example.data.local.BattleRecordEntity
import com.example.data.model.BattleVerdict
import com.example.data.model.Character
import com.example.data.repository.CharacterRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

enum class ArenaTab {
    HOME,
    ARENA,
    LEGENDS,
    COMPARE,
    HISTORY
}

class ArenaViewModel(application: Application) : AndroidViewModel(application) {

    private val preferences = ArenaPreferences(application)
    private val database = AppDatabase.getDatabase(application)
    private val battleRecordDao = database.battleRecordDao()
    private val simulatorEngine = BattleSimulatorEngine(preferences)

    val historyRecords: StateFlow<List<BattleRecordEntity>> = battleRecordDao
        .getAllRecords()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _activeTab = MutableStateFlow(ArenaTab.HOME)
    val activeTab: StateFlow<ArenaTab> = _activeTab.asStateFlow()

    private val _fighter1 = MutableStateFlow(CharacterRepository.allCharacters[0]) // Luffy
    val fighter1: StateFlow<Character> = _fighter1.asStateFlow()

    private val _fighter2 = MutableStateFlow(CharacterRepository.allCharacters[3]) // Shanks
    val fighter2: StateFlow<Character> = _fighter2.asStateFlow()

    private val _isSimulating = MutableStateFlow(false)
    val isSimulating: StateFlow<Boolean> = _isSimulating.asStateFlow()

    private val _battleVerdict = MutableStateFlow<BattleVerdict?>(null)
    val battleVerdict: StateFlow<BattleVerdict?> = _battleVerdict.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _selectedAffiliation = MutableStateFlow("All")
    val selectedAffiliation: StateFlow<String> = _selectedAffiliation.asStateFlow()

    private val _dossierCharacter = MutableStateFlow<Character?>(null)
    val dossierCharacter: StateFlow<Character?> = _dossierCharacter.asStateFlow()

    private val _compareFighter1 = MutableStateFlow(CharacterRepository.allCharacters[0])
    val compareFighter1: StateFlow<Character> = _compareFighter1.asStateFlow()

    private val _compareFighter2 = MutableStateFlow(CharacterRepository.allCharacters[3])
    val compareFighter2: StateFlow<Character> = _compareFighter2.asStateFlow()

    private val _showSettingsDialog = MutableStateFlow(false)
    val showSettingsDialog: StateFlow<Boolean> = _showSettingsDialog.asStateFlow()

    private val _pickingFighterSlot = MutableStateFlow<Int?>(null)
    val pickingFighterSlot: StateFlow<Int?> = _pickingFighterSlot.asStateFlow()

    private val _customApiKey = MutableStateFlow(preferences.getCustomApiKey())
    val customApiKey: StateFlow<String> = _customApiKey.asStateFlow()

    fun setTab(tab: ArenaTab) {
        _activeTab.value = tab
    }

    fun selectFighter(slot: Int, character: Character) {
        if (slot == 1) {
            _fighter1.value = character
        } else {
            _fighter2.value = character
        }
        _pickingFighterSlot.value = null
    }

    fun randomizeFighters() {
        val list = CharacterRepository.allCharacters
        val idx1 = list.indices.random()
        var idx2 = list.indices.random()
        while (idx2 == idx1) {
            idx2 = list.indices.random()
        }
        _fighter1.value = list[idx1]
        _fighter2.value = list[idx2]
    }

    fun simulateBattle() {
        if (_isSimulating.value) return
        val f1 = _fighter1.value
        val f2 = _fighter2.value

        viewModelScope.launch {
            _isSimulating.value = true
            try {
                val verdict = simulatorEngine.simulateBattle(f1, f2)
                _battleVerdict.value = verdict

                // Persist to Room database
                battleRecordDao.insertRecord(
                    BattleRecordEntity(
                        winnerName = verdict.winnerName,
                        loserName = verdict.loserName,
                        confidence = verdict.confidence,
                        verdictSummary = verdict.verdictSummary,
                        strategicEdge = verdict.strategicEdge,
                        combatPhases = verdict.combatPhases,
                        engineType = verdict.engineType,
                        timestamp = verdict.timestamp
                    )
                )
            } finally {
                _isSimulating.value = false
            }
        }
    }

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun setAffiliation(affiliation: String) {
        _selectedAffiliation.value = affiliation
    }

    fun openDossier(character: Character) {
        _dossierCharacter.value = character
    }

    fun closeDossier() {
        _dossierCharacter.value = null
    }

    fun openFighterPicker(slot: Int) {
        _pickingFighterSlot.value = slot
    }

    fun closeFighterPicker() {
        _pickingFighterSlot.value = null
    }

    fun setCompareFighter1(character: Character) {
        _compareFighter1.value = character
    }

    fun setCompareFighter2(character: Character) {
        _compareFighter2.value = character
    }

    fun sendComparisonToArena() {
        _fighter1.value = _compareFighter1.value
        _fighter2.value = _compareFighter2.value
        _activeTab.value = ArenaTab.ARENA
    }

    fun openSettings() {
        _customApiKey.value = preferences.getCustomApiKey()
        _showSettingsDialog.value = true
    }

    fun closeSettings() {
        _showSettingsDialog.value = false
    }

    fun saveCustomApiKey(key: String) {
        preferences.saveCustomApiKey(key)
        _customApiKey.value = key
        _showSettingsDialog.value = false
    }

    fun clearCustomApiKey() {
        preferences.clearCustomApiKey()
        _customApiKey.value = ""
    }

    fun clearHistory() {
        viewModelScope.launch {
            battleRecordDao.clearAll()
        }
    }
}

package com.example.data.engine

import com.example.BuildConfig
import com.example.data.local.ArenaPreferences
import com.example.data.model.BattleVerdict
import com.example.data.model.Character
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.util.concurrent.TimeUnit
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

class BattleSimulatorEngine(
    private val preferences: ArenaPreferences
) {
    private val httpClient = OkHttpClient.Builder()
        .connectTimeout(15, TimeUnit.SECONDS)
        .readTimeout(25, TimeUnit.SECONDS)
        .build()

    suspend fun simulateBattle(fighter1: Character, fighter2: Character): BattleVerdict = withContext(Dispatchers.IO) {
        val customKey = preferences.getCustomApiKey().trim()
        val buildKey = BuildConfig.GEMINI_API_KEY.trim()
        val apiKey = when {
            customKey.isNotEmpty() -> customKey
            buildKey.isNotEmpty() -> buildKey
            else -> ""
        }

        if (apiKey.isNotEmpty()) {
            try {
                return@withContext runGeminiSimulation(fighter1, fighter2, apiKey)
            } catch (e: Exception) {
                // If network/quota fails, gracefully fall back to deterministic algorithm
                return@withContext runDeterministicSimulation(fighter1, fighter2, isFallback = true)
            }
        } else {
            // Simulated delay for suspense & animations
            delay(750)
            return@withContext runDeterministicSimulation(fighter1, fighter2, isFallback = false)
        }
    }

    private fun runGeminiSimulation(f1: Character, f2: Character, apiKey: String): BattleVerdict {
        val prompt = """
            You are a world-class One Piece lore analyst and powerscaling expert.
            Simulate a full-power canon battle between:
            Fighter 1: ${f1.name} (Affiliation: ${f1.category}, Devil Fruit/Form: ${f1.fruit}, Haki: ${f1.haki}, Stats: STR ${f1.strength}, SPD ${f1.speed}, DUR ${f1.durability}, INT ${f1.intelligence}, EXP ${f1.experience})
            Fighter 2: ${f2.name} (Affiliation: ${f2.category}, Devil Fruit/Form: ${f2.fruit}, Haki: ${f2.haki}, Stats: STR ${f2.strength}, SPD ${f2.speed}, DUR ${f2.durability}, INT ${f2.intelligence}, EXP ${f2.experience})

            Respond strictly with valid raw JSON (no markdown fences, no formatting backticks) in this exact schema:
            {
              "winner": "Name of Winner",
              "loser": "Name of Loser",
              "confidence": 78,
              "verdictSummary": "2-3 sentences explaining who emerges victorious and the decisive factor.",
              "strategicEdge": "1-2 sentences on Devil Fruit interaction, Haki superiority, or environmental/tactical factors.",
              "combatPhases": "2-3 sentences detailing how the opening, mid-game awakening, and final exchange unfold."
            }
        """.trimIndent()

        val jsonBody = JSONObject().apply {
            val partsArray = org.json.JSONArray().apply {
                put(JSONObject().apply { put("text", prompt) })
            }
            val contentsArray = org.json.JSONArray().apply {
                put(JSONObject().apply { put("parts", partsArray) })
            }
            put("contents", contentsArray)

            val genConfig = JSONObject().apply {
                put("response_mime_type", "application/json")
                put("temperature", 0.6)
            }
            put("generationConfig", genConfig)
        }

        val requestBody = jsonBody.toString().toRequestBody("application/json".toMediaType())
        val url = "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.5-flash:generateContent?key=$apiKey"

        val request = Request.Builder()
            .url(url)
            .post(requestBody)
            .build()

        val response = httpClient.newCall(request).execute()
        if (!response.isSuccessful) {
            throw IllegalStateException("Gemini API call failed with code: ${response.code}")
        }

        val responseBody = response.body?.string() ?: throw IllegalStateException("Empty Gemini response")
        val root = JSONObject(responseBody)
        val candidates = root.optJSONArray("candidates")
        val firstCandidate = candidates?.optJSONObject(0)
        val content = firstCandidate?.optJSONObject("content")
        val parts = content?.optJSONArray("parts")
        val text = parts?.optJSONObject(0)?.optString("text") ?: "{}"

        val cleanJson = text.replace("```json", "", ignoreCase = true)
            .replace("```", "")
            .trim()

        val parsed = JSONObject(cleanJson)
        val rawWinner = parsed.optString("winner", f1.name)
        val winnerName = if (rawWinner.contains(f2.name, ignoreCase = true)) f2.name else f1.name
        val loserName = if (winnerName == f1.name) f2.name else f1.name
        val confidence = parsed.optInt("confidence", 76).coerceIn(52, 98)

        val verdictSummary = parsed.optString(
            "verdictSummary",
            "After a cataclysmic clash of wills on the Grand Line, $winnerName overcomes $loserName through superior endurance and Haki infusing strikes."
        )
        val strategicEdge = parsed.optString(
            "strategicEdge",
            "$winnerName utilized decisive tactical maneuvering and Conqueror's Haki penetration to breach $loserName's guard."
        )
        val combatPhases = parsed.optString(
            "combatPhases",
            "The duel opened with continent-splitting shockwaves. At the midpoint, ultimate awakenings reshaped the battlefield until $winnerName landed the decisive finishing blow."
        )

        return BattleVerdict(
            winnerName = winnerName,
            loserName = loserName,
            confidence = confidence,
            verdictSummary = verdictSummary,
            strategicEdge = strategicEdge,
            combatPhases = combatPhases,
            engineType = "Gemini 2.5 Flash Real-Time AI"
        )
    }

    private fun runDeterministicSimulation(f1: Character, f2: Character, isFallback: Boolean): BattleVerdict {
        val hakiBonus1 = calculateHakiTier(f1.haki)
        val hakiBonus2 = calculateHakiTier(f2.haki)

        val score1 = (f1.strength * 1.25) + (f1.speed * 1.15) + (f1.durability * 1.1) +
                (f1.intelligence * 0.9) + (f1.experience * 0.95) + hakiBonus1
        val score2 = (f2.strength * 1.25) + (f2.speed * 1.15) + (f2.durability * 1.1) +
                (f2.intelligence * 0.9) + (f2.experience * 0.95) + hakiBonus2

        val diff = abs(score1 - score2)
        val winner = if (score1 >= score2) f1 else f2
        val loser = if (score1 >= score2) f2 else f1

        val confidence = min(96, max(53, (52 + diff * 1.2).toInt()))

        val strategicEdge = buildStrategicEdge(winner, loser)
        val combatPhases = "The battle opened in high tension with territorial shockwaves tearing through the terrain. Mid-battle, both fighters unleashed their ultimate awakenings, pushing their stamina to the brink. In the decisive climax, ${winner.name} broke through ${loser.name}'s guard to land the final concussive blow."
        val verdictSummary = "After an earth-shattering confrontation on the Grand Line, ${winner.name} claims victory over ${loser.name}. While ${loser.name} inflicted severe damage, ${winner.name}'s total combat pressure and Haki mastery proved insurmountable."

        val engineLabel = if (isFallback) {
            "Offline Powerscale (Gemini Fallback)"
        } else {
            "Deterministic Grand Line Scales"
        }

        return BattleVerdict(
            winnerName = winner.name,
            loserName = loser.name,
            confidence = confidence,
            verdictSummary = verdictSummary,
            strategicEdge = strategicEdge,
            combatPhases = combatPhases,
            engineType = engineLabel
        )
    }

    private fun calculateHakiTier(haki: String): Double {
        return when {
            haki.contains("Divine Conqueror's", ignoreCase = true) ||
                    haki.contains("God-Tier Conqueror's", ignoreCase = true) ||
                    haki.contains("Ancient Domination", ignoreCase = true) -> 12.0
            haki.contains("Supreme Conqueror's", ignoreCase = true) -> 10.0
            haki.contains("Advanced Conqueror's", ignoreCase = true) -> 7.5
            haki.contains("Conqueror's", ignoreCase = true) -> 4.5
            haki.contains("Advanced Armament", ignoreCase = true) -> 3.0
            else -> 1.0
        }
    }

    private fun buildStrategicEdge(winner: Character, loser: Character): String {
        return when {
            winner.haki.contains("Supreme Conqueror's") || winner.haki.contains("God-Tier") || winner.haki.contains("Divine") -> {
                "${winner.name} dominated the clash through supreme Advanced Conqueror's Haki infusion, bypassing ${loser.name}'s conventional defenses with non-contact internal destruction."
            }
            winner.speed >= loser.speed + 4 -> {
                "${winner.name}'s blinding speed and agility allowed them to continually outmaneuver and counter ${loser.name}'s heavier artillery."
            }
            winner.fruit.contains("Awakened", ignoreCase = true) && !loser.fruit.contains("Awakened", ignoreCase = true) -> {
                "${winner.name}'s awakened Devil Fruit transmuted the environment into an overwhelming offensive theater that exhausted ${loser.name}."
            }
            winner.durability >= loser.durability + 4 -> {
                "${winner.name}'s immense physical resilience and unyielding defense absorbed ${loser.name}'s fiercest attacks until an opening emerged."
            }
            else -> {
                "${winner.name}'s superior physical threshold and battle experience tipped the encounter against ${loser.name} in a prolonged war of attrition."
            }
        }
    }
}

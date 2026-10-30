package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.data.engine.BattleSimulatorEngine
import com.example.data.local.ArenaPreferences
import com.example.data.repository.CharacterRepository
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class ExampleRobolectricTest {

  @Test
  fun `read string from context`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val appName = context.getString(R.string.app_name)
    assertEquals("Grand Line Arena", appName)
  }

  @Test
  fun `verify character repository integrity`() {
    val characters = CharacterRepository.allCharacters
    assertEquals(30, characters.size)

    val luffy = CharacterRepository.getCharacterById(1)
    assertEquals("Monkey D. Luffy", luffy.name)
    assertTrue(luffy.fruit.contains("Nika"))
  }

  @Test
  fun `verify offline battle simulator engine`() = runBlocking {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val prefs = ArenaPreferences(context)
    val engine = BattleSimulatorEngine(prefs)

    val luffy = CharacterRepository.getCharacterById(1)
    val shanks = CharacterRepository.getCharacterById(4)

    val verdict = engine.simulateBattle(luffy, shanks)
    assertNotNull(verdict)
    assertTrue(verdict.winnerName.isNotEmpty())
    assertTrue(verdict.confidence >= 50)
  }
}

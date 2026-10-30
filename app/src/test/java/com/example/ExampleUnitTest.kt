package com.example

import com.example.data.repository.CharacterRepository
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class ExampleUnitTest {
  @Test
  fun testCharacterCountsAndStats() {
    val characters = CharacterRepository.allCharacters
    assertEquals(30, characters.size)

    characters.forEach { c ->
      assertTrue(c.strength in 50..100)
      assertTrue(c.speed in 50..100)
      assertTrue(c.durability in 50..100)
      assertTrue(c.overallRating in 50..100)
    }
  }

  @Test
  fun testCategories() {
    val categories = CharacterRepository.allCharacters.map { it.category }.toSet()
    assertTrue(categories.contains("Straw Hat Pirates"))
    assertTrue(categories.contains("Yonko"))
    assertTrue(categories.contains("Marines"))
    assertTrue(categories.contains("Legend"))
    assertTrue(categories.contains("Cross Guild"))
  }
}

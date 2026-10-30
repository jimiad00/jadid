package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface BattleRecordDao {
    @Query("SELECT * FROM battle_records ORDER BY timestamp DESC LIMIT 50")
    fun getAllRecords(): Flow<List<BattleRecordEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecord(record: BattleRecordEntity): Long

    @Query("DELETE FROM battle_records")
    suspend fun clearAll()
}

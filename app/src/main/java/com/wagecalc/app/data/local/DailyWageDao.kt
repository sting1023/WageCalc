package com.wagecalc.app.data.local

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface DailyWageDao {
    @Query("SELECT * FROM daily_wage_records ORDER BY date DESC")
    fun getAllRecords(): Flow<List<DailyWageRecord>>

    @Query("SELECT * FROM daily_wage_records WHERE date LIKE :yearMonth || '%' ORDER BY date ASC")
    fun getRecordsByMonth(yearMonth: String): Flow<List<DailyWageRecord>>

    @Query("SELECT * FROM daily_wage_records WHERE date = :date LIMIT 1")
    suspend fun getRecordByDate(date: String): DailyWageRecord?

    @Query("SELECT date FROM daily_wage_records WHERE date LIKE :yearMonth || '%'")
    fun getDatesWithRecords(yearMonth: String): Flow<List<String>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(record: DailyWageRecord): Long

    @Update
    suspend fun update(record: DailyWageRecord)

    @Delete
    suspend fun delete(record: DailyWageRecord)

    @Query("DELETE FROM daily_wage_records WHERE id = :id")
    suspend fun deleteById(id: Long)
}

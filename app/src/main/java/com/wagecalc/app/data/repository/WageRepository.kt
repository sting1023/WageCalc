package com.wagecalc.app.data.repository

import com.wagecalc.app.data.local.DailyWageDao
import com.wagecalc.app.data.local.DailyWageRecord
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WageRepository @Inject constructor(
    private val dao: DailyWageDao
) {
    fun getAllRecords(): Flow<List<DailyWageRecord>> = dao.getAllRecords()

    fun getRecordsByMonth(yearMonth: String): Flow<List<DailyWageRecord>> =
        dao.getRecordsByMonth(yearMonth)

    suspend fun getRecordByDate(date: String): DailyWageRecord? =
        dao.getRecordByDate(date)

    fun getDatesWithRecords(yearMonth: String): Flow<List<String>> =
        dao.getDatesWithRecords(yearMonth)

    suspend fun saveRecord(record: DailyWageRecord): Long {
        val total = DailyWageRecord.calculate(
            basicWage = record.basicWage,
            multiplier = record.multiplier,
            overtimeHours = record.overtimeHours,
            hourlyWagePreset = record.hourlyWagePreset,
            hourlyWageEnabled = record.hourlyWageEnabled,
            dailyWagePreset = record.dailyWagePreset,
            dailyWageEnabled = record.dailyWageEnabled,
            extraIncome = record.extraIncome
        )
        val toSave = record.copy(totalWage = total, updatedAt = System.currentTimeMillis())
        return dao.insert(toSave)
    }

    suspend fun deleteRecord(id: Long) = dao.deleteById(id)
}

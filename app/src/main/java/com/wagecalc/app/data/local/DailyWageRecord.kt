package com.wagecalc.app.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "daily_wage_records")
data class DailyWageRecord(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val date: String, // yyyy-MM-dd
    val basicWage: Double,
    val multiplier: Double = 1.0,
    val overtimeHours: Double = 0.0,
    val hourlyWagePreset: Double = 0.0,
    val hourlyWageEnabled: Boolean = false,
    val dailyWagePreset: Double = 0.0,
    val dailyWageEnabled: Boolean = false,
    val extraIncome: Double = 0.0,
    val totalWage: Double = 0.0,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
) {
    companion object {
        fun calculate(
            basicWage: Double,
            multiplier: Double,
            overtimeHours: Double,
            hourlyWagePreset: Double,
            hourlyWageEnabled: Boolean,
            dailyWagePreset: Double,
            dailyWageEnabled: Boolean,
            extraIncome: Double
        ): Double {
            val basicPart = basicWage * multiplier
            val hourlyPart = if (hourlyWageEnabled) overtimeHours * hourlyWagePreset * multiplier else 0.0
            val dailyPart = if (dailyWageEnabled) dailyWagePreset else 0.0
            return (basicPart + hourlyPart + dailyPart + extraIncome).let {
                (it * 100).toLong() / 100.0
            }
        }
    }
}

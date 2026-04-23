package com.wagecalc.app.ui.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wagecalc.app.data.local.DailyWageRecord
import com.wagecalc.app.data.repository.WageRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.YearMonth
import javax.inject.Inject

data class SummaryUiState(
    val yearMonth: YearMonth = YearMonth.now(),
    val records: List<DailyWageRecord> = emptyList(),
    val totalWage: Double = 0.0,
    val isLoading: Boolean = false
)

@HiltViewModel
class SummaryViewModel @Inject constructor(
    private val repository: WageRepository
) : ViewModel() {

    private val _yearMonth = MutableStateFlow(YearMonth.now())

    val uiState: StateFlow<SummaryUiState> = _yearMonth
        .flatMapLatest { ym ->
            repository.getRecordsByMonth(ym.toString()).map { records ->
                SummaryUiState(
                    yearMonth = ym,
                    records = records,
                    totalWage = records.sumOf { it.totalWage }
                )
            }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), SummaryUiState())

    fun prevMonth() {
        _yearMonth.update { it.minusMonths(1) }
    }

    fun nextMonth() {
        _yearMonth.update { it.plusMonths(1) }
    }

    fun deleteRecord(id: Long) {
        viewModelScope.launch {
            repository.deleteRecord(id)
        }
    }
}

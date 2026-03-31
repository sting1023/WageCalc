package com.wagecalc.app.ui.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wagecalc.app.data.local.DailyWageRecord
import com.wagecalc.app.data.repository.WageRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.YearMonth
import javax.inject.Inject

data class InputUiState(
    val yearMonth: YearMonth = YearMonth.now(),
    val selectedDate: LocalDate = LocalDate.now(),
    val datesWithRecords: Set<String> = emptySet(),
    val currentRecord: DailyWageRecord? = null,
    val isLoading: Boolean = false
)

@HiltViewModel
class InputViewModel @Inject constructor(
    private val repository: WageRepository
) : ViewModel() {

    private val _yearMonth = MutableStateFlow(YearMonth.now())
    private val _selectedDate = MutableStateFlow(LocalDate.now())

    private val _uiState = MutableStateFlow(InputUiState())

    val uiState: StateFlow<InputUiState> = combine(
        _yearMonth,
        _selectedDate,
        _uiState
    ) { ym, sd, existing ->
        existing.copy(yearMonth = ym, selectedDate = sd)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), InputUiState())

    init {
        // Load dates with records for current month
        viewModelScope.launch {
            _yearMonth.collectLatest { ym ->
                repository.getDatesWithRecords(ym.toString()).collect { dates ->
                    _uiState.update { it.copy(datesWithRecords = dates.toSet()) }
                }
            }
        }
        loadSelectedDateRecord()
    }

    fun selectDate(date: LocalDate) {
        _selectedDate.value = date
        _yearMonth.value = YearMonth.from(date)
        loadSelectedDateRecord()
    }

    fun prevMonth() {
        _yearMonth.update { it.minusMonths(1) }
    }

    fun nextMonth() {
        _yearMonth.update { it.plusMonths(1) }
    }

    private fun loadSelectedDateRecord() {
        viewModelScope.launch {
            val dateStr = _selectedDate.value.toString()
            val record = repository.getRecordByDate(dateStr)
            _uiState.update { it.copy(currentRecord = record) }
        }
    }

    fun saveRecord(record: DailyWageRecord) {
        viewModelScope.launch {
            val toSave = record.copy(date = _selectedDate.value.toString())
            repository.saveRecord(toSave)
            loadSelectedDateRecord()
            // refresh dates
            val ym = _yearMonth.value.toString()
            repository.getDatesWithRecords(ym).first().let { dates ->
                _uiState.update { it.copy(datesWithRecords = dates.toSet()) }
            }
        }
    }
}

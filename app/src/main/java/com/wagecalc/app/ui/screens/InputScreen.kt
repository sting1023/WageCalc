package com.wagecalc.app.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.wagecalc.app.ui.components.Calendar
import com.wagecalc.app.ui.components.WageForm

@Composable
fun InputScreen(
    viewModel: InputViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Column(modifier = Modifier.fillMaxSize()) {
        // 日历
        Calendar(
            yearMonth = uiState.yearMonth,
            selectedDate = uiState.selectedDate,
            datesWithRecords = uiState.datesWithRecords,
            onDateSelected = { viewModel.selectDate(it) },
            onPrevMonth = { viewModel.prevMonth() },
            onNextMonth = { viewModel.nextMonth() },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        )

        HorizontalDivider()

        // 工资录入表单
        WageForm(
            record = uiState.currentRecord,
            onSave = { viewModel.saveRecord(it) },
            modifier = Modifier.fillMaxWidth()
        )
    }
}

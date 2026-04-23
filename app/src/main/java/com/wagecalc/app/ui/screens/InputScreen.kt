package com.wagecalc.app.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.wagecalc.app.data.local.DailyWageRecord
import com.wagecalc.app.ui.components.WageCalendar
import com.wagecalc.app.ui.components.WageForm
import com.wagecalc.app.ui.theme.AccentGreen
import java.text.DecimalFormat

@Composable
fun InputScreen(
    viewModel: InputViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Column(modifier = Modifier.fillMaxSize()) {
        // 缩小后的日历
        WageCalendar(
            yearMonth = uiState.yearMonth,
            selectedDate = uiState.selectedDate,
            datesWithRecords = uiState.datesWithRecords,
            onDateSelected = { viewModel.selectDate(it) },
            onPrevMonth = { viewModel.prevMonth() },
            onNextMonth = { viewModel.nextMonth() },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp)
        )

        HorizontalDivider()

        // 本月记录列表（显示5条）
        if (uiState.monthlyRecords.isNotEmpty()) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Text(
                    text = "本月记录 (${uiState.monthlyRecords.size}条)",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(4.dp))
                uiState.monthlyRecords.take(5).forEach { record ->
                    MonthlyRecordRow(record = record)
                }
            }
            HorizontalDivider()
        }

        // 工资录入表单
        WageForm(
            record = uiState.currentRecord,
            onSave = { viewModel.saveRecord(it) },
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
private fun MonthlyRecordRow(record: DailyWageRecord) {
    val dateLabel = record.date.takeLast(5).replace("-", "/")
    val summary = buildString {
        if (record.basicWage > 0) append("基本:${record.basicWage}")
        if (record.overtimeHours > 0) append(" 加班:${record.overtimeHours}h")
        if (record.extraIncome > 0) append(" 额外:${record.extraIncome}")
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(text = dateLabel, fontWeight = FontWeight.Medium, fontSize = 13.sp)
            if (summary.isNotEmpty()) {
                Text(
                    text = summary,
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
        Text(
            text = "¥${DecimalFormat("#,##0.00").format(record.totalWage)}",
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp,
            color = AccentGreen
        )
    }
}

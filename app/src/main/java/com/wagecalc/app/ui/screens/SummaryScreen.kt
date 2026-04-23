package com.wagecalc.app.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.wagecalc.app.data.local.DailyWageRecord
import com.wagecalc.app.ui.theme.AccentGreen
import java.text.DecimalFormat

@Composable
fun SummaryScreen(
    viewModel: SummaryViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Column(modifier = Modifier.fillMaxSize()) {
        // 月份导航
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { viewModel.prevMonth() }) {
                Icon(Icons.AutoMirrored.Filled.KeyboardArrowLeft, "上一月")
            }
            Text(
                text = "${uiState.yearMonth.year}年 ${uiState.yearMonth.monthValue}月",
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium
            )
            IconButton(onClick = { viewModel.nextMonth() }) {
                Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, "下一月")
            }
        }

        // 本月总收入
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            colors = CardDefaults.cardColors(containerColor = AccentGreen.copy(alpha = 0.1f))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("本月总收入", fontSize = 14.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "¥${DecimalFormat("#,##0.00").format(uiState.totalWage)}",
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    color = AccentGreen
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 每日明细
        if (uiState.records.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                Text("暂无记录", color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(uiState.records, key = { it.id }) { record ->
                    WageRecordItem(
                        record = record,
                        onDelete = { viewModel.deleteRecord(record.id) }
                    )
                }
                item { Spacer(modifier = Modifier.height(16.dp)) }
            }
        }
    }
}

@Composable
private fun WageRecordItem(
    record: DailyWageRecord,
    onDelete: () -> Unit
) {
    val dateLabel = record.date.takeLast(5).replace("-", "/")
    val summary = buildString {
        if (record.basicWage > 0) append("基本:${record.basicWage}")
        if (record.overtimeHours > 0) append(" 加班:${record.overtimeHours}h")
        if (record.extraIncome > 0) append(" 额外:${record.extraIncome}")
    }

    Card(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = dateLabel,
                    fontWeight = FontWeight.Medium,
                    fontSize = 15.sp
                )
                if (summary.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = summary,
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "¥${DecimalFormat("#,##0.00").format(record.totalWage)}",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = AccentGreen
                )
                IconButton(onClick = onDelete) {
                    Icon(
                        Icons.Default.Delete,
                        contentDescription = "删除",
                        tint = MaterialTheme.colorScheme.error,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }
    }
}

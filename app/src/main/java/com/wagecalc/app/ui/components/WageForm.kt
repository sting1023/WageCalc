package com.wagecalc.app.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.wagecalc.app.data.local.DailyWageRecord
import com.wagecalc.app.ui.theme.AccentGreen
import java.text.DecimalFormat

@Composable
fun WageForm(
    record: DailyWageRecord?,
    onSave: (DailyWageRecord) -> Unit,
    modifier: Modifier = Modifier
) {
    var basicWage by remember(record) { mutableStateOf(record?.basicWage?.toString() ?: "") }
    var multiplier by remember(record) { mutableStateOf(record?.multiplier?.toString() ?: "1.0") }
    var overtimeHours by remember(record) { mutableStateOf(record?.overtimeHours?.toString() ?: "") }
    var hourlyWagePreset by remember(record) { mutableStateOf(record?.hourlyWagePreset?.toString() ?: "") }
    var hourlyWageEnabled by remember(record) { mutableStateOf(record?.hourlyWageEnabled ?: false) }
    var dailyWagePreset by remember(record) { mutableStateOf(record?.dailyWagePreset?.toString() ?: "") }
    var dailyWageEnabled by remember(record) { mutableStateOf(record?.dailyWageEnabled ?: false) }
    var extraIncome by remember(record) { mutableStateOf(record?.extraIncome?.toString() ?: "") }

    val previewTotal = remember(basicWage, multiplier, overtimeHours, hourlyWagePreset, hourlyWageEnabled, dailyWagePreset, dailyWageEnabled, extraIncome) {
        val bw = basicWage.toDoubleOrNull() ?: 0.0
        val mult = multiplier.toDoubleOrNull() ?: 1.0
        val oh = overtimeHours.toDoubleOrNull() ?: 0.0
        val hwp = hourlyWagePreset.toDoubleOrNull() ?: 0.0
        val dwp = dailyWagePreset.toDoubleOrNull() ?: 0.0
        val ei = extraIncome.toDoubleOrNull() ?: 0.0
        DailyWageRecord.calculate(bw, mult, oh, hwp, hourlyWageEnabled, dwp, dailyWageEnabled, ei)
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // 预览总额
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = AccentGreen.copy(alpha = 0.1f))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("当日工资预览", fontSize = 14.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "¥${DecimalFormat("#,##0.00").format(previewTotal)}",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = AccentGreen
                )
            }
        }

        // 基本工资
        OutlinedTextField(
            value = basicWage,
            onValueChange = { basicWage = it },
            label = { Text("基本工资 *") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        // 倍数
        OutlinedTextField(
            value = multiplier,
            onValueChange = { multiplier = it },
            label = { Text("基本工资倍数") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        // 加班小时数
        OutlinedTextField(
            value = overtimeHours,
            onValueChange = { overtimeHours = it },
            label = { Text("加班小时数") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        HorizontalDivider()

        // 预设小时工资
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = hourlyWagePreset,
                onValueChange = { hourlyWagePreset = it },
                label = { Text("预设小时工资") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                modifier = Modifier.weight(1f),
                singleLine = true,
                enabled = hourlyWageEnabled
            )
            Spacer(modifier = Modifier.width(8.dp))
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Checkbox(checked = hourlyWageEnabled, onCheckedChange = { hourlyWageEnabled = it })
                Text("启用", fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }

        // 预设日工资
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = dailyWagePreset,
                onValueChange = { dailyWagePreset = it },
                label = { Text("预设日工资") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                modifier = Modifier.weight(1f),
                singleLine = true,
                enabled = dailyWageEnabled
            )
            Spacer(modifier = Modifier.width(8.dp))
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Checkbox(checked = dailyWageEnabled, onCheckedChange = { dailyWageEnabled = it })
                Text("启用", fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }

        // 额外收入
        OutlinedTextField(
            value = extraIncome,
            onValueChange = { extraIncome = it },
            label = { Text("额外收入") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(8.dp))

        // 保存按钮
        Button(
            onClick = {
                val bw = basicWage.toDoubleOrNull() ?: 0.0
                if (bw <= 0) return@Button
                val newRecord = DailyWageRecord(
                    id = record?.id ?: 0,
                    date = record?.date ?: "",
                    basicWage = bw,
                    multiplier = multiplier.toDoubleOrNull() ?: 1.0,
                    overtimeHours = overtimeHours.toDoubleOrNull() ?: 0.0,
                    hourlyWagePreset = hourlyWagePreset.toDoubleOrNull() ?: 0.0,
                    hourlyWageEnabled = hourlyWageEnabled,
                    dailyWagePreset = dailyWagePreset.toDoubleOrNull() ?: 0.0,
                    dailyWageEnabled = dailyWageEnabled,
                    extraIncome = extraIncome.toDoubleOrNull() ?: 0.0,
                    totalWage = previewTotal
                )
                onSave(newRecord)
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = basicWage.toDoubleOrNull()?.let { it > 0 } ?: false
        ) {
            Text("保存", fontSize = 16.sp)
        }
    }
}

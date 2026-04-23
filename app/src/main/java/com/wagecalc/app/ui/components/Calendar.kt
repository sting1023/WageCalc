package com.wagecalc.app.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.wagecalc.app.ui.theme.Primary
import com.wagecalc.app.ui.theme.TextSecondary
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.TextStyle
import java.util.*

@Composable
fun WageCalendar(
    yearMonth: YearMonth,
    selectedDate: LocalDate?,
    datesWithRecords: Set<String>,
    onDateSelected: (LocalDate) -> Unit,
    onPrevMonth: () -> Unit,
    onNextMonth: () -> Unit,
    modifier: Modifier = Modifier
) {
    val daysInMonth = yearMonth.lengthOfMonth()
    val firstDayOfMonth = yearMonth.atDay(1)
    val firstDayOfWeek = firstDayOfMonth.dayOfWeek.value % 7 // 0=Sun

    Column(modifier = modifier.fillMaxWidth()) {
        // Header: Month navigation
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 6.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onPrevMonth, modifier = Modifier.size(32.dp)) {
                Icon(Icons.Filled.KeyboardArrowLeft, contentDescription = "上一月", modifier = Modifier.size(20.dp))
            }
            Text(
                text = "${yearMonth.year}年 ${yearMonth.monthValue}月",
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onBackground
            )
            IconButton(onClick = onNextMonth, modifier = Modifier.size(32.dp)) {
                Icon(Icons.Filled.KeyboardArrowRight, contentDescription = "下一月", modifier = Modifier.size(20.dp))
            }
        }

        // Weekday headers
        Row(modifier = Modifier.fillMaxWidth()) {
            listOf("日", "一", "二", "三", "四", "五", "六").forEach { day ->
                Text(
                    text = day,
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center,
                    fontSize = 12.sp,
                    color = TextSecondary,
                    fontWeight = FontWeight.Medium
                )
            }
        }

        Spacer(modifier = Modifier.height(4.dp))

        // Calendar grid
        val totalCells = firstDayOfWeek + daysInMonth
        val rows = (totalCells + 6) / 7

        for (row in 0 until rows) {
            Row(modifier = Modifier.fillMaxWidth()) {
                for (col in 0 until 7) {
                    val dayIndex = row * 7 + col - firstDayOfWeek + 1
                    if (dayIndex in 1..daysInMonth) {
                        val date = yearMonth.atDay(dayIndex)
                        val dateStr = date.toString()
                        val isSelected = selectedDate == date
                        val hasRecord = datesWithRecords.contains(dateStr)
                        val isToday = date == LocalDate.now()

                        CalendarDay(
                            day = dayIndex,
                            isSelected = isSelected,
                            hasRecord = hasRecord,
                            isToday = isToday,
                            onClick = { onDateSelected(date) },
                            modifier = Modifier.weight(1f),
                            cellSize = 36.dp
                        )
                    } else {
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }
            }
        }
    }
}

@Composable
private fun CalendarDay(
    day: Int,
    isSelected: Boolean,
    hasRecord: Boolean,
    isToday: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    cellSize: androidx.compose.ui.unit.Dp = 40.dp
) {
    Box(
        modifier = modifier
            .size(cellSize)
            .padding(1.dp)
            .clip(CircleShape)
            .background(
                when {
                    isSelected -> Primary
                    isToday -> Primary.copy(alpha = 0.1f)
                    else -> Color.Transparent
                }
            )
            .then(
                if (isToday && !isSelected) {
                    Modifier.border(1.dp, Primary, CircleShape)
                } else Modifier
            )
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = day.toString(),
                fontSize = 14.sp,
                color = when {
                    isSelected -> Color.White
                    else -> MaterialTheme.colorScheme.onBackground
                },
                fontWeight = if (isSelected || isToday) FontWeight.Bold else FontWeight.Normal
            )
            if (hasRecord) {
                Spacer(modifier = Modifier.height(1.dp))
                Box(
                    modifier = Modifier
                        .size(3.dp)
                        .clip(CircleShape)
                        .background(if (isSelected) Color.White else Primary)
                )
            }
        }
    }
}

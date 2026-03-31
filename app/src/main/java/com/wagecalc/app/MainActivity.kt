package com.wagecalc.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.List
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.wagecalc.app.ui.screens.InputScreen
import com.wagecalc.app.ui.screens.SummaryScreen
import com.wagecalc.app.ui.theme.WageCalcTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WageCalcTheme {
                WageCalcContent()
            }
        }
    }
}

sealed class Tab(val title: String, val icon: ImageVector) {
    data object Input : Tab("录入", Icons.Default.Add)
    data object Summary : Tab("汇总", Icons.Default.List)

    companion object {
        val all = listOf(Input, Summary)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WageCalcContent() {
    var selectedTab by remember { mutableStateOf<Tab>(Tab.Input) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("工资计算器") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        },
        bottomBar = {
            NavigationBar {
                Tab.all.forEach { tab ->
                    NavigationBarItem(
                        icon = { Icon(tab.icon, contentDescription = tab.title) },
                        label = { Text(tab.title) },
                        selected = selectedTab == tab,
                        onClick = { selectedTab = tab }
                    )
                }
            }
        }
    ) { paddingValues ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            color = MaterialTheme.colorScheme.background
        ) {
            when (selectedTab) {
                Tab.Input -> InputScreen()
                Tab.Summary -> SummaryScreen()
            }
        }
    }
}

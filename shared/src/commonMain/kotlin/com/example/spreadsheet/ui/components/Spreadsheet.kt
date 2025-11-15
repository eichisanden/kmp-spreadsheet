package com.example.spreadsheet.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import com.example.spreadsheet.data.model.Column
import com.example.spreadsheet.ui.viewmodel.SpreadsheetViewModel

@Composable
fun Spreadsheet(
    viewModel: SpreadsheetViewModel = remember { SpreadsheetViewModel() },
    modifier: Modifier = Modifier
) {
    val state by viewModel.state.collectAsState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5))
    ) {
        // ヘッダー
        SpreadsheetHeader(
            columns = state.columns,
            onColumnWidthChange = { columnIndex, newWidth ->
                viewModel.updateColumnWidth(columnIndex, newWidth)
            }
        )

        // 行
        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            items(
                items = state.rows,
                key = { it.id }
            ) { row ->
                SpreadsheetRowView(
                    row = row,
                    columns = state.columns,
                    selectedCell = state.selectedCell,
                    onCellClick = { rowId, columnIndex ->
                        viewModel.selectCell(rowId, columnIndex)
                    },
                    onCellValueChange = { rowId, columnIndex, value ->
                        viewModel.updateCellValue(rowId, columnIndex, value)
                    },
                    onToggleExpansion = { rowId ->
                        viewModel.toggleRowExpansion(rowId)
                    },
                    onHeightChange = { rowId, newHeight ->
                        viewModel.updateRowHeight(rowId, newHeight)
                    }
                )
            }
        }
    }
}

@Composable
fun SpreadsheetHeader(
    columns: List<Column>,
    onColumnWidthChange: (Int, Float) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(40.dp)
            .background(MaterialTheme.colorScheme.surfaceVariant),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // ツリーボタン用のスペース
        Spacer(modifier = Modifier.width(32.dp))

        columns.forEach { column ->
            HeaderCell(
                column = column,
                onWidthChange = { newWidth ->
                    onColumnWidthChange(column.index, newWidth)
                }
            )
        }
    }
}

@Composable
fun HeaderCell(
    column: Column,
    onWidthChange: (Float) -> Unit,
    modifier: Modifier = Modifier
) {
    var currentWidth by remember(column.width) { mutableStateOf(column.width) }

    Box(
        modifier = modifier
            .width(currentWidth.dp)
            .height(40.dp)
            .border(0.5.dp, Color.Gray)
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .padding(8.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = column.name,
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        // 幅調整ハンドル
        Box(
            modifier = Modifier
                .width(4.dp)
                .height(40.dp)
                .align(Alignment.CenterEnd)
                .background(Color.Transparent)
                .pointerInput(Unit) {
                    detectDragGestures { change, dragAmount ->
                        change.consume()
                        val newWidth = (currentWidth + dragAmount.x / density).coerceIn(60f, 400f)
                        currentWidth = newWidth
                        onWidthChange(newWidth)
                    }
                }
        )
    }
}

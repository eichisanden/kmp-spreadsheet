package com.example.spreadsheet.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
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
import com.example.spreadsheet.data.model.SpreadsheetRow

@Composable
fun SpreadsheetRowView(
    row: SpreadsheetRow,
    columns: List<Column>,
    selectedCell: Pair<String, Int>?,
    onCellClick: (String, Int) -> Unit,
    onCellValueChange: (String, Int, String) -> Unit,
    onToggleExpansion: (String) -> Unit,
    onHeightChange: (String, Float) -> Unit,
    modifier: Modifier = Modifier
) {
    if (!row.isVisible) return

    var currentHeight by remember(row.height) { mutableStateOf(row.height) }

    Box(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(currentHeight.dp)
                .background(Color.White),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // インデントとツリー展開/折りたたみボタン
            Spacer(modifier = Modifier.width((row.level * 20).dp))

            if (row.hasChildren) {
                IconButton(
                    onClick = { onToggleExpansion(row.id) },
                    modifier = Modifier.width(32.dp)
                ) {
                    Icon(
                        imageVector = if (row.isExpanded) {
                            Icons.Default.KeyboardArrowDown
                        } else {
                            Icons.Default.KeyboardArrowRight
                        },
                        contentDescription = if (row.isExpanded) "折りたたむ" else "展開",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            } else {
                Spacer(modifier = Modifier.width(32.dp))
            }

            // セル
            row.cells.forEach { cell ->
                val columnWidth = columns.getOrNull(cell.columnIndex)?.width ?: 120f
                SpreadsheetCellView(
                    cell = cell,
                    columnWidth = columnWidth,
                    isSelected = selectedCell == (row.id to cell.columnIndex),
                    onCellClick = { onCellClick(row.id, cell.columnIndex) },
                    onValueChange = { newValue ->
                        onCellValueChange(row.id, cell.columnIndex, newValue)
                    }
                )
            }
        }

        // 高さ調整ハンドル
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(4.dp)
                .align(Alignment.BottomCenter)
                .background(Color.Transparent)
                .pointerInput(Unit) {
                    detectDragGestures { change, dragAmount ->
                        change.consume()
                        val newHeight = (currentHeight + dragAmount.y / density).coerceIn(24f, 200f)
                        currentHeight = newHeight
                        onHeightChange(row.id, newHeight)
                    }
                }
        )
    }
}

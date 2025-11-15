package com.example.spreadsheet.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.spreadsheet.data.model.SpreadsheetCell

@Composable
fun SpreadsheetCellView(
    cell: SpreadsheetCell,
    columnWidth: Float,
    isSelected: Boolean,
    onCellClick: () -> Unit,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var isEditing by remember(isSelected) { mutableStateOf(isSelected) }
    var textValue by remember(cell.value) { mutableStateOf(cell.value) }

    LaunchedEffect(cell.value) {
        textValue = cell.value
    }

    LaunchedEffect(isSelected) {
        isEditing = isSelected
    }

    Box(
        modifier = modifier
            .width(columnWidth.dp)
            .fillMaxHeight()
            .border(
                width = if (isSelected) 2.dp else 0.5.dp,
                color = if (isSelected) MaterialTheme.colorScheme.primary else Color.Gray
            )
            .background(
                if (isSelected) MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.1f)
                else Color.White
            )
            .clickable { onCellClick() }
            .padding(8.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        if (isEditing) {
            BasicTextField(
                value = textValue,
                onValueChange = { newValue ->
                    textValue = newValue
                    onValueChange(newValue)
                },
                textStyle = TextStyle(
                    fontSize = 14.sp,
                    color = Color.Black
                ),
                singleLine = true,
                modifier = Modifier.fillMaxHeight()
            )
        } else {
            Text(
                text = cell.value,
                style = MaterialTheme.typography.bodyMedium,
                fontSize = 14.sp
            )
        }
    }
}

package com.example.spreadsheet.data.model

data class SpreadsheetCell(
    val rowId: String,
    val columnIndex: Int,
    val value: String = "",
    val isEditing: Boolean = false
)

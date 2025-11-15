package com.example.spreadsheet.ui.viewmodel

import com.example.spreadsheet.data.model.Column
import com.example.spreadsheet.data.model.SpreadsheetRow

data class SpreadsheetState(
    val columns: List<Column> = emptyList(),
    val rows: List<SpreadsheetRow> = emptyList(),
    val selectedCell: Pair<String, Int>? = null
)

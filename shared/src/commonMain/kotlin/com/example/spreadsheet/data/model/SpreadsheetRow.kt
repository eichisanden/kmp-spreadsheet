package com.example.spreadsheet.data.model

data class SpreadsheetRow(
    val id: String,
    val cells: List<SpreadsheetCell>,
    val height: Float = 48f,
    val level: Int = 0,
    val isExpanded: Boolean = true,
    val hasChildren: Boolean = false,
    val parentId: String? = null,
    val isVisible: Boolean = true
)

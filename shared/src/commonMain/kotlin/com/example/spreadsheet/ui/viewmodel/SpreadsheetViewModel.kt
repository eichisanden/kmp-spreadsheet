package com.example.spreadsheet.ui.viewmodel

import com.example.spreadsheet.data.model.Column
import com.example.spreadsheet.data.model.SpreadsheetCell
import com.example.spreadsheet.data.model.SpreadsheetRow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class SpreadsheetViewModel {
    private val _state = MutableStateFlow(SpreadsheetState())
    val state: StateFlow<SpreadsheetState> = _state.asStateFlow()

    init {
        initializeSampleData()
    }

    private fun initializeSampleData() {
        val columns = listOf(
            Column(0, "A", 120f),
            Column(1, "B", 120f),
            Column(2, "C", 120f),
            Column(3, "D", 120f)
        )

        val rows = listOf(
            createRow("1", level = 0, hasChildren = true, cells = listOf("親行1-A", "親行1-B", "親行1-C", "親行1-D")),
            createRow("1-1", level = 1, parentId = "1", cells = listOf("子行1-1-A", "子行1-1-B", "子行1-1-C", "子行1-1-D")),
            createRow("1-2", level = 1, parentId = "1", cells = listOf("子行1-2-A", "子行1-2-B", "子行1-2-C", "子行1-2-D")),
            createRow("2", level = 0, hasChildren = true, cells = listOf("親行2-A", "親行2-B", "親行2-C", "親行2-D")),
            createRow("2-1", level = 1, parentId = "2", cells = listOf("子行2-1-A", "子行2-1-B", "子行2-1-C", "子行2-1-D")),
            createRow("3", level = 0, hasChildren = false, cells = listOf("通常行3-A", "通常行3-B", "通常行3-C", "通常行3-D"))
        )

        _state.update { it.copy(columns = columns, rows = rows) }
    }

    private fun createRow(
        id: String,
        level: Int,
        parentId: String? = null,
        hasChildren: Boolean = false,
        cells: List<String>
    ): SpreadsheetRow {
        return SpreadsheetRow(
            id = id,
            cells = cells.mapIndexed { index, value ->
                SpreadsheetCell(rowId = id, columnIndex = index, value = value)
            },
            level = level,
            parentId = parentId,
            hasChildren = hasChildren,
            isExpanded = true,
            isVisible = true
        )
    }

    fun updateCellValue(rowId: String, columnIndex: Int, value: String) {
        _state.update { currentState ->
            currentState.copy(
                rows = currentState.rows.map { row ->
                    if (row.id == rowId) {
                        row.copy(
                            cells = row.cells.map { cell ->
                                if (cell.columnIndex == columnIndex) {
                                    cell.copy(value = value)
                                } else {
                                    cell
                                }
                            }
                        )
                    } else {
                        row
                    }
                }
            )
        }
    }

    fun selectCell(rowId: String, columnIndex: Int) {
        _state.update { it.copy(selectedCell = rowId to columnIndex) }
    }

    fun deselectCell() {
        _state.update { it.copy(selectedCell = null) }
    }

    fun toggleRowExpansion(rowId: String) {
        _state.update { currentState ->
            val updatedRows = currentState.rows.map { row ->
                if (row.id == rowId) {
                    row.copy(isExpanded = !row.isExpanded)
                } else {
                    row
                }
            }

            // 子行の表示/非表示を更新
            val visibleRows = updateChildrenVisibility(updatedRows)

            currentState.copy(rows = visibleRows)
        }
    }

    private fun updateChildrenVisibility(rows: List<SpreadsheetRow>): List<SpreadsheetRow> {
        val rowMap = rows.associateBy { it.id }

        return rows.map { row ->
            if (row.parentId != null) {
                val parent = rowMap[row.parentId]
                val isVisible = parent?.isExpanded == true &&
                               isParentChainExpanded(row.parentId, rowMap)
                row.copy(isVisible = isVisible)
            } else {
                row.copy(isVisible = true)
            }
        }
    }

    private fun isParentChainExpanded(parentId: String?, rowMap: Map<String, SpreadsheetRow>): Boolean {
        if (parentId == null) return true

        val parent = rowMap[parentId] ?: return false
        if (!parent.isExpanded) return false

        return isParentChainExpanded(parent.parentId, rowMap)
    }

    fun updateRowHeight(rowId: String, newHeight: Float) {
        _state.update { currentState ->
            currentState.copy(
                rows = currentState.rows.map { row ->
                    if (row.id == rowId) {
                        row.copy(height = newHeight.coerceIn(24f, 200f))
                    } else {
                        row
                    }
                }
            )
        }
    }

    fun updateColumnWidth(columnIndex: Int, newWidth: Float) {
        _state.update { currentState ->
            currentState.copy(
                columns = currentState.columns.map { column ->
                    if (column.index == columnIndex) {
                        column.copy(width = newWidth.coerceIn(60f, 400f))
                    } else {
                        column
                    }
                }
            )
        }
    }
}

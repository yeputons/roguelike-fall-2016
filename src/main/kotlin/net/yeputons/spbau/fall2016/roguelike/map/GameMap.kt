package net.yeputons.spbau.fall2016.roguelike.map

data class Position(val col: Int, val row: Int)

fun neighboring(a: Position, b: Position): Boolean {
    return Math.abs(a.col - b.col) + Math.abs(a.row - b.row) == 1
}

interface Displayable {
    val position: Position
    val char: Char?
}

class GameMap(val mapByRows: Array<Array<Cell>>) {
    val rows = mapByRows.size
    val cols = mapByRows[0].size

    init {
        mapByRows.forEach {
            if (it.size != cols) throw IllegalArgumentException("GameMap is not a rectangle")
        }
    }

    fun getCell(pos: Position): Cell? {
        if (pos.row < 0 || pos.row >= rows) return null
        if (pos.col < 0 || pos.col >= cols) return null
        return mapByRows[pos.row][pos.col]
    }

    fun isPassable(pos: Position): Boolean {
        val cell = getCell(pos) ?: return false
        return cell.passable
    }
}
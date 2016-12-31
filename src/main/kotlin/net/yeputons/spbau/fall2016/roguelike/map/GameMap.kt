package net.yeputons.spbau.fall2016.roguelike.map

data class Position(val col: Int, val row: Int)

interface Displayable {
    val position: Position
    val char: Char?
}

class GameMap(val rows: Array<Array<Cell>>) {
    val height = rows.size
    val width = rows[0].size

    init {
        rows.forEach {
            if (it.size != width) throw IllegalArgumentException("GameMap is not a rectangle")
        }
    }

    fun getCell(pos: Position) = rows[pos.row][pos.col]
}
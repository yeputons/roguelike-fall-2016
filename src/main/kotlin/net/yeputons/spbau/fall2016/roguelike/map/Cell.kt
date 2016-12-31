package net.yeputons.spbau.fall2016.roguelike.map

abstract class Cell(val pos: Position) : Displayable {
    override val position = pos

    abstract val passable: Boolean
}

class GoalCell(pos: Position) : Cell(pos) {
    override val passable: Boolean
        get() = true
    override val char: Char?
        get() = 'G'
}

class EmptyCell(pos: Position) : Cell(pos) {
    override val passable: Boolean
        get() = true
    override val char: Char?
        get() = '.'
}

class WallCell(pos: Position) : Cell(pos) {
    override val passable: Boolean
        get() = false
    override val char: Char?
        get() = '#'
}

class LockedCell(pos: Position) : Cell(pos) {
    private var locked = true

    override val passable: Boolean
        get() = !locked
    override val char: Char?
        get() = if (locked) '$' else ','

    fun unlock() {
        locked = false
    }
}

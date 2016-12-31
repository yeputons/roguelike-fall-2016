package net.yeputons.spbau.fall2016.roguelike.map

interface ActingObject : Displayable {
    fun getNextTurn(): Position
    fun makeNextTurn()
    fun skipNextTurn()
}

abstract class AbstractArtifact(val pos: Position) : ActingObject {
    abstract val description: String

    final override val position = pos

    final override fun getNextTurn(): Position = position

    final override fun makeNextTurn() {
    }

    final override fun skipNextTurn() {
    }
}

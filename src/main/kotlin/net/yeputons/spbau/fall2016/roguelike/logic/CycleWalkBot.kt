package net.yeputons.spbau.fall2016.roguelike.logic

import net.yeputons.spbau.fall2016.roguelike.map.Position

class CycleWalkBot(val path: List<Position>) : AbstractMob() {
    var pointInPath: Int = 0

    override fun getNextTurn(): Position = path[(pointInPath + 1) % path.size]

    override val position: Position
        get() = path[pointInPath]

    override fun makeNextTurn() {
        pointInPath = (pointInPath + 1) % path.size
    }

    override val char: Char? = 'z'

    override fun skipNextTurn() {
    }

    override val initialHealth: Int = 3
    override val attackPoints: Int = 3
    override val defensePoints: Int = 1

    override fun die(logger: GameMessageLogger) {
    }
}
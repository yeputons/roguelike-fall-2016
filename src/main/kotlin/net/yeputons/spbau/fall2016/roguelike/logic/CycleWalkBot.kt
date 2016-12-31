package net.yeputons.spbau.fall2016.roguelike.logic

import net.yeputons.spbau.fall2016.roguelike.map.LockedCell
import net.yeputons.spbau.fall2016.roguelike.map.Position

class CycleWalkBot(val path: List<Position>, val cellLocked: LockedCell?) : AbstractMob() {
    var pointInPath: Int = 0

    override fun getNextTurn(): Position = path[(pointInPath + 1) % path.size]

    override val position: Position = path[pointInPath]

    override fun makeNextTurn() {
        pointInPath = (pointInPath + 1) % path.size
    }

    override val char: Char? =
            if (cellLocked != null)
                'Z'
            else
                'z'

    override fun skipNextTurn() {
    }

    override val initialHealth: Int = 10
    override val defensePoints: Int = 1

    override fun die(logger: GameMessageLogger) {
        if (cellLocked != null) {
            logger.log("Unlocked some cell")
            cellLocked.unlock()
        }
    }
}
package net.yeputons.spbau.fall2016.roguelike.logic

import net.yeputons.spbau.fall2016.roguelike.map.ActingObject


abstract class AbstractMob : ActingObject {
    abstract protected val initialHealth: Int
    abstract protected val defensePoints: Int
    abstract fun die(logger: GameMessageLogger)

    private var health_: Int? = null
    var health: Int
        get() {
            if (health_ == null) {
                health_ = initialHealth
            }
            return health_!!
        }
        private set(newHealth) {
            health_ = newHealth
        }

    fun attacked(points: Int, logger: GameMessageLogger) {
        health -= Math.max(0, points - defensePoints)
        if (health == 0) {
            die(logger)
        }
    }
}

package net.yeputons.spbau.fall2016.roguelike.logic

import net.yeputons.spbau.fall2016.roguelike.map.AbstractArtifact
import net.yeputons.spbau.fall2016.roguelike.map.PlayerStatsChanger
import net.yeputons.spbau.fall2016.roguelike.map.Position

class Player(private var currentPosition: Position) : AbstractMob() {
    private val inventory_ = mutableListOf<AbstractArtifact>()
    private val wearing_ = mutableListOf<AbstractArtifact>()

    val inventory: List<AbstractArtifact> = inventory_
    val wearing: List<AbstractArtifact> = wearing_

    val maxWearing = 3

    public var nextTurn: PlayerTurnDirection = PlayerTurnDirection.STAY

    companion object {
        private fun popArtifact(list: MutableList<AbstractArtifact>, artifact: AbstractArtifact) {
            var found : Int? = null
            list.forEachIndexed { i, elem ->
                if (elem === artifact) {
                    found = i
                }
            }
            if (found === null)
                throw IllegalArgumentException("Artifact was not found in the list")
            list.removeAt(found!!)
        }
    }

    fun pickUp(artifact: AbstractArtifact) {
        inventory_.add(artifact)
    }

    fun putOn(artifact: AbstractArtifact) {
        if (wearing.size >= maxWearing) {
            throw IllegalArgumentException("Cannot wear more than {$maxWearing}")
        }
        popArtifact(inventory_, artifact)
        wearing_.add(artifact)
    }

    fun takeOff(artifact: AbstractArtifact) {
        popArtifact(wearing_, artifact)
        inventory_.add(artifact)
    }

    override fun getNextTurn(): Position {
        return Position(
                col = currentPosition.col + nextTurn.dcol,
                row = currentPosition.row + nextTurn.drow
        )
    }

    override val position: Position
        get() = currentPosition

    override fun makeNextTurn() {
        currentPosition = getNextTurn()
        nextTurn = PlayerTurnDirection.STAY
    }

    override val char = '@'

    override fun skipNextTurn() {
        nextTurn = PlayerTurnDirection.STAY
    }

    override val initialHealth = 5

     override val attackPoints: Int
        get() {
            val sumChange = wearing.map { a ->
                if (a is PlayerStatsChanger)
                    a.changeAttack
                else
                    0
            }.sum()
            return Math.max(0, sumChange)
        }

    override val defensePoints: Int
        get() {
            val sumChange = wearing.map { a ->
                if (a is PlayerStatsChanger)
                    a.changeDefense
                else
                    0
            }.sum()
            return Math.max(0, 1 + sumChange)
        }

    override fun die(logger: GameMessageLogger) {
        logger.log("You died")
    }
}

enum class PlayerTurnDirection(val dcol: Int, val drow: Int) {
    UP(0, -1), RIGHT(1, 0), DOWN(0, 1), LEFT(-1, 0), STAY(0, 0)
}

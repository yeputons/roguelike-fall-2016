package net.yeputons.spbau.fall2016.roguelike.logic

import net.yeputons.spbau.fall2016.roguelike.map.ActingObject
import net.yeputons.spbau.fall2016.roguelike.map.GameMap
import net.yeputons.spbau.fall2016.roguelike.map.GoalCell
import net.yeputons.spbau.fall2016.roguelike.map.Position

class Game(val map: GameMap, val initialObjects: List<ActingObject>, val visibilityRange: Int) {
    val actingObjects = initialObjects.toMutableList()

    val player: Player = actingObjects.find { it is Player } as Player

    val goalCells : List<Position> = {
        val result = mutableListOf<Position>()
        for (row in 0..map.height - 1)
            for (col in 0..map.width - 1)
                if (map.getCell(Position(col = col, row = row)) is GoalCell) {
                    result.add(Position(col = col, row = row))
                }
        result
    }()

    val gameResult: GameResult
        get() {
            if (player.health == 0) {
                return GameResult.DIED
            }
            if (goalCells.contains(player.position)) {
                return GameResult.WON
            }
            return GameResult.NOT_ENDED
        }

    fun canSee(pos: Position): Boolean {
        val dx = pos.col - player.position.col
        val dy = pos.row - player.position.row
        return dx * dx + dy * dy <= visibilityRange * visibilityRange
    }

    val visibilityByRow = Array(map.height, { row -> BooleanArray(map.width, { col -> canSee(Position(col = col, row = row)) }) })

    fun isCellVisible(pos: Position) = visibilityByRow[pos.row][pos.col]

    fun makeTurn(logger : GameMessageLogger) {
        val candidates = mutableMapOf<Position, MutableList<ActingObject>>()
        for (obj in actingObjects) {
            val nextPos = obj.getNextTurn()
            candidates.getOrPut(nextPos, { mutableListOf() }) += actingObjects
        }
        for ((pos, objs) in candidates.entries) {
            if (objs.size > 1) {
                throw NotImplementedError()
            }
            for (obj in objs) {
                obj.makeNextTurn()
            }
        }
        if (gameResult == GameResult.WON) {
            logger.log("You won")
        }
    }
}

enum class GameResult {
    NOT_ENDED, WON, DIED
}
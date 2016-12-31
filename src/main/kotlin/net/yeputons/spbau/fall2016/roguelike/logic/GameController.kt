package net.yeputons.spbau.fall2016.roguelike.logic

import net.yeputons.spbau.fall2016.roguelike.map.*

class GameController(val map: GameMap, val initialObjects: List<ActingObject>, val visibilityRange: Int) {
    val actingObjects = initialObjects.toMutableList()

    val player: Player = actingObjects.find { it is Player } as Player

    val goalCells: List<Position> = {
        val result = mutableListOf<Position>()
        for (row in 0..map.rows - 1)
            for (col in 0..map.cols - 1)
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

    val visibilityByRow = Array(map.rows, { row -> BooleanArray(map.cols, { col -> canSee(Position(col = col, row = row)) }) })

    fun isCellVisible(pos: Position) = visibilityByRow[pos.row][pos.col]

    fun makeTurn(logger: GameMessageLogger) {
        val candidates = mutableMapOf<Position, MutableList<ActingObject>>()
        for (artifact in player.wearing)
            if (artifact is Key) {
                val cell = map.getCell(player.getNextTurn())
                if (cell != null) {
                    artifact.tryAgainst(cell, logger)
                }
            }
        for (obj in actingObjects) {
            val nextPos = obj.getNextTurn()
            candidates.getOrPut(nextPos, { mutableListOf() }) += obj
        }
        for ((pos, objs) in candidates.entries) {
            if (!map.isPassable(pos)) {
                for (obj in objs) {
                    obj.skipNextTurn()
                }
            } else {
                val toRemove: List<ActingObject> = InCellConflictResolver(objs, logger).resolve()
                actingObjects.removeAll(toRemove)
            }
        }
        for (row in 0..map.rows - 1)
            for (col in 0..map.cols - 1)
                if (canSee(Position(col = col, row = row))) {
                    visibilityByRow[row][col] = true
                }
        if (gameResult == GameResult.WON) {
            logger.log("You won")
        }
    }
}

enum class GameResult {
    NOT_ENDED, WON, DIED
}
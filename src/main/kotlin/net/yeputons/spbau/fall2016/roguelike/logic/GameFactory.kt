package net.yeputons.spbau.fall2016.roguelike.logic

import net.yeputons.spbau.fall2016.roguelike.map.*
import java.io.InputStream
import java.util.*

object GameFactory {
    fun createCycleFromPath(path: List<Position>): List<Position> {
        val result = mutableListOf<Position>()
        val start = 0
        fun dfs(i: Int, previous: Int) {
            if (i == start && previous >= 0) {
                return
            }
            result += path[i]
            for (next in 0..path.size - 1) {
                if (next == i || next == previous) continue
                if (neighboring(path[i], path[next])) {
                    dfs(next, i)
                    break
                }
            }
        }
        dfs(0, -1)
        return result
    }

    fun createFromDescription(inp: InputStream): GameController {
        val scanner = Scanner(inp)
        val rows = scanner.nextInt()
        val cols = scanner.nextInt()
        val visibilityRange = scanner.nextInt()
        scanner.nextLine()
        val cells = Array(rows, { row -> Array<Cell>(cols, { col -> EmptyCell(Position(col = col, row = row)) }) })

        var playerPos: Position? = null
        val lockedCells = mutableMapOf<Char, LockedCell>()
        val keys = mutableMapOf<Char, MutableList<Position>>()
        val paths = mutableMapOf<Char, MutableList<Position>>()
        val objs = mutableListOf<ActingObject>()

        for (row in 0..rows - 1) {
            val s = scanner.nextLine()
            if (s.length != cols) {
                throw InputMismatchException("Expected line of length ${cols}, got {$s.length}")
            }
            for (col in 0..cols - 1) {
                val pos = Position(col = col, row = row)
                val c = s[col]
                cells[row][col] =
                    when (c) {
                        '.' -> EmptyCell(pos)
                        '#' -> WallCell(pos)
                        '$' -> LockedCell(pos)
                        '!' -> GoalCell(pos)
                        '@' -> {
                            if (playerPos != null) {
                                throw InputMismatchException("Player occured twice: at $playerPos and $pos")
                            }
                            playerPos = pos
                            EmptyCell(pos)
                        }
                        '[' -> {
                            objs.add(PlayerStatsChanger(pos, 1, 0))
                            EmptyCell(pos)
                        }
                        '|' -> {
                            objs.add(PlayerStatsChanger(pos, 1, 1))
                            EmptyCell(pos)
                        }
                        ']' -> {
                            objs.add(PlayerStatsChanger(pos, 0, 1))
                            EmptyCell(pos)
                        }
                        in 'A'..'Z' -> {
                            if (c in lockedCells) {
                                throw InputMismatchException("Locked cell ${c} occurs twice")
                            }
                            val cell = LockedCell(pos)
                            lockedCells[c] = cell
                            cell
                        }
                        in 'a'..'z' -> {
                            keys.getOrPut(c.toUpperCase(), { mutableListOf() }) += pos
                            EmptyCell(pos)
                        }
                        in '0'..'9' -> {
                            paths.getOrPut(c, { mutableListOf() }) += pos
                            EmptyCell(pos)
                        }
                        else ->
                                throw InputMismatchException("Illegal character at $pos: '${c}'")
                    }
            }
        }

        val player = Player(playerPos ?: throw InputMismatchException("Player's position was not found"))
        objs.add(player)
        for ((char, poses) in keys) {
            val cell = lockedCells.get(char)
            if (cell == null) {
                throw InputMismatchException("Unable to find cell ${char} for key at ${poses[0]}")
            }
            for (pos in poses) {
                objs.add(Key(pos, cell, "$char"))
            }
        }

        for ((char, path) in paths) {
            objs.add(CycleWalkBot(createCycleFromPath(path)))
        }

        return GameController(GameMap(cells), objs, visibilityRange)
    }
}
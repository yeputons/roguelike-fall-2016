package net.yeputons.spbau.fall2016.roguelike.map

import net.yeputons.spbau.fall2016.roguelike.logic.GameMessageLogger

class Key(pos: Position, val cellLocked : LockedCell, val name: String) : AbstractArtifact(pos) {
    override val char: Char? = 'k'

    override val description = "Key(${name})"

    fun tryAgainst(cell: Cell, logger: GameMessageLogger) {
        if (cell == cellLocked) {
            logger.log("Cell was unlocked by ${description}!")
            cellLocked.unlock()
        }
    }
}

class PlayerStatsChanger(pos: Position, val changeAttack: Int, val changeDefense: Int) : AbstractArtifact(pos) {
    override val char: Char? = 'o'

    override val description = "StatsChanger(a=${changeAttack}, d=${changeDefense})"
}

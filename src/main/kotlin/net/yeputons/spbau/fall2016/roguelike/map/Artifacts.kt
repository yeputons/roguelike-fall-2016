package net.yeputons.spbau.fall2016.roguelike.map

class Key(pos: Position, val cellLocked : LockedCell) : AbstractArtifact(pos) {
    override val char: Char? = 'k'

    override val description = "Key for a cell"
}

class PlayerStatsChanger(pos: Position, val changeAttack: Int, val changeDefense: Int) : AbstractArtifact(pos) {
    override val char: Char? = 'o'

    override val description = "Stats changer: attack by ${changeAttack}, defense by ${changeDefense}"
}

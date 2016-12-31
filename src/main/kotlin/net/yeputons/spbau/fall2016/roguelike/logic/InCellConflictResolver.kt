package net.yeputons.spbau.fall2016.roguelike.logic

import net.yeputons.spbau.fall2016.roguelike.map.AbstractArtifact
import net.yeputons.spbau.fall2016.roguelike.map.ActingObject

interface GameMessageLogger {
    fun log(message: String)
}

class InCellConflictResolver(val objs: List<ActingObject>, val logger: GameMessageLogger) {
    fun resolve(): List<ActingObject> {
        val player = objs.find { it is Player } as Player?
        if (player == null) {
            objs.forEach { it.makeNextTurn() }
            return emptyList()
        }

        val artifacts = objs.filter { it is AbstractArtifact }.map { it as AbstractArtifact }
        val mobs = objs.filter { it is AbstractMob && it !== player }.map { it as AbstractMob }

        artifacts.forEach {
            logger.log("You have picked something up: '${it.description}'")
            player.pickUp(it)
        }

        val died = mutableListOf<ActingObject>()
        if (mobs.isEmpty()) {
            player.makeNextTurn()
        } else {
            player.skipNextTurn()
            mobs.forEach {
                it.skipNextTurn()

                val zAttack = it.attackPoints
                logger.log("You've been attacked with ${zAttack}")
                player.attacked(zAttack, logger)
                if (player.health > 0) {
                    val myAttack = player.attackPoints
                    it.attacked(myAttack, logger)
                    logger.log("You've hit zombie with ${myAttack}, it has ${it.health} health left")
                    if (it.health == 0) {
                        died.add(it)
                    }
                }
            }
        }
        return artifacts + died
    }
}
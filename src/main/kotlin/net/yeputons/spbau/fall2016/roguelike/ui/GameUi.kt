package net.yeputons.spbau.fall2016.roguelike.ui

import net.yeputons.spbau.fall2016.roguelike.logic.GameController
import net.yeputons.spbau.fall2016.roguelike.logic.GameMessageLogger
import net.yeputons.spbau.fall2016.roguelike.logic.PlayerTurnDirection
import java.awt.event.KeyEvent

class GameUi(val gameController: GameController) {
    var logger: GameMessageLogger? = null
    val player = gameController.player

    fun processKey(keyCode: Int) {
        val logger = this.logger
        if (logger == null) {
            throw IllegalStateException("GameMessageLogger is not set")
        }
        when (keyCode) {
            KeyEvent.VK_LEFT -> {
                player.nextTurn = PlayerTurnDirection.LEFT
                gameController.makeTurn(logger)
            }
            KeyEvent.VK_UP -> {
                player.nextTurn = PlayerTurnDirection.UP
                gameController.makeTurn(logger)
            }
            KeyEvent.VK_RIGHT -> {
                player.nextTurn = PlayerTurnDirection.RIGHT
                gameController.makeTurn(logger)
            }
            KeyEvent.VK_DOWN -> {
                player.nextTurn = PlayerTurnDirection.DOWN
                gameController.makeTurn(logger)
            }
            KeyEvent.VK_SPACE -> {
                player.nextTurn = PlayerTurnDirection.STAY
                gameController.makeTurn(logger)
            }
            KeyEvent.VK_S -> {
                logger.log("You wear items: " + player.wearing.map { it.description }.joinToString(", "))
                logger.log("You have items:" + player.inventory.mapIndexed { i, artifact -> "\n  $i: ${artifact.description}" }.joinToString(""))
                logger.log("Your health: ${player.health}, your attack: ${player.attackPoints}, your defense: ${player.defensePoints}")
            }
            KeyEvent.VK_D -> {
                player.wearing.toList().forEach { player.takeOff(it) }
                logger.log("Took off all items")
            }
            in KeyEvent.VK_0..KeyEvent.VK_9 -> {
                val id = keyCode - KeyEvent.VK_0
                if (id >= player.inventory.size) {
                    logger.log("No element ${id} in inventory")
                    return
                }
                if (player.wearing.size >= player.maxWearing) {
                    logger.log("Cannot wear more than ${player.maxWearing} items")
                    return
                }
                val item = player.inventory[id]
                logger.log("Put on item ${id} from inventory: ${item.description}")
                player.putOn(item)
            }
        }
    }
}

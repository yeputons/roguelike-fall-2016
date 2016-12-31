package net.yeputons.spbau.fall2016.roguelike.ui

import net.yeputons.spbau.fall2016.roguelike.logic.Game
import net.yeputons.spbau.fall2016.roguelike.logic.GameMessageLogger
import net.yeputons.spbau.fall2016.roguelike.logic.PlayerTurnDirection
import java.awt.event.KeyEvent

class GameController(val game: Game, val logger: GameMessageLogger) {
    fun processKey(keyCode: Int) {
        when (keyCode) {
            KeyEvent.VK_LEFT -> {
                game.player.nextTurn = PlayerTurnDirection.LEFT
                game.makeTurn(logger)
            }
            KeyEvent.VK_UP -> {
                game.player.nextTurn = PlayerTurnDirection.UP
                game.makeTurn(logger)
            }
            KeyEvent.VK_RIGHT -> {
                game.player.nextTurn = PlayerTurnDirection.RIGHT
                game.makeTurn(logger)
            }
            KeyEvent.VK_DOWN -> {
                game.player.nextTurn = PlayerTurnDirection.DOWN
                game.makeTurn(logger)
            }
            KeyEvent.VK_SPACE -> {
                game.player.nextTurn = PlayerTurnDirection.STAY
                game.makeTurn(logger)
            }
        }
    }
}

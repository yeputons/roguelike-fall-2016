package net.yeputons.spbau.fall2016.roguelike.ui

import net.yeputons.spbau.fall2016.roguelike.logic.GameFactory
import net.yeputons.spbau.fall2016.roguelike.logic.GameMessageLogger
import javax.swing.JFrame
import javax.swing.JOptionPane

fun main(args: Array<String>) {
    Roguelike().run()
}

class Roguelike {
    fun run() {
        val game = javaClass.classLoader.getResourceAsStream("default.txt").use { GameFactory.createFromDescription(it) }

        val frame = JFrame("Roguelike")
        val controller = GameUi(game)
        val panel = GamePanel(controller)
        controller.logger = panel

        frame.add(panel)
        frame.pack()
        frame.defaultCloseOperation = JFrame.EXIT_ON_CLOSE
        frame.isResizable = false
        frame.isVisible = true
    }
}
package net.yeputons.spbau.fall2016.roguelike.ui

import asciiPanel.AsciiFont
import asciiPanel.AsciiPanel
import net.yeputons.spbau.fall2016.roguelike.logic.GameMessageLogger
import net.yeputons.spbau.fall2016.roguelike.logic.GameResult
import net.yeputons.spbau.fall2016.roguelike.map.Position
import java.awt.Font
import java.awt.event.KeyAdapter
import java.awt.event.KeyEvent
import javax.swing.*

class GamePanel(val gameUi: GameUi) : JPanel(), GameMessageLogger {
    val panel = AsciiPanel(gameUi.gameController.map.cols, gameUi.gameController.map.rows, AsciiFont.CP437_16x16)
    val log = JTextArea()
    var loggedInThisTurn = false

    val keyListener = object : KeyAdapter() {
        override fun keyPressed(e: KeyEvent?) {
            if (gameUi.gameController.gameResult != GameResult.NOT_ENDED) {
                return
            }
            if (e != null) {
                loggedInThisTurn = false
                gameUi.processKey(e.keyCode)
                redraw()
            }
        }
    }

    init {
        log.isEditable = false
        log.isFocusable = false
        log.font = Font("Monospaced", Font.PLAIN, 14)
        log.columns = 80
        log.rows = 20

        val box = Box.createVerticalBox()
        box.add(panel)
        box.add(JScrollPane(log))
        add(box)

        isFocusable = true
        addKeyListener(keyListener)
        redraw()
    }

    override fun log(message: String) {
        if (!loggedInThisTurn) {
            log.append("\n")
        }
        loggedInThisTurn = true
        log.append("$message\n")
    }

    fun redraw() {
        panel.clear()
        val data = Array(gameUi.gameController.map.rows, { row ->
            Array<Pair<Int, Char>>(gameUi.gameController.map.cols, { col ->
                val pos = Position(col = col, row = row)
                val char = gameUi.gameController.map.getCell(pos)!!.char
                if (char == null) {
                    Pair(0, ' ')
                } else {
                    Pair(1, char)
                }
            })
        })

        for (obj in gameUi.gameController.actingObjects) {
            val row = obj.position.row
            val col = obj.position.col
            val lev = 2
            if (obj.char != null && data[row][col].first < lev) {
                data[row][col] = Pair(lev, obj.char!!)
            }
        }

        for (row in 0..gameUi.gameController.map.rows - 1)
            for (col in 0..gameUi.gameController.map.cols - 1) {
                val pos = Position(col = col, row = row)
                if (!gameUi.gameController.isCellVisible(pos)) {
                    continue
                }
                panel.write(data[row][col].second, col, row)
            }
        panel.repaint()
    }
}

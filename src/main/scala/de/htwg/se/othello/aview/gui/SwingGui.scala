package de.htwg.se.othello.aview.gui

import java.awt.Color

import scala.swing._
import de.htwg.se.othello.controller._
import de.htwg.se.othello.util.Observer
import javax.swing.ImageIcon

import scala.swing.event.Key

class SwingGui(controller: Controller) extends Frame with Observer {

  private val white = new Label {icon = new ImageIcon("resources/white.png")}
  private val black = new Label { icon = new ImageIcon("resources/black.png") }
  private val empty = new Label("")
  private val highlight = new Label { icon = new ImageIcon("resources/highlight.png") }

  controller.add(this)

  title = "Othello"

  menuBar = new MenuBar {
    contents += new Menu("File") {
      mnemonic = Key.F
      contents += new MenuItem(Action("New Game") {
        controller.createBoard(controller.board.size)
      })
      contents += new MenuItem(Action("Quit") {
        System.exit(0)
      })
    }
    contents += new Menu("Edit") {
      mnemonic = Key.E
      contents += new MenuItem(Action("Undo") {
        controller.undo()
      })
      contents += new MenuItem(Action("Redo") {
        controller.redo()
      })
    }
    contents += new Menu("Options") {
      mnemonic = Key.O
      contents += new MenuItem(Action("Highlight possible moves") { controller.highlight() })
      contents += new MenuItem(Action("Reduce board size") { controller.resizeBoard("-") })
      contents += new MenuItem(Action("Increase board size") { controller.resizeBoard("+") })
      contents += new MenuItem(Action("Reset board") { controller.resizeBoard(".") })
    }
  }


  def table: Table = new Table(controller.board.size,controller.board.size) {
    background = new Color(0, 170, 0)
    gridColor = Color.DARK_GRAY
    rowHeight = 50
    preferredSize = new Dimension(400,400)

    override def rendererComponent(isSelected: Boolean, focused: Boolean, row: Int, column: Int): Component = {
      controller.board.valueOf(column, row) match {
        case 1 => black
        case 2 => white
        case 0 => empty
        case -1 => if (controller.player.value == 1) highlight else white
      }
    }
  }

  contents = table

  visible = true
  location = new Point(200,200)
  peer.setAlwaysOnTop(true)

  peer.setDefaultCloseOperation(3)

  def redraw(): Unit = {
    for {
      x <- 0 until table.rowCount
      y <- 0 until table.rowCount
    } table.update(x, y, controller.board.valueOf(x, y))
    repaint()
  }
  def update: Boolean = {
    redraw()
    controller.gameStatus = GameStatus.IDLE
    true
  }
}

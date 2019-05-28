package de.htwg.se.othello.aview.gui

import java.awt.Color

import scala.swing._
import de.htwg.se.othello.controller._
import de.htwg.se.othello.util.Observer
import javax.swing.ImageIcon

import scala.swing.event.{Key, MouseClicked}

class SwingGui(controller: Controller) extends Frame with Observer {

  controller.add(this)

  private val white = new Label { icon = new ImageIcon("resources/white_leather_shadow.png") }
  private val black = new Label { icon = new ImageIcon("resources/black_leather_shadow.png") }
  private val highlight = new Label { icon = new ImageIcon("resources/black_dot.png") }

  title = "Othello"

  menuBar = new MenuBar {
    contents += new Menu("File") {
      mnemonic = Key.F
      contents += new MenuItem(Action("New Game") { controller.newGame() })
      contents += new MenuItem(Action("Quit") { controller.exit() })
    }
    contents += new Menu("Edit") {
      mnemonic = Key.E
      contents += new MenuItem(Action("Undo") { controller.undo() })
      contents += new MenuItem(Action("Redo") { controller.redo() })
    }
    contents += new Menu("Options") {
      mnemonic = Key.O
      contents += new MenuItem(Action("Highlight possible moves") {
        controller.highlight()
      })
      contents += new MenuItem(Action("Reduce board size") {
        controller.resizeBoard("-")
      })
      contents += new MenuItem(Action("Increase board size") {
        controller.resizeBoard("+")
      })
      contents += new MenuItem(Action("Reset board size") {
        controller.resizeBoard(".")
      })
      contents += new Menu("Player count") {
        contents += new MenuItem(Action("1") {controller.setupPlayers("1")})
        contents += new MenuItem(Action("2") {controller.setupPlayers("2")})
      }
    }
  }

  def table: Table = new Table(controller.board.size, controller.board.size) {
    background = new Color(10, 91, 10)
    gridColor = new Color(20, 20, 20)
    rowHeight = 50
    preferredSize = new Dimension(400, 400)
    listenTo(mouse.clicks)
    reactions += {
      case e: MouseClicked =>
        if (controller.board.gameOver) controller.newGame()
        else controller.set(e.point.x / rowHeight, e.point.y / rowHeight)
    }
    override def rendererComponent(isSelected: Boolean, focused: Boolean, row: Int, column: Int): Component = {
      controller.board.valueOf(column, row) match {
        case 1 => black
        case 2 => white
        case 0 => new Label("")
        case -1 => highlight
      }
    }
  }

  def top: GridPanel = new GridPanel(1, 9) {
    background = Color.lightGray
    preferredSize = new Dimension(450, 50)
    contents += new Label("")
    contents += new Label("A")
    contents += new Label("B")
    contents += new Label("C")
    contents += new Label("D")
    contents += new Label("E")
    contents += new Label("F")
    contents += new Label("G")
    contents += new Label("H")
  }

  def left: GridPanel = new GridPanel(8, 1) {
    background = Color.lightGray
    preferredSize = new Dimension(50, 400)
    contents += new Label("1")
    contents += new Label("2")
    contents += new Label("3")
    contents += new Label("4")
    contents += new Label("5")
    contents += new Label("6")
    contents += new Label("7")
    contents += new Label("8")
  }

  def panel: BorderPanel = new BorderPanel {
    add(top, BorderPanel.Position.North)
    add(left, BorderPanel.Position.West)
    add(table, BorderPanel.Position.Center)
  }

  contents = panel

  visible = true
  location = new Point(200, 200)
  // peer.setAlwaysOnTop(true)
  peer.setDefaultCloseOperation(3)

  def update: Boolean = {
    repaint()
    true
  }
}

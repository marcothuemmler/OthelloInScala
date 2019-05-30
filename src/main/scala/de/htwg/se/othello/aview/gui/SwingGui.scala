package de.htwg.se.othello.aview.gui

import scala.swing._
import de.htwg.se.othello.controller._
import de.htwg.se.othello.util.Observer

import scala.swing.event.Key

class SwingGui(controller: Controller) extends Frame with Observer {

  controller.add(this)

  title = "Othello"

  menuBar = new MenuBar {
    contents += new Menu("File") {
      mnemonic = Key.F
      contents += new MenuItem(Action("New Game") {
        controller.newGame()
      })
      contents += new MenuItem(Action("Quit") {
        controller.exit()
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
      contents += new Menu("Game mode") {
        contents += new MenuItem(Action("Player vs. Computer") {
          controller.setupPlayers("1")
        })
        contents += new MenuItem(Action("Player vs. Player") {
          controller.setupPlayers("2")
        })
      }
    }
  }

  lazy val tablePanel = new TablePanel(controller)
  contents = tablePanel
  // peer.setAlwaysOnTop(true)

  def update: Boolean = {
    tablePanel.redraw()
    pack
    centerOnScreen
    repaint
    resizable = false
    visible = true
    true
  }
}

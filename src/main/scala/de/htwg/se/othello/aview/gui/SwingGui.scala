package de.htwg.se.othello.aview.gui

import scala.swing._
import de.htwg.se.othello.controller._
import de.htwg.se.othello.util.Observer
import scala.swing.event.Key

class SwingGui(controller: Controller) extends Observer {

  controller.add(this)

  lazy val tablePanel = new TablePanel(controller)

  lazy val mainFrame: MainFrame = new MainFrame {
    title = "Othello"
    menuBar = menus
    contents = tablePanel
    centerOnScreen
    // peer.setAlwaysOnTop(true)
    resizable = false
  }

  def menus: MenuBar = new MenuBar {
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

  def update: Boolean = {
    tablePanel.redraw()
    mainFrame.pack
    mainFrame.repaint
    mainFrame.visible = true
    true
  }
}

package de.htwg.se.othello.aview.gui

import scala.swing._
import de.htwg.se.othello.controller._
import de.htwg.se.othello.util.Observer

import scala.swing.event.{ButtonClicked, Key}

class SwingGui(controller: Controller) extends Observer {

  controller.add(this)

  lazy val tablePanel = new TablePanel(controller)

  lazy val mainFrame: MainFrame = new MainFrame {
    title = "Othello"
    menuBar = menus
    contents = tablePanel
    centerOnScreen
    resizable = false
  }

  def menus: MenuBar = new MenuBar {
    contents += new Menu("File") {
      mnemonic = Key.F
      contents += new MenuItem(Action("New Game") { controller.newGame() })
      contents += new MenuItem(Action("Quit") { sys.exit })
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
        val pvc: RadioMenuItem = new RadioMenuItem("Player vs. Computer") {
          selected = true
        }
        val pvp = new RadioMenuItem("Player vs. Player")
        val cvc = new RadioMenuItem("Demo mode (Computer vs Computer)")
        val mode = new ButtonGroup(pvc, pvp, cvc)
        contents ++= mode.buttons
        listenTo(pvc, pvp, cvc)
        reactions += {
          case e: ButtonClicked =>
            if (e.source == pvp) controller.setupPlayers("2")
            if (e.source == pvc) controller.setupPlayers("1")
            if (e.source == cvc) controller.setupPlayers("0")
        }
      }
    }
  }

  def update: Boolean = {
    tablePanel.redraw()
    mainFrame.pack
    mainFrame.visible = true
    true
  }
}

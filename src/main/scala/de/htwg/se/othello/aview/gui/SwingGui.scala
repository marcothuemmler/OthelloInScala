package de.htwg.se.othello.aview.gui

import java.awt.event.KeyEvent

import scala.swing._
import de.htwg.se.othello.controller._
import de.htwg.se.othello.util.Observer
import javax.swing.KeyStroke
import scala.swing.event.Key.Modifier
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
      contents += new MenuItem(new Action("New Game") {
        accelerator = Some(KeyStroke.getKeyStroke("ctrl N"))

        override def apply: Unit = controller.newGame()
      })
      contents += new MenuItem(new Action("Quit") {
        accelerator = Some(KeyStroke.getKeyStroke("ctrl Q"))

        override def apply: Unit = sys.exit
      })
    }
    contents += new Menu("Edit") {
      mnemonic = Key.E
      contents += new MenuItem(new Action("Undo") {
        accelerator = Some(KeyStroke.getKeyStroke("ctrl Z"))

        override def apply: Unit = controller.undo()
      })
      contents += new MenuItem(new Action("Redo") {
        accelerator = Some(KeyStroke.getKeyStroke("ctrl Y"))

        override def apply: Unit = controller.redo()
      })
    }
    contents += new Menu("Options") {
      mnemonic = Key.O
      contents += new MenuItem(new Action("Highlight possible moves") {
        accelerator = Some(KeyStroke.getKeyStroke("ctrl H"))

        override def apply: Unit = controller.highlight()
      })
      contents += new MenuItem(new Action("Reduce board size") {
        accelerator = Some(KeyStroke.getKeyStroke(45, Modifier.Control))

        override def apply: Unit = controller.resizeBoard("-")
      })
      contents += new MenuItem(new Action("Increase board size") {
        accelerator = Some(KeyStroke.getKeyStroke(KeyEvent.VK_PLUS, Modifier.Control))

        override def apply: Unit = controller.resizeBoard("+")
      })
      contents += new MenuItem(new Action("Reset board size") {
        accelerator = Some(KeyStroke.getKeyStroke(46, Modifier.Control))

        override def apply: Unit = controller.resizeBoard(".")
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
    mainFrame.repaint
    mainFrame.pack
    mainFrame.visible = true
    true
  }
}

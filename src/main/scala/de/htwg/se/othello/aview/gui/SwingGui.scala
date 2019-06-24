package de.htwg.se.othello.aview.gui

import java.awt.event.KeyEvent

import scala.swing._
import de.htwg.se.othello.controller.controllerComponent.{BoardChanged, ControllerInterface, PlayerOmitted}
import javax.swing.KeyStroke

import scala.swing.event.Key.{Modifier, Modifiers}
import scala.swing.event.{ButtonClicked, Key}

class SwingGui(controller: ControllerInterface) extends Reactor {

  listenTo(controller)

  lazy val tablePanel = new TablePanel(controller)

  lazy val mainFrame: MainFrame = new MainFrame {
    title = "Othello"
    menuBar = menus
    contents = tablePanel
    centerOnScreen
    resizable = false
  }

  val modifier: Modifiers = {
    if (System.getProperty("os.name").startsWith("Mac")) Modifier.Meta
    else Modifier.Control
  }

  def menus: MenuBar = new MenuBar {
    contents += new Menu("File") {
      mnemonic = Key.F
      contents += new MenuItem(new Action("New Game") {
        accelerator = Some(KeyStroke.getKeyStroke(KeyEvent.VK_N, modifier))
        override def apply: Unit = controller.newGame
      })
      contents += new MenuItem(new Action("Quit") {
        accelerator = Some(KeyStroke.getKeyStroke(KeyEvent.VK_Q, modifier))
        override def apply: Unit = sys.exit
      })
    }
    contents += new Menu("Edit") {
      mnemonic = Key.E
      contents += new MenuItem(new Action("Undo") {
        accelerator = Some(KeyStroke.getKeyStroke(KeyEvent.VK_Z, modifier))
        override def apply: Unit = controller.undo()
      })
      contents += new MenuItem(new Action("Redo") {
        accelerator = Some(KeyStroke.getKeyStroke(KeyEvent.VK_Y, modifier))
        override def apply: Unit = controller.redo()
      })
    }
    contents += new Menu("Options") {
      mnemonic = Key.O
      contents += new MenuItem(new Action("Highlight possible moves") {
        enabled = if (controller.moves.isEmpty) false else true
        accelerator = Some(KeyStroke.getKeyStroke(KeyEvent.VK_I, modifier))
        override def apply: Unit = controller.highlight()
      })
      contents += new MenuItem(new Action("Reduce board size") {
        enabled = if (controller.size <= 4) false else true
        accelerator = Some(KeyStroke.getKeyStroke(45, modifier))
        override def apply: Unit = controller.resizeBoard("-")
      })
      contents += new MenuItem(new Action("Increase board size") {
        accelerator = Some(KeyStroke.getKeyStroke(KeyEvent.VK_PLUS, modifier))
        override def apply: Unit = controller.resizeBoard("+")
      })
      contents += new MenuItem(new Action("Reset board size") {
        enabled = if (controller.size == 8) false else true
        accelerator = Some(KeyStroke.getKeyStroke(46, modifier))
        override def apply: Unit = controller.resizeBoard(".")
      })
      contents += new Menu("Game mode") {
        val pvc: RadioMenuItem = new RadioMenuItem("Player vs. Computer") {
          selected = if (controller.playerCount == 1) true else false
        }
        val pvp: RadioMenuItem = new RadioMenuItem("Player vs. Player") {
          selected = if (controller.playerCount == 2) true else false
        }
        val cvc: RadioMenuItem = new RadioMenuItem("Demo mode (Computer vs Computer)") {
          selected = if (controller.playerCount == 0) true else false
        }
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
      contents += new Menu("Game Difficulty") {
        val easy: RadioMenuItem = new RadioMenuItem("Easy") {
          enabled = if (controller.size == 8) true else false
          selected = if (controller.difficulty == 1) true else false
        }
        val medium: RadioMenuItem = new RadioMenuItem("Normal") {
          enabled = if (controller.size == 8) true else false
          selected = if (controller.difficulty == 2) true else false
        }
        val hard: RadioMenuItem = new RadioMenuItem("Hard") {
          enabled = if (controller.size == 8) true else false
          selected = if (controller.difficulty == 3) true else false
        }
        val mode = new ButtonGroup(easy, medium, hard)
        contents ++= mode.buttons
        listenTo(easy, medium, hard)
        reactions += {
          case e: ButtonClicked =>
            if (e.source == easy) controller.setDifficulty("e")
            if (e.source == medium) controller.setDifficulty("m")
            if (e.source == hard) controller.setDifficulty("d")
        }
      }
    }
  }

  reactions += { case _: BoardChanged | _: PlayerOmitted => update }

  def update: Boolean = {
    tablePanel.redraw()
    mainFrame.pack
    mainFrame.menuBar = menus
    mainFrame.visible = true
    true
  }
}
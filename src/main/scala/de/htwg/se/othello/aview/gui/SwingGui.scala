package de.htwg.se.othello.aview.gui

import java.awt.event.KeyEvent

import de.htwg.se.othello.controller.controllerComponent.ControllerInterface
import de.htwg.se.othello.util.Observer
import javax.swing.KeyStroke

import scala.swing.event.Key.{Modifier, Modifiers}
import scala.swing.event.{ButtonClicked, Key}
import scala.swing.{Action, ButtonGroup, MainFrame, Menu, MenuBar, MenuItem, RadioMenuItem, Separator}

class SwingGui(controller: ControllerInterface) extends Observer {

  controller.add(this)

  val modifier: Modifiers = {
    if (System.getProperty("os.name").startsWith("Mac")) Modifier.Meta
    else Modifier.Control
  }

  lazy val tablePanel = new TablePanel(controller)

  lazy val mainFrame: MainFrame = new MainFrame {
    title = "Othello"
    menuBar = menus
    contents = tablePanel
    centerOnScreen
    resizable = false
    visible = true
  }

  def menus: MenuBar = new MenuBar {
    contents += new Menu("File") {
      mnemonic = Key.F
      contents += new MenuItem(new Action("New Game") {
        accelerator = Some(KeyStroke.getKeyStroke(KeyEvent.VK_N, modifier))
        override def apply: Unit = controller.newGame
      })
      contents += new MenuItem(new Action("Save") {
        accelerator = Some(KeyStroke.getKeyStroke(KeyEvent.VK_F, modifier))
        override def apply: Unit = controller.save()
      })
      contents += new MenuItem(new Action("Load") {
        accelerator = Some(KeyStroke.getKeyStroke(KeyEvent.VK_L, modifier))
        override def apply: Unit = controller.load()
      })
      contents += new Separator
      contents += new MenuItem(new Action("Quit") {
        accelerator = {
          if (System.getProperty("os.name").startsWith("Win"))
            Some(KeyStroke.getKeyStroke(KeyEvent.VK_F4, Modifier.Alt))
          else
            Some(KeyStroke.getKeyStroke(KeyEvent.VK_Q, modifier))
        }
        override def apply: Unit = sys.exit
      })
    }
    contents += new Menu("Edit") {
      mnemonic = Key.E
      contents += new MenuItem(new Action("Undo") {
        accelerator = Some(KeyStroke.getKeyStroke(KeyEvent.VK_Z, modifier))
        enabled = controller.canUndo
        override def apply: Unit = controller.undo()
      })
      contents += new MenuItem(new Action("Redo") {
        enabled = controller.canRedo
        accelerator = Some(KeyStroke.getKeyStroke(KeyEvent.VK_Z, modifier | Modifier.Shift))
        override def apply: Unit = controller.redo()
      })
    }
    contents += new Menu("Options") {
      mnemonic = Key.O
      contents += new MenuItem(new Action("Highlight possible moves") {
        enabled = controller.moves.nonEmpty
        accelerator = Some(KeyStroke.getKeyStroke(KeyEvent.VK_I, modifier))
        override def apply: Unit = controller.highlight()
      })
      contents += new MenuItem(new Action("Reduce board size") {
        enabled = controller.size > 4
        accelerator = Some(KeyStroke.getKeyStroke(45, modifier))
        override def apply: Unit = controller.resizeBoard("-")
      })
      contents += new MenuItem(new Action("Increase board size") {
        accelerator = Some(KeyStroke.getKeyStroke(KeyEvent.VK_PLUS, modifier))
        override def apply: Unit = controller.resizeBoard("+")
      })
      contents += new MenuItem(new Action("Reset board size") {
        enabled = controller.size != 8
        accelerator = Some(KeyStroke.getKeyStroke(46, modifier))
        override def apply: Unit = controller.resizeBoard(".")
      })
      contents += new Menu("Game mode") {
        val pvc: RadioMenuItem = new RadioMenuItem("Player vs. Computer") {
          selected = controller.playerCount == 1
        }
        val pvp: RadioMenuItem = new RadioMenuItem("Player vs. Player") {
          selected = controller.playerCount == 2
        }
        val cvc: RadioMenuItem = new RadioMenuItem("Demo mode (Computer vs Computer)") {
          selected = controller.playerCount == 0
        }
        contents ++= Seq(pvc, pvp, cvc)
        listenTo(pvc, pvp, cvc)
        reactions += {
          case e: ButtonClicked =>
            if (e.source == pvp) controller.setupPlayers("2")
            if (e.source == pvc) controller.setupPlayers("1")
            if (e.source == cvc) controller.setupPlayers("0")
        }
      }
      contents += new Menu("Game Difficulty") {
        val easy: RadioMenuItem = new RadioMenuItem("Easy")
        val medium: RadioMenuItem = new RadioMenuItem("Normal")
        val hard: RadioMenuItem = new RadioMenuItem("Hard")
        val buttonGroup = new ButtonGroup(easy, medium, hard)
        buttonGroup.buttons.foreach(b => {
          b.enabled = controller.size == 8
          b.selected = b.text.equals(controller.difficulty) && b.enabled
        })
        contents ++= buttonGroup.buttons
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

  def update: Boolean = {
    tablePanel.redraw()
    mainFrame.menuBar = menus
    mainFrame.pack
    true
  }
}

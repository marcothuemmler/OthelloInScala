package de.htwg.se.othello.aview.gui

import java.awt.{Color, GridLayout}

import de.htwg.se.othello.controller.controllerComponent.{ControllerInterface, GameStatus}
import javax.swing.ImageIcon

import scala.swing.event.MouseClicked
import scala.swing.{BorderPanel, BoxPanel, Dimension, FlowPanel, Font, Label, Orientation}

class OperationPanel(controller: ControllerInterface, Hoehe: Int) extends FlowPanel {
  val operationsides = 200
  background = Color.LIGHT_GRAY

  preferredSize = new Dimension(operationsides, Hoehe)

  def titel: BoxPanel = new BoxPanel(Orientation.Vertical) {
    preferredSize = new Dimension(operationsides, Hoehe / 4)
    background = Color.LIGHT_GRAY
    contents += new Label() {
      icon = new ImageIcon(getClass.getResource(s"resources/Othello.png"))
    }
  }

  def mode: BoxPanel = new BoxPanel(Orientation.Horizontal) {
    background = Color.LIGHT_GRAY
    preferredSize = new Dimension(operationsides, Hoehe / 4)
    if (controller.playerCount == 0) {
      contents += new Label() {
        icon = new ImageIcon(getClass.getResource("resources/botr.png"))
      }
      contents += new Label() {
        icon = new ImageIcon(getClass.getResource("resources/vsr.png"))
      }
      contents += new Label() {
        icon = new ImageIcon(getClass.getResource("resources/botr.png"))
      }
    } else if (controller.playerCount == 1) {
      contents += new Label() {
        icon = new ImageIcon(getClass.getResource("resources/playerr.png"))
      }
      contents += new Label() {
        icon = new ImageIcon(getClass.getResource("resources/vsr.png"))
      }
      contents += new Label() {
        icon = new ImageIcon(getClass.getResource("resources/botr.png"))
      }
    } else {
      contents += new Label() {
        icon = new ImageIcon(getClass.getResource("resources/playerr.png"))
      }
      contents += new Label() {
        icon = new ImageIcon(getClass.getResource("resources/vsr.png"))
      }
      contents += new Label() {
        icon = new ImageIcon(getClass.getResource("resources/playerr.png"))
      }
    }
  }

  def presentPlayer: BoxPanel = new BoxPanel(Orientation.Horizontal) {
    background = Color.LIGHT_GRAY
    preferredSize = new Dimension(operationsides, Hoehe / 4)
    if (controller.playerPresent == 1) contents += playerWhite
    else contents += playerBlack
    contents += infoBox
  }

  def infoBox: BoxPanel = new BoxPanel(Orientation.Horizontal) {
    background = Color.LIGHT_GRAY
    if (controller.gameOver) contents += NewGame
    else contents += new Label(" to play")
    if (!controller.gameOver && controller.gameStatus == GameStatus.OMITTED) {
      contents += new Label("No legal moves.")
    }
  }

  def NewGame: BoxPanel = new BoxPanel(Orientation.Horizontal) {
    background = Color.LIGHT_GRAY
    contents += new Label() {
      icon = new ImageIcon(getClass.getResource(s"resources/new.png"))
    }
    listenTo(mouse.clicks)
    reactions += {
      case _: MouseClicked => controller.newGame
    }
  }

  def scoreLabel: Int => Label = {
    case n@(1 | 2) =>
      new Label {
        icon = new ImageIcon(getClass.getResource(s"resources/$n.png"))
        text = s"${controller.count(n)}"
        foreground = new Color(10, 10, 10)
      }
  }

  def scorePanel: BoxPanel = new BoxPanel(Orientation.Horizontal) {
    peer.setLayout(new GridLayout)
    background = Color.lightGray
    if (!controller.gameOver) {
      contents ++= List(scoreLabel(1), scoreLabel(2))
    } else {
      contents += new Label {
        val fontSize: Int = if (controller.size > 4) 15 else 12
        text = controller.score
        font = new Font(font.getName, font.getStyle, fontSize)
      }
    }
  }

  def playerWhite: Label = new Label() {
    icon = new ImageIcon(getClass.getResource("resources/2.png"))
    foreground = new Color(200, 200, 200)
  }

  def playerBlack: Label = new Label() {
    icon = new ImageIcon(getClass.getResource("resources/1.png"))
    foreground = new Color(200, 200, 200)
  }

  def Undo: BoxPanel = new BoxPanel(Orientation.Horizontal) {
    background = Color.LIGHT_GRAY
    contents += new Label {
      icon = new ImageIcon(getClass.getResource("resources/undo.png"))
    }
    listenTo(mouse.clicks)
    reactions += {
      case _: MouseClicked if !controller.player.isBot => controller.undo()
    }
  }

  def Redo: BoxPanel = new BoxPanel(Orientation.Horizontal) {
    background = Color.LIGHT_GRAY
    contents += new Label {
      icon = new ImageIcon(getClass.getResource("resources/redo.png"))
    }
    listenTo(mouse.clicks)
    reactions += {
      case _: MouseClicked if !controller.player.isBot => controller.redo()
    }
  }

  def tip: BoxPanel = new BoxPanel(Orientation.Horizontal) {
    background = Color.LIGHT_GRAY
    contents += new Label {
      icon = new ImageIcon(getClass.getResource("resources/tip.png"))
    }
    listenTo(mouse.clicks)
    reactions += {
      case _: MouseClicked if !controller.player.isBot => controller.highlight()
    }
  }

  def Operation: BoxPanel = new BoxPanel(Orientation.Horizontal) {
    background = Color.LIGHT_GRAY
    contents += tip
    contents += Undo
    contents += Redo
    contents += NewGame
  }

  contents += new BoxPanel(Orientation.Vertical) {
    background = Color.LIGHT_GRAY
    preferredSize = new Dimension(operationsides, Hoehe)
    contents += new BorderPanel {
      background = Color.LIGHT_GRAY
      add(titel, BorderPanel.Position.Center)
      // add(mode, BorderPanel.Position.Center)
    }
    contents += mode
    contents += scorePanel
    contents += presentPlayer
    contents += Operation
  }
}

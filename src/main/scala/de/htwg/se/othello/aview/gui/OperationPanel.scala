package de.htwg.se.othello.aview.gui

import java.awt.Color

import de.htwg.se.othello.controller.controllerComponent.{ControllerInterface, GameStatus}
import javax.swing.ImageIcon

import scala.swing.event.MouseClicked
import scala.swing.{BorderPanel, BoxPanel, Dimension, FlowPanel, Font, Label, Orientation}

class OperationPanel(controller: ControllerInterface, height: Int) extends FlowPanel {
  val operationSides = 200
  background = Color.lightGray

  preferredSize = new Dimension(operationSides, height)

  def title: BoxPanel = new BoxPanel(Orientation.Vertical) {
    preferredSize = new Dimension(operationSides, height / 4)
    background = Color.lightGray
    contents += new Label {
      icon = new ImageIcon(getClass.getResource("resources/Othello.png"))
    }
  }

  def mode: BoxPanel = new BoxPanel(Orientation.Horizontal) {
    background = Color.lightGray
    preferredSize = new Dimension(operationSides, height / 4)
    if (controller.playerCount == 0) {
      contents += new Label {
        icon = new ImageIcon(getClass.getResource("resources/botr.png"))
      }
      contents += new Label {
        icon = new ImageIcon(getClass.getResource("resources/vsr.png"))
      }
      contents += new Label {
        icon = new ImageIcon(getClass.getResource("resources/botr.png"))
      }
    } else if (controller.playerCount == 1) {
      contents += new Label {
        icon = new ImageIcon(getClass.getResource("resources/playerr.png"))
      }
      contents += new Label {
        icon = new ImageIcon(getClass.getResource("resources/vsr.png"))
      }
      contents += new Label {
        icon = new ImageIcon(getClass.getResource("resources/botr.png"))
      }
    } else {
      contents += new Label {
        icon = new ImageIcon(getClass.getResource("resources/playerr.png"))
      }
      contents += new Label {
        icon = new ImageIcon(getClass.getResource("resources/vsr.png"))
      }
      contents += new Label {
        icon = new ImageIcon(getClass.getResource("resources/playerr.png"))
      }
    }
  }

  def presentPlayer: BoxPanel = new BoxPanel(Orientation.Horizontal) {
    background = Color.lightGray
    preferredSize = new Dimension(operationSides, height / 4)
    contents += (if (controller.player.value == 2) playerWhite else playerBlack)
    contents += infoBox
  }

  def infoBox: BoxPanel = new BoxPanel(Orientation.Horizontal) {
    background = Color.lightGray
    if (controller.gameOver) contents += NewGame
    else contents += new Label(" to play")
    if (!controller.gameOver && controller.gameStatus == GameStatus.OMITTED) {
      contents += new Label("No legal moves.")
    }
  }

  def NewGame: BoxPanel = new BoxPanel(Orientation.Horizontal) {
    background = Color.lightGray
    contents += new Label {
      icon = new ImageIcon(getClass.getResource("resources/new.png"))
    }
    listenTo(mouse.clicks)
    reactions += { case _: MouseClicked => controller.newGame }
  }

  def playerWhite: Label = new Label {
    icon = new ImageIcon(getClass.getResource("resources/2.png"))
  }

  def playerBlack: Label = new Label {
    icon = new ImageIcon(getClass.getResource("resources/1.png"))
  }

  def scorePanel: BoxPanel = new BoxPanel(Orientation.Horizontal) {
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

  def scoreLabel: Int => Label = {
    case n@(1 | 2) =>
      new Label(s"${controller.count(n)}") {
        icon = new ImageIcon(getClass.getResource(s"resources/$n.png"))
        foreground = new Color(10, 10, 10)
      }
  }

  def Operation: BoxPanel = new BoxPanel(Orientation.Horizontal) {
    background = Color.LIGHT_GRAY
    contents ++= List(tip, Undo, Redo, NewGame)
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

  contents += new BoxPanel(Orientation.Vertical) {
    background = Color.LIGHT_GRAY
    preferredSize = new Dimension(operationSides, height)
    contents += new BorderPanel {
      background = Color.LIGHT_GRAY
      add(title, BorderPanel.Position.Center)
    }
    contents ++= List(mode, scorePanel, presentPlayer, Operation)
  }
}

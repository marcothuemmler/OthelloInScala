package de.htwg.se.othello.aview.gui

import java.awt.RenderingHints.{KEY_ANTIALIASING, VALUE_ANTIALIAS_ON}
import java.awt.image.BufferedImage
import java.awt.{BasicStroke, Color, GridLayout}

import de.htwg.se.othello.controller.controllerComponent.GameStatus._
import de.htwg.se.othello.controller.controllerComponent.{ControllerInterface, GameStatus}
import javax.imageio.ImageIO.read

import scala.concurrent.{ExecutionContext, Future}
import scala.swing.Swing.{Icon, LineBorder}
import scala.swing.event.ButtonClicked
import scala.swing.{BorderPanel, BoxPanel, Button, Dimension, FlowPanel, Font, Graphics2D, GridPanel, Label, Orientation}

class TablePanel(controller: ControllerInterface) extends FlowPanel {

  val sides = 23
  val squares = 46
  val back: BufferedImage = read(getClass.getResource("/back.jpg"))
  val black: BufferedImage = read(getClass.getResource("/1.png"))
  val white: BufferedImage = read(getClass.getResource("/2.png"))

  def gameStatus: GameStatus = controller.gameStatus

  def edges: Int = controller.size * squares

  def rows: BoxPanel = new BoxPanel(Orientation.Vertical) {
    background = Color.lightGray
    preferredSize = new Dimension(sides, edges)
    contents ++= Seq(
      new Label {
        preferredSize = new Dimension(sides, sides)
      },
      new GridPanel(controller.size, 1) {
        opaque = false
        1 to rows map (i => contents += new Label(s"$i"))
      }
    )
  }

  def columns: GridPanel = new GridPanel(1, controller.size) {
    background = Color.lightGray
    preferredSize = new Dimension(edges, sides)
    0 until columns map (i => contents += new Label(s"${(i + 65).toChar}"))
  }

  def table: GridPanel = new GridPanel(controller.size, controller.size) {
    border = LineBorder(Color.black, 2)
    override def paintComponent(g: Graphics2D): Unit = {
      g.setRenderingHint(KEY_ANTIALIASING, VALUE_ANTIALIAS_ON)
      g.drawImage(back, 2, 2, edges, edges, null)
      g.setColor(new Color(30, 30, 30))
      g.fillOval(2 * squares - 3, 2 * squares - 3, 10, 10)
      g.fillOval(2 * squares - 3, edges - 2 * squares - 3, 10, 10)
      g.fillOval(edges - 2 * squares - 3, 2 * squares - 3, 10, 10)
      g.fillOval(edges - 2 * squares - 3, edges - 2 * squares - 3, 10, 10)
    }
    0 until columns map (col =>
      0 until rows map (row => contents += square(col, row)))
  }

  def square: (Int, Int) => Button = (row, col) => new Button {
    val circleSize = 21
    val shadow = new Color(10, 32, 10, 220)
    val borderColor = new Color(45, 130, 45, 220)
    val holeColor = new Color(10, 52, 10)
    val align = 13
    opaque = false
    contentAreaFilled = false
    border = LineBorder(new Color(30, 30, 30, 200))
    preferredSize = new Dimension(squares, squares)
    override def paintComponent(g: Graphics2D): Unit = {
      controller.valueOf(col, row) match {
        case 1 => g.drawImage(black, 1, 1, null)
        case 2 => g.drawImage(white, 1, 1, null)
        case -1 =>
          g.setStroke(new BasicStroke(2.8f))
          g.setColor(borderColor)
          g.drawOval(align, align + 1, circleSize - 1, circleSize - 1)
          g.setColor(shadow)
          g.drawOval(align - 1, align, circleSize - 1, circleSize - 1)
          g.setColor(holeColor)
          g.fillOval(align, align, circleSize, circleSize)
        case _ =>
      }
    }
    reactions += {
      case _: ButtonClicked if !controller.currentPlayer.isBot && !controller.gameOver =>
        Future(controller.set(col, row))(ExecutionContext.global)
      case _: ButtonClicked if controller.gameOver => controller.newGame
    }
  }

  def scoreLabel: Int => Label = {
    case n@(1 | 2) => new Label(s"${controller.count(n)}") {
      icon = Icon(getClass.getResource(s"/$n.png"))
      foreground = new Color(200, 200, 200)
    }
  }

  def messagePanel: BoxPanel = new BoxPanel(Orientation.Horizontal) {
    peer.setLayout(new GridLayout)
    preferredSize = new Dimension(edges, squares)
    background = if (gameStatus == ILLEGAL) Color.red.darker else Color.darkGray
    if (gameStatus == IDLE) contents ++= Seq(scoreLabel(1), scoreLabel(2))
    else {
      contents += new Label {
        val fontSize: Int = if (controller.size > 4) 26 else 20
        font = Font(name, Font.Style.Plain, fontSize)
        foreground = new Color(200, 200, 200)
        text = gameStatus match {
          case GAME_OVER => controller.score
          case OMITTED =>
            GameStatus.message(gameStatus) + " for " + controller.nextPlayer
          case _ => GameStatus.message(gameStatus)
        }
      }
    }
  }

  def redraw(): Unit = {
    contents.clear
    contents += new BoxPanel(Orientation.Vertical) {
      contents += messagePanel
      contents += new BorderPanel {
        add(rows, BorderPanel.Position.West)
        add(new BoxPanel(Orientation.Vertical) {
          contents ++= List(columns, table)
        }, BorderPanel.Position.East)
      }
    }
  }
}

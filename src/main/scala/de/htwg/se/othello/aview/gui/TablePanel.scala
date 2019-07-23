package de.htwg.se.othello.aview.gui

import java.awt.RenderingHints.{KEY_ANTIALIASING, VALUE_ANTIALIAS_ON}
import java.awt.image.BufferedImage
import java.awt.{Color, GridLayout}

import de.htwg.se.othello.controller.controllerComponent.ControllerInterface
import javax.imageio.ImageIO.read

import scala.concurrent.{ExecutionContext, Future}
import scala.swing.Swing.{Icon, LineBorder}
import scala.swing.event.ButtonClicked
import scala.swing.{BorderPanel, BoxPanel, Button, Dimension, FlowPanel, Font, Graphics2D, GridPanel, Label, Orientation}

class TablePanel(controller: ControllerInterface) extends FlowPanel {

  val sides = 23
  val squares = 46
  val back: BufferedImage = read(getClass.getResource("resources/back.jpg"))
  val black: BufferedImage = read(getClass.getResource("resources/1.png"))
  val white: BufferedImage = read(getClass.getResource("resources/2.png"))

  def edges: Int = controller.size * squares

  def rows: BoxPanel = new BoxPanel(Orientation.Vertical) {
    background = Color.lightGray
    preferredSize = new Dimension(sides, edges)
    contents ++= List(
      new Label { preferredSize = new Dimension(sides, sides) },
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
    opaque = false
    contentAreaFilled = false
    border = LineBorder(new Color(30, 30, 30, 200))
    preferredSize = new Dimension(squares, squares)
    override def paintComponent(g: Graphics2D): Unit = {
      g.setRenderingHint(KEY_ANTIALIASING, VALUE_ANTIALIAS_ON)
      g.setColor(new Color(0, 0, 0, 150))
      controller.valueOf(col, row) match {
        case -1 => g.fillOval(13, 13, 20, 20)
        case 1 => g.drawImage(black, 1, 1, null)
        case 2 => g.drawImage(white, 1, 1, null)
        case _ =>
      }
    }
    reactions += {
      case _: ButtonClicked if !controller.player.isBot && !controller.gameOver =>
        if (controller.moves.values.flatten.toSet.contains((col, row)))
          Future(controller.set(col, row))(ExecutionContext.global)
        else controller.highlight()
      case _: ButtonClicked if controller.gameOver => controller.newGame
    }
  }

  def scoreLabel: Int => Label = {
    case n@(1 | 2) => new Label(s"${controller.count(n)}") {
      icon = Icon(getClass.getResource(s"resources/$n.png"))
      foreground = new Color(200, 200, 200)
    }
  }

  def scorePanel: BoxPanel = new BoxPanel(Orientation.Horizontal) {
    peer.setLayout(new GridLayout)
    preferredSize = new Dimension(edges, squares)
    background = Color.darkGray
    if (!controller.gameOver) contents ++= List(scoreLabel(1), scoreLabel(2))
    else {
      contents += new Label(controller.score) {
        val fontSize: Int = if (controller.size > 4) 26 else 20
        font = Font(name, Font.Style.Plain, fontSize)
        foreground = new Color(200, 200, 200)
      }
    }
  }

  def redraw(): Unit = {
    contents.clear
    contents += new BoxPanel(Orientation.Horizontal) {
    // contents += new BoxPanel(Orientation.Vertical) {
      contents += new OperationPanel(controller, sides + edges)
      // contents += scorePanel
      contents += new BorderPanel {
        add(rows, BorderPanel.Position.West)
        add(new BoxPanel(Orientation.Vertical) {
          contents ++= List(columns, table)
        }, BorderPanel.Position.East)
      }
    }
    revalidate
  }
}

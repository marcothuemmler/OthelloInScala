package de.htwg.se.othello.aview.gui

import java.awt.{Color, GridLayout, RenderingHints}

import de.htwg.se.othello.controller.controllerComponent.ControllerInterface
import javax.imageio.ImageIO

import scala.concurrent.{ExecutionContext, Future}
import scala.swing.Swing.{Icon, LineBorder}
import scala.swing.event.ButtonClicked
import scala.swing.{BorderPanel, BoxPanel, Button, Dimension, FlowPanel, Font, Graphics2D, GridPanel, Label, Orientation}

class TablePanel(controller: ControllerInterface) extends FlowPanel {

  val sides = 24
  val sidesColor: Color = Color.lightGray
  val squares = 48

  def tableSize: Int = controller.size

  def edges: Int = tableSize * squares

  def rows: BoxPanel = new BoxPanel(Orientation.Vertical) {
    background = sidesColor
    preferredSize = new Dimension(sides, edges)
    contents ++= List(
      new Label { preferredSize = new Dimension(sides, sides) },
      new GridPanel(tableSize, 1) {
        background = sidesColor
        for { i <- 1 to rows } contents += new Label(s"$i")
      }
    )
  }

  def columns: GridPanel = new GridPanel(1, tableSize) {
    background = sidesColor
    preferredSize = new Dimension(edges, sides)
    for { i <- 0 until columns } contents += new Label(s"${(i + 65).toChar}")
  }

  def table: GridPanel = new GridPanel(tableSize, tableSize) {
    border = LineBorder(Color.black, 2)
    override def paintComponent(g: Graphics2D): Unit = {
      g.setRenderingHint(
        RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON
      )
      g.drawImage(ImageIO.read(getClass.getResource("resources/back.jpg"))
        , 2, 2, edges, edges, null)
      g.setColor(new Color(30, 30, 30))
      g.fillOval(2 * squares - 3, 2 * squares - 3, 10, 10)
      g.fillOval(2 * squares - 3, edges - 2 * squares - 3, 10, 10)
      g.fillOval(edges - 2 * squares - 3, 2 * squares - 3, 10, 10)
      g.fillOval(edges - 2 * squares - 3, edges - 2 * squares - 3, 10, 10)
    }
    for {
      col <- 0 until columns
      row <- 0 until rows
    } contents += square(col, row)
  }

  def square(row: Int, col: Int): Button = new Button {
    opaque = false
    contentAreaFilled = false
    border = LineBorder(new Color(30, 30, 30, 200))
    preferredSize = new Dimension(squares, squares)
    override def paintComponent(g: Graphics2D): Unit = {
      g.setRenderingHint(
        RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON
      )
      controller.valueOf(col, row) match {
        case -1 =>
          g.setColor(new Color(0, 0, 0, 150))
          g.fillOval(14, 14, 21, 21)
        case n @ (1 | 2) =>
          g.drawImage(ImageIO.read(
            getClass.getResource(s"resources/$n.png")), 2, 2, null)
        case _ =>
      }
    }
    reactions += {
      case _: ButtonClicked if !controller.player.isBot && !controller.gameOver =>
        if (controller.options.contains((col, row)))
          Future(controller.set(col, row))(ExecutionContext.global)
        else controller.highlight()
      case _: ButtonClicked if controller.gameOver => controller.newGame
    }
  }

  def scoreLabel: Int => Label = {
    case n @ (1 | 2) => new Label(s"${controller.count(n)}") {
      icon = Icon(getClass.getResource(s"resources/$n.png"))
      foreground = new Color(200, 200, 200)
    }
  }

  def scorePanel: BoxPanel = new BoxPanel(Orientation.Horizontal) {
    peer.setLayout(new GridLayout)
    preferredSize = new Dimension(edges, squares)
    background = Color.darkGray
    if (!controller.gameOver) {
      contents ++= List(scoreLabel(1), scoreLabel(2))
    } else {
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
      contents += scorePanel
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

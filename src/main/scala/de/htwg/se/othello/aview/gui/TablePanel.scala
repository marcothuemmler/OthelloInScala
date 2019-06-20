package de.htwg.se.othello.aview.gui

import java.awt.{Color, GridLayout, RenderingHints}

import de.htwg.se.othello.controller.controllerComponent.ControllerInterface
import javax.imageio.ImageIO
import javax.swing.ImageIcon
import javax.swing.border.LineBorder

import scala.concurrent.{ExecutionContext, Future}
import scala.swing.event.MouseClicked
import scala.swing.{BorderPanel, BoxPanel, Dimension, FlowPanel, Font, Graphics2D, GridPanel, Label, Orientation}

class TablePanel(controller: ControllerInterface) extends FlowPanel {

  val sides = 26
  val sidesColor: Color = Color.lightGray
  val squareSize = 52

  def tableSize: Int = controller.size

  def edgeLength: Int = tableSize * squareSize

  def rows: BoxPanel = new BoxPanel(Orientation.Vertical) {
    background = sidesColor
    preferredSize = new Dimension(sides, edgeLength)
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
    preferredSize = new Dimension(edgeLength, sides)
    for { i <- 0 until columns } contents += new Label(s"${(i + 65).toChar}")
  }

  def table: GridPanel = new GridPanel(tableSize, tableSize) {
    border = new LineBorder(Color.black, 2)
    override def paintComponent(g: Graphics2D): Unit = {
      g.setRenderingHint(
        RenderingHints.KEY_ANTIALIASING,
        RenderingHints.VALUE_ANTIALIAS_ON
      )
      g.drawImage({
        ImageIO.read(getClass.getResourceAsStream("resources/back.jpg"))
      }, 2, 2, edgeLength, edgeLength, null)
      g.setColor(new Color(0x20, 0x20, 0x20))
      g.fillOval(2 * squareSize - 5, 2 * squareSize - 5, 14, 14)
      g.fillOval(2 * squareSize - 5, edgeLength - 2 * squareSize - 5, 14, 14)
      g.fillOval(edgeLength - 2 * squareSize - 5, 2 * squareSize - 5, 14, 14)
      g.fillOval(edgeLength - 2 * squareSize - 5, edgeLength - 2 * squareSize - 5, 14, 14)
    }
    for {
      col <- 0 until columns
      row <- 0 until rows
    } contents += square(col, row)
  }

  def square(row: Int, col: Int): Label = new Label {
    border = new LineBorder(new Color(0x20, 0x20, 0x20, 200))
    preferredSize = new Dimension(squareSize, squareSize)
    override def paintComponent(g: Graphics2D): Unit = {
      g.setRenderingHint(
        RenderingHints.KEY_ANTIALIASING,
        RenderingHints.VALUE_ANTIALIAS_ON
      )
      controller.valueOf(col, row) match {
        case -1 =>
          g.setColor(new Color(0, 0, 0, 135))
          g.fillOval(16, 16, 21, 21)
        case n @ (1 | 2) =>
          g.drawImage({
            ImageIO.read(getClass.getResource(f"resources/$n.png"))
          }, 4, 4, null)
        case _ =>
      }
    }
    listenTo(mouse.clicks)
    reactions += {
      case _: MouseClicked if controller.isReady =>
        if (controller.options.contains((col, row))) {
          Future(controller.set(col, row))(ExecutionContext.global)
        } else if (controller.gameOver) controller.newGame
        else controller.highlight()
    }
  }

  def scoreLabel: Int => Label = {
    case n @ (1 | 2) =>
      new Label {
        icon = new ImageIcon(getClass.getResource(s"resources/$n.png"))
        text = s"${controller.count(n)}"
        foreground = new Color(200, 200, 200)
      }
  }

  def scorePanel: BoxPanel = new BoxPanel(Orientation.Horizontal) {
    peer.setLayout(new GridLayout)
    preferredSize = new Dimension(edgeLength, squareSize)
    background = Color.darkGray
    if (!controller.gameOver) {
      contents ++= List(scoreLabel(1), scoreLabel(2))
    } else {
      contents += new Label {
        val fontSize: Int = if (controller.size > 4) 26 else 20
        text = controller.score
        font = new Font(font.getName, font.getStyle, fontSize)
        foreground = new Color(200, 200, 200)
      }
    }
  }

  def redraw(): Unit = {
    contents.clear
    contents += new BoxPanel(Orientation.Vertical) {
      contents += scorePanel
      contents += new BorderPanel {
        add(rows, BorderPanel.Position.West)
        add(new BoxPanel(Orientation.Vertical) {
          contents += columns
          contents += table
        }, BorderPanel.Position.East)
      }
    }
    revalidate
  }
}

package de.htwg.se.othello.aview.gui

import java.awt.Color

import de.htwg.se.othello.controller.Controller
import javax.swing.ImageIcon
import javax.swing.border.LineBorder

import scala.concurrent.{ExecutionContext, Future}
import scala.swing.event.MouseClicked
import scala.swing.{BorderPanel, BoxPanel, Dimension, FlowPanel, Font, GridPanel, Label, Orientation}

class TablePanel(controller: Controller) extends FlowPanel {

  val sides = 32
  val sidesColor: Color = Color.lightGray
  val squareSize = 52

  def tableSize: Int = controller.board.size

  def edgeLength: Int = tableSize * squareSize

  def rows: BoxPanel = new BoxPanel(Orientation.Vertical) {
    background = sidesColor
    preferredSize = new Dimension(sides, edgeLength)
    contents += new Label {
      preferredSize = new Dimension(sides, sides)
    }
    contents += new GridPanel(tableSize, 1) {
      background = sidesColor
      for { i <- 1 to rows } contents += new Label(s"$i")
    }
  }

  def columns: GridPanel = new GridPanel(1, tableSize) {
    background = sidesColor
    preferredSize = new Dimension(edgeLength, sides)
    for { i <- 0 until columns } contents += new Label(s"${(i + 65).toChar}")
  }

  def table: GridPanel = new GridPanel(tableSize, tableSize) {
    background = new Color(10, 95, 10)
    for {
      col <- 0 until columns
      row <- 0 until rows
    } contents += square(col, row)
  }

  def square(row: Int, col: Int): Label = new Label {
    border = new LineBorder(new Color(30, 30, 30, 140))
    preferredSize = new Dimension(squareSize, squareSize)
    controller.board.valueOf(col, row) match {
      case -1 => icon = new ImageIcon("resources/dot.png")
      case 1 => icon = new ImageIcon("resources/black.png")
      case 2 => icon = new ImageIcon("resources/white.png")
      case _ =>
    }
    listenTo(mouse.clicks)
    reactions += {
      case _: MouseClicked =>
        if (controller.options.contains((col, row))) {
          Future(controller.set(col, row))(ExecutionContext.global)
        } else if (controller.board.gameOver) controller.newGame()
        else controller.highlight()
    }
  }

  def scoreLabel(value: Int): Label = new Label {
    text = s"${controller.board.count(value)}"
    foreground = new Color(200, 200, 200)
    value match {
      case 1 => icon = new ImageIcon("resources/black.png")
      case 2 => icon = new ImageIcon("resources/white.png")
      case _ =>
    }
  }

  def scorePanel: GridPanel = {
    if (!controller.board.gameOver) new GridPanel(1, 2) {
      contents += scoreLabel(1)
      contents += scoreLabel(2)
      background = Color.darkGray
      preferredSize = new Dimension(edgeLength, squareSize)
    }
    else new GridPanel(1, 1) {
      contents += new Label {
        text = controller.board.score
        font = new Font(font.getName, font.getStyle, 28)
        foreground = new Color(200, 200, 200)
      }
      background = Color.darkGray
      preferredSize = new Dimension(edgeLength, squareSize)
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
    repaint
  }
}

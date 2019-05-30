package de.htwg.se.othello.aview.gui

import java.awt.Color

import de.htwg.se.othello.controller.Controller
import javax.swing.ImageIcon
import javax.swing.border.LineBorder

import scala.swing.event.MouseClicked
import scala.swing.{BorderPanel, BoxPanel, Dimension, FlowPanel, GridPanel, Label, Orientation}

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
    background = new Color(10, 90, 10)
    for {
      col <- 0 until columns
      row <- 0 until rows
    } contents += square(col, row)
  }

  def square(row: Int, col: Int): Label = new Label {
    border = new LineBorder(new Color(30, 30, 30, 140), 1)
    preferredSize = new Dimension(squareSize, squareSize)
    icon = controller.board.valueOf(col, row) match {
      case -1 => new ImageIcon("resources/big_dot.png")
      case 0  => new ImageIcon("resources/empty.png")
      case 1  => new ImageIcon("resources/black_shadow.png")
      case 2  => new ImageIcon("resources/white_shadow.png")
    }
    listenTo(mouse.clicks)
    reactions += {
      case _: MouseClicked =>
        if (controller.options.contains((col, row))) controller.set(col, row)
        else if (controller.board.gameOver) controller.newGame()
        else controller.highlight()
    }
  }

  def redraw(): Unit = {
    contents.clear
    contents += new BorderPanel {
      add(rows, BorderPanel.Position.West)
      add(new BoxPanel(Orientation.Vertical) {
        contents += columns
        contents += table
      }, BorderPanel.Position.East)
    }
    repaint
  }
}

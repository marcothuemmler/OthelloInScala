package de.htwg.se.othello.aview.gui

import java.awt.Color

import de.htwg.se.othello.controller.Controller
import javax.swing.ImageIcon
import javax.swing.border.LineBorder

import scala.swing.event.MouseClicked
import scala.swing.{BorderPanel, BoxPanel, Dimension, FlowPanel, GridPanel, Label, Orientation}

class TablePanel(controller: Controller) extends FlowPanel {

  val borderWidth = 36
  val borderColor: Color = Color.lightGray

  def redraw(): Unit = {
    contents.clear
    contents += new BorderPanel {
      add(sidePanel, BorderPanel.Position.West)
      add(new BoxPanel(Orientation.Vertical) {
        contents += columns
        contents += table
      }, BorderPanel.Position.East)
    }
    revalidate
    repaint
  }

  def columns: GridPanel = new GridPanel(1, controller.board.size) {
    background = borderColor
    preferredSize = new Dimension(controller.board.size * 50, borderWidth)
    for {
      i <- controller.board.grid.indices
    } contents += new Label((i + 65).toChar.toString)
  }

  def sidePanel: BoxPanel = new BoxPanel(Orientation.Vertical) {
    background = borderColor
    preferredSize = new Dimension(borderWidth, controller.board.size * 50)
    contents += new Label {
      text = ""
      preferredSize = new Dimension(borderWidth, borderWidth)
    }
    contents += rows
  }

  def rows: GridPanel = new GridPanel(controller.board.size, 1) {
    background = borderColor
    for {
      i <- 0 until rows
    } contents += new Label(s"${i + 1}")
  }

  def table: GridPanel = new GridPanel(controller.board.size, controller.board.size) {
    for {
      col <- controller.board.grid.indices
      row <- controller.board.grid.indices
    } contents += labels(col, row)
    background = new Color(10, 91, 10)
    listenTo(mouse.clicks)
    reactions += {
      case e: MouseClicked =>
        if (controller.board.gameOver) controller.newGame()
        else {
          val column = e.point.x / (size.height / rows)
          val row = e.point.y / (size.width / columns)
          if (!controller.board.isSet(column, row)) controller.set(column, row)
        }
    }
  }

  def labels(row: Int, column: Int): Label = {
    controller.board.valueOf(column, row) match {
      case 1 => new Label {
        icon = new ImageIcon("resources/black_leather_shadow.png")
        border = new LineBorder(new Color(30, 30, 30, 100), 1)
        preferredSize = new Dimension(50, 50)
      }
      case 2 => new Label {
        icon = new ImageIcon("resources/white_leather_shadow.png")
        border = new LineBorder(new Color(30, 30, 30, 100), 1)
        preferredSize = new Dimension(50, 50)
      }
      case -1 => new Label {
        icon = new ImageIcon("resources/black_dot.png")
        border = new LineBorder(new Color(30, 30, 30, 100), 1)
        preferredSize = new Dimension(50, 50)
      }
      case 0 => new Label {
        text = ""
        border = new LineBorder(new Color(30, 30, 30, 100), 1)
        preferredSize = new Dimension(50, 50)
      }
    }
  }
}

package de.htwg.se.othello.aview.gui

import java.awt.Color

import de.htwg.se.othello.controller.Controller
import javax.swing.ImageIcon
import javax.swing.border.LineBorder

import scala.swing.event.MouseClicked
import scala.swing.{Dimension, FlowPanel, GridPanel, Label}

class TablePanel(controller: Controller) extends FlowPanel {

  def table: GridPanel = new GridPanel(controller.board.size,controller.board.size) {
    for {
      col <- controller.board.grid.indices
      row <- controller.board.grid.indices
    } contents += labels(col, row)
    background = new Color(10, 91, 10)
    preferredSize = new Dimension(400, 400)
    listenTo(mouse.clicks)
    reactions += {
      case e: MouseClicked =>
        controller.set(e.point.x / (size.height / rows), e.point.y/ (size.width / columns))
    }
  }

  def labels(row: Int, column: Int): Label = {
    controller.board.valueOf(column, row) match {
      case 1 => new Label {
        icon = new ImageIcon("resources/black_leather_shadow.png")
        border = new LineBorder(new Color(30,30,30) ,1)
      }
      case 2 => new Label {
        icon = new ImageIcon("resources/white_leather_shadow.png")
        border = new LineBorder(new Color(30,30,30) ,1)
      }
      case -1 => new Label {
        icon = new ImageIcon("resources/black_dot.png")
        border = new LineBorder(new Color(30,30,30), 1)
      }
      case 0 => new Label {
        text = ""
        border = new LineBorder(new Color(30, 30, 30), 1)
      }
    }
  }

  contents += table

  def update(): Unit = {
    contents.clear
    contents += table
    table.repaint()
    revalidate
    repaint
  }
}

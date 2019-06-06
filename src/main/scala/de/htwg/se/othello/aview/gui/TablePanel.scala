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
  val operationsides = 150

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
  def UndoPanel: BoxPanel = new BoxPanel(Orientation.Horizontal){
    background = Color.darkGray
    contents += new Label() {
      icon = new ImageIcon("resources/undo.png")
     // text = s"undo"
      foreground = new Color(250, 250, 200)
    }
    listenTo(mouse.clicks)
    reactions += {
      case _: MouseClicked =>
      controller.undo()
    }

  }

  def RedoPanel: BoxPanel = new BoxPanel(Orientation.Horizontal){
    background = Color.darkGray
    contents += new Label() {
      icon = new ImageIcon("resources/redo.png")
    //  text = s"redo"
      foreground = new Color(250, 250, 200)
    }
    listenTo(mouse.clicks)
    reactions += {
      case _: MouseClicked =>
        controller.redo()
    }
  }
  def TippsPanel: BoxPanel = new BoxPanel(Orientation.Horizontal){
    background = Color.darkGray
    contents += new Label() {
      icon = new ImageIcon("resources/tipps.png")
      text = s"Hightlights"
      foreground = new Color(250, 250, 200)
    }
    listenTo(mouse.clicks)
    reactions += {
      case _: MouseClicked =>
        controller.highlight()
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
    border = new LineBorder(new Color(30, 30, 30, 140))
    preferredSize = new Dimension(squareSize, squareSize)
    controller.board.valueOf(col, row) match {
      case -1 => icon = new ImageIcon("resources/big_dot.png")
      case 1 => icon = new ImageIcon("resources/black_shadow.png")
      case 2 => icon = new ImageIcon("resources/white_shadow.png")
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

  def scorePanel: GridPanel = {
    if (!controller.board.gameOver) new GridPanel(1, 2) {
      contents += new Label {
        icon = new ImageIcon("resources/black_shadow.png")
        text = s"${controller.board.count(1)}"
        foreground = new Color(200, 200, 200)
      }
      contents += new Label {
        icon = new ImageIcon("resources/white_shadow.png")
        text = s"${controller.board.count(2)}"
        foreground = new Color(200, 200, 200)
      }
      contents += new BoxPanel(Orientation.Vertical){
        background = Color.darkGray
        contents += UndoPanel
        contents += RedoPanel
        //contents += TippsPanel
      }
      background = Color.darkGray
      preferredSize = new Dimension(edgeLength, 60)
    }
    else new GridPanel(1, 1) {
      contents += new Label {
        text = controller.board.score
        val test: Font = font
        font = new Font(test.getName,0, 28)
        foreground = new Color(200, 200, 200)
      }
      contents += new Label {
        text = "New Game!"
        val test: Font = font
        font = new Font(test.getName,0, 28)
        foreground = new Color(200, 200, 200)
        listenTo(mouse.clicks)
        reactions += {
          case _: MouseClicked =>
            controller.newGame()
        }

      }

      background = Color.darkGray
      preferredSize = new Dimension(edgeLength, 60)
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

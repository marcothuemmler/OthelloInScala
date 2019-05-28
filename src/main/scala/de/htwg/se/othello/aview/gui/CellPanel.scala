package de.htwg.se.othello.aview.gui

import de.htwg.se.othello.controller._
import javax.swing.ImageIcon

import scala.swing._
import scala.swing.event._



class CellPanel(row: Int, column: Int, controller: Controller) extends FlowPanel  {
  val white = new Label {icon = new ImageIcon("resources/white.png")}
  val black = new Label { icon = new ImageIcon("resources/black.png") }
  val empty = new Label("")
  val highlightBlack = new Label { icon = new ImageIcon("resources/highlight.png") }
  val highlightWhite = new Label { icon = new ImageIcon("resources/highlight-white.png") }

  val label: Label = new Label {
    text = cellText(row, column)
    font = new Font("Verdana", 1, 36)
  }
  val cell: BoxPanel = new BoxPanel(Orientation.Vertical) {
    contents += label
    preferredSize = new Dimension(65, 65)
    border = Swing.BeveledBorder(Swing.Lowered)
    listenTo(mouse.clicks)
    listenTo(mouse.moves)
   // listenTo(controller)
    reactions += {
      case MouseClicked(src, pt, mod, clicks, pops) =>
        controller.set(row, column)
    }

    def rendererComponent(isSelected: Boolean, focused: Boolean, row: Int, column: Int): Component = {
      controller.board.valueOf(column, row) match {
        case 1 => black
        case 2 => white
        case 0 => empty
        case -1 => if (controller.player.value == 1) highlightBlack else highlightWhite
      }
    }

  }
  def redraw(): Unit = {
    contents.clear()
    label.text = cellText(row, column)
    contents += cell
    repaint
  }

  def cellText(row: Int, col: Int): String = ""





}

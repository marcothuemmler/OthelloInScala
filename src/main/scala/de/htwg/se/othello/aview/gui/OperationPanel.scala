package de.htwg.se.othello.aview.gui

import java.awt.{Color, GridLayout, RenderingHints}

import de.htwg.se.othello.controller.controllerComponent.{ControllerInterface, GameStatus}
import de.htwg.se.othello.model.Player
import javax.swing.ImageIcon
import javax.swing.border.LineBorder
import javax.imageio.ImageIO

import scala.concurrent.{ExecutionContext, Future}
import scala.swing.event.MouseClicked
import scala.swing.{BorderPanel, BoxPanel, Dimension, FlowPanel, Font, Graphics2D, GridPanel, Label, Orientation}

class OperationPanel(controller: ControllerInterface, Hoehe: Int) extends  FlowPanel{
  val operationsides = 200
  background = Color.LIGHT_GRAY
  preferredSize = new Dimension(operationsides, Hoehe)

  def titel: BoxPanel = new BoxPanel(Orientation.Vertical){
    background = Color.LIGHT_GRAY
    contents += new Label(){
      icon = new ImageIcon(getClass.getResource(s"resources/titel.png"))

    }
    contents += new Label(){
     // text = "Reversi"
    }
  }
  def mode: BoxPanel = new BoxPanel(Orientation.Horizontal){
    background = Color.LIGHT_GRAY
    preferredSize = new Dimension(operationsides, Hoehe/5)
    if (controller.playerCount == 0){
      contents += new Label(){
        icon = new ImageIcon(getClass.getResource(s"resources/botr.png"))
      }
      contents += new Label(){
        icon = new ImageIcon(getClass.getResource(s"resources/vsr.png"))
      }
      contents += new Label(){
        icon = new ImageIcon(getClass.getResource(s"resources/botr.png"))
      }
    } else if (controller.playerCount == 1){
      contents += new Label(){
        icon = new ImageIcon(getClass.getResource(s"resources/playerr.png"))
      }
      contents += new Label(){
        icon = new ImageIcon(getClass.getResource(s"resources/vsr.png"))
      }
      contents += new Label(){
        icon = new ImageIcon(getClass.getResource(s"resources/botr.png"))
      }
    } else {
      contents += new Label(){
        icon = new ImageIcon(getClass.getResource(s"resources/playerr.png"))
      }
      contents += new Label(){
        icon = new ImageIcon(getClass.getResource(s"resources/vsr.png"))
      }
      contents += new Label(){
        icon = new ImageIcon(getClass.getResource(s"resources/playerr.png"))
      }
    }
  }

  def presentPlayer: BoxPanel =  new BoxPanel(Orientation.Horizontal){
    background = Color.LIGHT_GRAY
    if(controller.playerPresent == 1) {
      contents += playerWhite
    } else {
      contents += playerBlack
    }
    contents += infoBox
  }

  def infoBox: BoxPanel = new BoxPanel(Orientation.Horizontal){
    background = Color.LIGHT_GRAY
    if(controller.gameOver){
      contents += NewGame
    } else {
      contents += new Label(){
        text = " to play"
      }

    }
    if(!controller.gameOver && controller.gameStatus == GameStatus. OMITTED){
      contents += new Label(){
        text = "No legal moves."
      }
    }
  }

  def NewGame: BoxPanel = new BoxPanel(Orientation.Horizontal){
    background = Color.LIGHT_GRAY
    contents += new Label(){
      icon = new ImageIcon(getClass.getResource(s"resources/new.png"))
    }
    listenTo(mouse.clicks)
    reactions += {
      case _: MouseClicked if controller.isReady =>
        controller.newGame
    }
  }


  def playerWhite: Label = new Label()  {
    icon = new ImageIcon(getClass.getResource(s"resources/2.png"))
    foreground = new Color(200, 200, 200)
  }
  def playerBlack: Label = new Label()  {
    icon = new ImageIcon(getClass.getResource(s"resources/1.png"))
    foreground = new Color(200, 200, 200)
  }

  def Undo: BoxPanel = new BoxPanel(Orientation.Horizontal) {
    background = Color.LIGHT_GRAY
    contents += new Label{
      icon = new ImageIcon(getClass.getResource(s"resources/undo.png"))
      }
    listenTo(mouse.clicks)
    reactions += {
      case _: MouseClicked if controller.isReady =>
        controller.undo()
    }

  }

  def Redo: BoxPanel = new BoxPanel(Orientation.Horizontal) {
    background = Color.LIGHT_GRAY
    contents += new Label{
      icon = new ImageIcon(getClass.getResource(s"resources/redo.png"))
    }
    listenTo(mouse.clicks)
    reactions += {
      case _: MouseClicked if controller.isReady =>
        controller.redo()
    }
  }

  def Operation: BoxPanel = new BoxPanel(Orientation.Horizontal){
    background = Color.LIGHT_GRAY
    contents += Undo
    contents += Redo
    contents += NewGame
  }


  contents += new BoxPanel(Orientation.Vertical){
    background = Color.LIGHT_GRAY
    preferredSize = new Dimension(operationsides, Hoehe)
    contents += titel
    contents += mode
    contents += presentPlayer
    contents += Operation
  }

  /**
  contents += new BorderPanel {
    background = Color.LIGHT_GRAY
    preferredSize = new Dimension(operationsides, Hoehe)
    add(titel, BorderPanel.Position.North)
    add(presentPlayer, BorderPanel.Position.Center)
    add(Operation, BorderPanel.Position.South)
  }
  **/





}

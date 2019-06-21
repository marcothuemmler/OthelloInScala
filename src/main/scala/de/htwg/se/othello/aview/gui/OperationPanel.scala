package de.htwg.se.othello.aview.gui

import java.awt.{Color, Dimension}

import de.htwg.se.othello.controller.controllerComponent.ControllerInterface

import scala.swing.FlowPanel

class OperationPanel(controller: ControllerInterface, Hoehe: Int) extends  FlowPanel{
  val operationsides = 200
  background = Color.LIGHT_GRAY
  preferredSize = new Dimension(operationsides, Hoehe)







}

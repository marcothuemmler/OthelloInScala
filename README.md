# OthelloInScala [![Build Status](https://travis-ci.org/marcothuemmler/de.htwg.se.OthelloInScala.svg?branch=master)](https://travis-ci.org/marcothuemmler/de.htwg.se.OthelloInScala)[![Coverage Status](https://coveralls.io/repos/github/marcothuemmler/de.htwg.se.OthelloInScala/badge.svg)](https://coveralls.io/github/marcothuemmler/de.htwg.se.OthelloInScala)![GitHub top language](https://img.shields.io/github/languages/top/marcothuemmler/OthelloInScala.svg?color=RED)![GitHub](https://img.shields.io/github/license/marcothuemmler/OthelloInScala.svg)

Othello game for Software Engineering lecture at University of applied science Constance



<p align="center"><img src="resources/screenshot.png?raw=true" width="210"/> </p>


**Object of the Game**

The goal is to get the majority of color disks on the board at the end of the game.


**Game Play**

Othello is a strategy board game played between 2 players. One player plays with black disks and the other twith white disks.

Each player gets 32 disks and black always starts the game.

The game then alternates between white and black until:

* one player can not make a valid move to outflank the opponent.
* both players have no valid moves.

When a player has no valid moves, he forfeits his turn and the opponent continues.

A player can not voluntarily forfeit his turn.

When both players can not make a valid move the game ends. 

The player with the most disks of his color facing up wins.

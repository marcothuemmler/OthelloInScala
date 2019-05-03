# OthelloInScala [![Build Status](https://travis-ci.org/marcothuemmler/de.htwg.se.OthelloInScala.svg?branch=master)](https://travisci.org/marcothuemmler/de.htwg.se.OthelloInScala)[![Coverage Status](https://coveralls.io/repos/github/marcothuemmler/de.htwg.se.OthelloInScala/badge.svg)](https://coveralls.io/github/marcothuemmler/de.htwg.se.OthelloInScala)![GitHub top language](https://img.shields.io/github/languages/top/marcothuemmler/OthelloInScala.svg)![GitHub](https://img.shields.io/github/license/marcothuemmler/OthelloInScala.svg)

Othello game for Software Engineering lecture at University of applied science Constance

<br/>

![Alt text](resources/screenshot.png?raw=true)


**Object of the Game**

The goal is to get the majority of colour discs on the board at the end of the game.

<br/>

**Game Play**

Othello is a strategy board game played between 2 players. One player plays black and the other white.

Each player gets 32 discs and black always starts the game.

The game then alternates between white and black until:

* one player can not make a valid move to outflank the opponent.
* both players have no valid moves.
* When a player has no valid moves, he pass his turn and the opponent continues.

A player can not voluntarily forfeit his turn.

When both players can not make a valid move the game ends.

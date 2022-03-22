package cs3500.freecell.controller;

/**
 * Represents possible move states in game.
 * GET_SRC  Waiting for the player to select a source pile.
 * GET_CARD Waiting for the player to select a card index.
 * GET_DEST Waiting for the player to select a destination pile.
 */
public enum MoveState {
  GET_SRC, GET_CARD, GET_DEST;
}

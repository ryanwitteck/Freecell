package cs3500.freecell.model;

import cs3500.freecell.model.multimove.MultiMoveFreecellModel;

/**
 * Implementation of factory class for freecell model.
 */
public class FreecellModelCreator {

  /**
   * Type for the two types of games.
   * Singlemove: player can only move one card per move.
   * Multimove: player is able to move blocks of cards in a single move.
   */
  public enum GameType {
    SINGLEMOVE, MULTIMOVE;
  }

  /**
   * Return the model of freecell that supports the given GameType.
   * @param type the game type - single or multi move.
   * @return new SimpleFreecellModel or new MultiMoveFreecellModel.
   */
  public static FreecellModel create(GameType type) {
    if (type == GameType.SINGLEMOVE) {
      return new SimpleFreecellModel();
    }
    else {
      return new MultiMoveFreecellModel();
    }
  }
}

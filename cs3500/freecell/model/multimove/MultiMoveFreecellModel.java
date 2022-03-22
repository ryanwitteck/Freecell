package cs3500.freecell.model.multimove;

import java.util.List;

import cs3500.freecell.cards.ICard;
import cs3500.freecell.model.PileType;
import cs3500.freecell.model.SimpleFreecellModel;

/**
 * This class represents a game of Freecell.
 */
public class MultiMoveFreecellModel extends SimpleFreecellModel {

  @Override
  public void move(PileType source, int pileNumber, int cardIndex,
                   PileType destination, int destPileNumber) {
    // if source pile and destination pile are not cascade piles, we cannot make a multi-move
    if (source != PileType.CASCADE || destination != PileType.CASCADE) {
      super.move(source, pileNumber, cardIndex, destination, destPileNumber);
      return;
    }
    // check if game has started
    checkHasStarted("Error: Game has not started - Move");

    // find appropriate source and destination piles
    List<ICard>[] src = getPile(source);
    List<ICard>[] dest = getPile(destination);

    // number of cards to move
    int nCards = src[pileNumber].size() - cardIndex;

    // if nCards < 2, not a multi-move
    if (nCards < 2) {
      super.move(source, pileNumber, cardIndex, destination, destPileNumber);
      return;
    }
    // check that multi-move of nCards is possible
    if (nCards > (countEmptyOpenPiles() + 1) * Math.pow(2, countEmptyCascadePiles())) {
      throw new IllegalArgumentException("Error - Impossible to move this many cards");
    }

    // check that source card can be placed on dest card
    if (!dest[destPileNumber].isEmpty()) {
      ICard sCard = src[pileNumber].get(cardIndex);
      ICard dCard = dest[destPileNumber].get(dest[destPileNumber].size() - 1);
      if (sameColor(sCard, dCard) || rankDif(dCard, sCard) != 1) {
        throw new IllegalArgumentException("Error: Invalid Move - Cannot Place " + sCard +
                " On Empty Pile (Dest: Foundation)");
      }
    }

    // check that card block being moved is valid
    for (int i = src[pileNumber].size() - 1; i > cardIndex; i--) {
      if (sameColor(src[pileNumber].get(i), src[pileNumber].get(i - 1))) {
        throw new IllegalArgumentException("Error - Invalid card block for multi-move (color)");
      }
      if (rankDif(src[pileNumber].get(i - 1), src[pileNumber].get(i)) != 1) {
        throw new IllegalArgumentException("Error - Invalid card block for multi-move (rank)");
      }
    }

    // move all cards
    for (int i = 0; i < nCards; i++) {
      dest[destPileNumber].add(src[pileNumber].remove(cardIndex));
    }
  }

  /**
   * Counts the number of open piles that are empty.
   *
   * @return the number of empty open piles.
   */
  private int countEmptyOpenPiles() {
    int count = 0;
    for (int i = 0; i < numOpenPiles; i++) {
      if (openPiles[i].isEmpty()) {
        count++;
      }
    }
    return count;
  }

  /**
   * Counts the number of cascade piles that are empty.
   *
   * @return the number of empty cascade piles.
   */
  private int countEmptyCascadePiles() {
    int count = 0;
    for (int i = 0; i < numCascadePiles; i++) {
      if (cascadePiles[i].isEmpty()) {
        count++;
      }
    }
    return count;
  }
}

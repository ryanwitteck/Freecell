package cs3500.freecell.model;

import java.util.List;

import cs3500.freecell.cards.ICard;

/**
 * This class represents a game of Freecell.
 */
public class SimpleFreecellModel extends AFreecellModel {

  @Override
  public void move(PileType source, int pileNumber, int cardIndex,
                   PileType destination, int destPileNumber) {
    // check if game has started
    checkHasStarted("Error: Game has not started - Move");

    // find appropriate source and destination piles
    List<ICard>[] src = getPile(source);
    List<ICard>[] dest = getPile(destination);

    // cannot move a card if the source pile is empty
    if (src[pileNumber].isEmpty()) {
      throw new IllegalArgumentException("Error: Invalid Move - Empty Source Pile");
    }
    // only the last card in the pile can be moved
    if (src[pileNumber].size() - 1 != cardIndex) {
      throw new IllegalArgumentException("Error: Invalid Move - Source Card Not Tail");
    }
    ICard sCard = src[pileNumber].get(cardIndex);

    if (dest[destPileNumber].isEmpty()) {
      // if dest is a foundation pile, only valid if source card is an Ace
      // if dest is not a foundation, always valid
      if (destination == PileType.FOUNDATION && !sCard.getRank().equals("A")) {
        throw new IllegalArgumentException("Error: Invalid Move - Cannot Place " + sCard +
                " On Empty Pile (Dest: Foundation)");
      }
    } else {
      ICard dCard = dest[destPileNumber].get(dest[destPileNumber].size() - 1);
      switch (destination) {
        case OPEN:
          // open piles can only hold one card
          throw new IllegalArgumentException("Error: Invalid Move - Destination Full (Dest: Open)");
        case FOUNDATION:
          /*
           * Valid move if:
           * the cards are the same suit and the source card is one rank above the dest card
           */
          if (!(dCard.getSuit().equals(sCard.getSuit())) || rankDif(sCard, dCard) != 1) {
            throw new IllegalArgumentException("Error: Invalid Move - Cannot Place " + sCard +
                    " on " + dCard + " (Dest: Foundation)");
          }
          break;
        case CASCADE:
          /*
           * Valid move if:
           * the cards are different colors and the source card is one rank below the dest card
           */
          if (sameColor(dCard, sCard) || rankDif(dCard, sCard) != 1) {
            throw new IllegalArgumentException("Error: Invalid Move - Cannot Place " + sCard +
                    " on " + dCard + " (Dest: Cascade)");
          }
          break;
        default:
          // throw error if invalid PileType
          throw new IllegalArgumentException("Error: Invalid PileType");
      }
    }

    // remove card from source and add to destination pile
    dest[destPileNumber].add(src[pileNumber].remove(cardIndex));
  }
}
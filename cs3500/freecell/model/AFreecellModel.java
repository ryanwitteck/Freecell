package cs3500.freecell.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import cs3500.freecell.cards.Card;
import cs3500.freecell.cards.ICard;
import cs3500.freecell.cards.Suit;

/**
 * Abstract class for freecell model.
 * Implements all methods defined in FreecellModel except for move.
 */
public abstract class AFreecellModel implements FreecellModel<ICard> {
  protected int numCascadePiles;
  protected int numOpenPiles;

  protected List<ICard>[] foundationPiles;
  protected List<ICard>[] cascadePiles;
  protected List<ICard>[] openPiles;

  protected boolean hasStarted;

  /**
   * Default Constructor.
   */
  public AFreecellModel() {
    numCascadePiles = -1;
    numOpenPiles = -1;

    foundationPiles = new List[0];
    cascadePiles = new List[0];
    openPiles = new List[0];

    hasStarted = false;
  }

  @Override
  public List<ICard> getDeck() {
    List<ICard> d = new ArrayList<ICard>();

    for (int i = 1; i < 14; i++) {
      d.add(new Card(i, Suit.CLUB));
      d.add(new Card(i, Suit.DIAMOND));
      d.add(new Card(i, Suit.HEART));
      d.add(new Card(i, Suit.SPADE));
    }
    return d;
  }

  @Override
  public void startGame(List<ICard> deck, int numCascadePiles, int numOpenPiles, boolean shuffle) {
    hasStarted = true;

    // throw error if too few piles
    if (numCascadePiles < 4 || numOpenPiles < 1) {
      throw new IllegalArgumentException("Error: Invalid Pile Size");
    }

    // throw error if deck is invalid
    if (deck.size() != 52 || !deck.containsAll(getDeck())) {
      throw new IllegalArgumentException("Error: Invalid Deck");
    }

    this.numCascadePiles = numCascadePiles;
    this.numOpenPiles = numOpenPiles;

    // initialize piles
    foundationPiles = new List[4];
    cascadePiles = new List[numCascadePiles];
    openPiles = new List[numOpenPiles];

    // fill foundationPiles with empty ArrayLists
    for (int i = 0; i < 4; i++) {
      foundationPiles[i] = new ArrayList<ICard>();
    }

    // fill cascadePiles with empty ArrayLists
    for (int i = 0; i < numCascadePiles; i++) {
      cascadePiles[i] = new ArrayList<ICard>();
    }

    // fill openPiles with invalid cards
    for (int i = 0; i < numOpenPiles; i++) {
      openPiles[i] = new ArrayList<ICard>();
    }

    // shuffle deck
    if (shuffle) {
      Collections.shuffle(deck);
    }

    // distribute deck to cascadePiles
    for (int i = 0; i < deck.size(); i++) {
      cascadePiles[i % numCascadePiles].add(deck.get(i));
    }
  }

  @Override
  public abstract void move(PileType source, int pileNumber, int cardIndex,
                            PileType destination, int destPileNumber);

  /**
   * Get openPiles, cascadePiles, or foundationPiles depending on given PileType.
   *
   * @param type  type of pile we want to get pointer to.
   * @return openPiles, cascadePiles, or foundationPiles.
   */
  protected List<ICard>[] getPile(PileType type) {
    switch (type) {
      case OPEN:
        // return openPiles
        return openPiles;
      case FOUNDATION:
        // return foundationPiles
        return foundationPiles;
      case CASCADE:
        // return cascadePiles
        return cascadePiles;
      default:
        // throw error if invalid PileType
        throw new IllegalArgumentException("Error: Invalid PileType");
    }
  }

  /**
   * Finds the numerical difference between the ranks of two cards.
   * A = 1, 2 = 2, ... 10 = 10, J = 11, Q = 12, K = 13.
   *
   * @param card1 any ICard.
   * @param card2 any ICard.
   * @return the numerical difference in the cards' ranks.
   */
  protected int rankDif(ICard card1, ICard card2) {
    return card1.getRankNum() - card2.getRankNum();
  }

  /**
   * Checks if two cards are the same color.
   *
   * @param card1 any ICard.
   * @param card2 any ICard.
   * @return if the two cards are the same color.
   */
  protected boolean sameColor(ICard card1, ICard card2) {
    return card1.getIsBlack() == card2.getIsBlack();
  }

  @Override
  public boolean isGameOver() {
    return foundationPiles[0].size() == 13
            && foundationPiles[1].size() == 13
            && foundationPiles[2].size() == 13
            && foundationPiles[3].size() == 13;
  }

  @Override
  public int getNumCardsInFoundationPile(int index) {
    checkHasStarted("Error: Game has not started - Get Num Foundation Pile");
    inBounds(index, 4, "Error: Invalid Index - Get Num Foundation Pile");
    return foundationPiles[index].size();
  }

  @Override
  public int getNumCascadePiles() {
    return numCascadePiles;
  }

  @Override
  public int getNumCardsInCascadePile(int index) {
    checkHasStarted("Error: Game has not started - Get Num Cascade Pile");
    inBounds(index, numCascadePiles, "Error: Invalid Index - Get Num Cascade Pile");
    return cascadePiles[index].size();
  }

  @Override
  public int getNumCardsInOpenPile(int index) {
    checkHasStarted("Error: Game has not started - Get Num Open Pile");
    inBounds(index, numOpenPiles, "Error: Invalid Index - Get Num Open Pile");
    return openPiles[index].size();
  }

  @Override
  public int getNumOpenPiles() {
    return numOpenPiles;
  }

  @Override
  public ICard getFoundationCardAt(int pileIndex, int cardIndex) {
    checkHasStarted("Error: Game has not started - Get Foundation Card");
    inBounds(pileIndex, 4, "Error: Invalid Pile Index - Get Foundation Card");
    inBounds(cardIndex, foundationPiles[pileIndex].size(),
            "Error: Invalid Card Index - Get Foundation Card");
    return foundationPiles[pileIndex].get(cardIndex);
  }

  @Override
  public ICard getCascadeCardAt(int pileIndex, int cardIndex) {
    checkHasStarted("Error: Game has not started - Get Cascade Card");
    inBounds(pileIndex, numCascadePiles, "Error: Invalid Pile Index - Get Cascade Card");
    inBounds(cardIndex, cascadePiles[pileIndex].size(),
            "Error: Invalid Card Index - Get Cascade Card");
    return cascadePiles[pileIndex].get(cardIndex);
  }

  @Override
  public ICard getOpenCardAt(int pileIndex) {
    checkHasStarted("Error: Game has not started - Get Open Card");
    inBounds(pileIndex, numOpenPiles, "Error: Invalid Index - Get Open Card");
    if (openPiles[pileIndex].isEmpty()) {
      return null;
    }
    return openPiles[pileIndex].get(0);
  }

  /**
   * Throw error if game hasn't started yet.
   *
   * @param msg error msg to print in event of error.
   */
  protected void checkHasStarted(String msg) {
    if (!hasStarted) {
      throw new IllegalStateException(msg);
    }
  }

  /**
   * Throw error if given index is out of bounds.
   *
   * @param index testing index.
   * @param upper upper bound of range.
   * @param msg   error msg to print in event of error.
   */
  protected void inBounds(int index, int upper, String msg) {
    if (index < 0 || index >= upper) {
      throw new IllegalArgumentException(msg);
    }
  }
}

package cs3500.freecell.cards;

/**
 * Represents the possible card suits.
 */
public enum Suit {
  CLUB("♣"), DIAMOND("♦"), HEART("♥"), SPADE("♠");

  private String str;

  private Suit(String str) {
    this.str = str;
  }

  @Override
  public String toString() {
    return this.str;
  }
}
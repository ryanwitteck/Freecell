package cs3500.freecell.cards;

/**
 * This class represents a card in a standard 52 card deck. Each card has a rank and suit.
 */
public interface ICard {
  /**
   * Getter for rank.
   * @return rank The rank of this card.
   */
  public String getRank();

  /**
   * Getter for suit.
   * @return suit The suit of this card.
   */
  public String getSuit();

  /**
   * Getter for isBlack.
   * @return isBlack Whether or not this card is black.
   */
  public boolean getIsBlack();

  /**
   * Get numerical value of this card's rank.
   * @return n The numerical value of this card's rank.
   */
  public int getRankNum();
}
package cs3500.freecell.cards;

/**
 * This class represents a card in a standard 52 card deck. Each card has a rank and suit.
 */
public class Card implements ICard {
  private String rank;
  private Suit suit;
  private boolean isBlack;

  /**
   * Constructor. Initializes rank and suit to given values.
   * @param rank The rank to be given to this card.
   * @param suit The suit to be given to this card.
   */
  public Card(int rank, Suit suit) {
    if (rank < 1 || rank > 13) {
      throw new IllegalArgumentException("Error: Rank out of Bounds [1, 13]");
    }

    switch (rank) {
      case 1:
        this.rank = "A";
        break;
      case 11:
        this.rank = "J";
        break;
      case 12:
        this.rank = "Q";
        break;
      case 13:
        this.rank = "K";
        break;
      default:
        this.rank = rank + "";
        break;
    }

    this.suit = suit;
    switch (this.suit) {
      case CLUB:
      case SPADE:
        this.isBlack = true;
        break;
      default:
        this.isBlack = false;
        break;
    }
  }

  @Override
  public String getRank() {
    return rank;
  }

  @Override
  public String getSuit() {
    return suit.toString();
  }

  @Override
  public boolean getIsBlack() {
    return isBlack;
  }

  @Override
  public int getRankNum() {
    switch (rank) {
      case "A":
        return 1;
      case "J":
        return 11;
      case "Q":
        return 12;
      case "K":
        return 13;
      default:
        return Integer.parseInt(rank);
    }
  }

  @Override
  public String toString() {
    return rank + suit;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == this) {
      return true;
    }
    if (!(obj instanceof Card)) {
      return false;
    }

    Card other = (Card)obj;
    return other.getRank().equals(rank) && other.getSuit().equals(suit.toString());
  }

  @Override
  public int hashCode() {
    return getRankNum() + suit.toString().hashCode();
  }
}

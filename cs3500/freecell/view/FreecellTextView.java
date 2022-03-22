package cs3500.freecell.view;

import java.io.IOException;

import cs3500.freecell.model.FreecellModelState;

/**
 * Represents the visuals of Freecell in text format.
 */
public class FreecellTextView implements FreecellView {

  private FreecellModelState fms;
  private Appendable ap;

  /**
   * Constructor for FreecellTextView.
   * @param fms The game of freecell.
   */
  public FreecellTextView(FreecellModelState<?> fms) {
    this.fms = fms;
  }

  /**
   * Constructor for FreecellTextView.
   * @param fms The game of freecell.
   * @param ap The visualization of freecell.
   */
  public FreecellTextView(FreecellModelState<?> fms, Appendable ap) {
    this.fms = fms;
    this.ap = ap;
  }

  @Override
  public void renderBoard() throws IOException {
    if (ap == null) {
      try {
        System.out.print(toString());
      }
      catch (Exception e) {
        throw new IOException("IOException: Render Board");
      }
    }

    ap.append(toString());
  }

  @Override
  public void renderMessage(String message) throws IOException {
    if (ap == null) {
      try {
        System.out.print(message);
      }
      catch (Exception e) {
        throw new IOException("IOException: Render Message");
      }
    }

    ap.append(message);
  }

  @Override
  public String toString() {
    if (fms.getNumCascadePiles() == -1) {
      return "";
    }
    String view = "";

    // string representation of foundation piles
    for (int i = 0; i < 4; i++) {
      view += "F" + (i + 1) + ":";

      int nCards = fms.getNumCardsInFoundationPile(i);
      if (nCards > 0) {
        for (int j = 0; j < nCards; j++) {
          view += " " + fms.getFoundationCardAt(i, j) + ",";
        }
        view = view.substring(0, view.length() - 1);
      }
      view += "\n";
    }

    // string representation of open piles
    for (int i = 0; i < fms.getNumOpenPiles(); i++) {
      view += "O" + (i + 1) + ":";

      int nCards = fms.getNumCardsInOpenPile(i);
      if (nCards > 0) {
        view += " " + fms.getOpenCardAt(i);
      }
      view += "\n";
    }

    // string representation of cascade piles
    for (int i = 0; i < fms.getNumCascadePiles(); i++) {
      view += "C" + (i + 1) + ":";

      int nCards = fms.getNumCardsInCascadePile(i);
      if (nCards > 0) {
        for (int j = 0; j < nCards; j++) {
          view += " " + fms.getCascadeCardAt(i, j) + ",";
        }
        view = view.substring(0, view.length() - 1);
      }
      view += "\n";
    }

    view = view.substring(0, view.length() - 1);

    return view;
  }
}

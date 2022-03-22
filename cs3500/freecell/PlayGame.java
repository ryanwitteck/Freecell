package cs3500.freecell;

import java.io.InputStreamReader;
import java.util.Collections;
import java.util.List;

import cs3500.freecell.cards.ICard;
import cs3500.freecell.controller.SimpleFreecellController;
import cs3500.freecell.model.FreecellModel;
import cs3500.freecell.model.SimpleFreecellModel;

/**
 * Runs game.
 */
public class PlayGame {

  /**
   * Main method.
   *
   * @param args args.
   */
  public static void main(String[] args) {
    FreecellModel model = new SimpleFreecellModel();
    SimpleFreecellController controller = new SimpleFreecellController(model,
            new InputStreamReader(System.in), System.out);

    List<ICard> deck = model.getDeck();
    Collections.reverse(deck);

    controller.playGame(deck, 4, 4, false);
  }
}

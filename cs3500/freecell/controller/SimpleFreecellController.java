package cs3500.freecell.controller;

import java.nio.CharBuffer;
import java.util.List;

import cs3500.freecell.cards.ICard;
import cs3500.freecell.model.FreecellModel;
import cs3500.freecell.model.PileType;
import cs3500.freecell.view.FreecellTextView;
import cs3500.freecell.view.FreecellView;

/**
 * Simple implementation of the freecell game controller. Works with
 * the IFreeCellModel interface to provide a game of freecell.
 */
public class SimpleFreecellController implements FreecellController<ICard> {

  private FreecellView view;
  private FreecellModel<ICard> model;
  private Readable rd;
  private Appendable ap;

  /**
   * Constructor for SimpleFreecellController.
   *
   * @param model The game of freecell.
   * @param rd    The ...
   * @param ap    The ...
   */
  public SimpleFreecellController(FreecellModel<ICard> model, Readable rd, Appendable ap) {
    if (model == null || rd == null || ap == null) {
      throw new IllegalArgumentException("Error: Null Argument");
    }

    this.model = model;
    this.rd = rd;
    this.ap = ap;
    this.view = new FreecellTextView(this.model, this.ap);
  }

  @Override
  public void playGame(List deck, int numCascades, int numOpens, boolean shuffle) {
    if (deck == null) {
      throw new IllegalArgumentException("Error: Null deck");
    }

    // start game
    try {
      model.startGame(deck, numCascades, numOpens, shuffle);
    } catch (Exception e) {
      renderMessage("Could not start game.");
      return;
    }

    /*
     * quitGame If the player has decided to quit the game.
     * gameOver If the player has beat the game.
     */
    boolean quitGame = false;
    boolean gameOver = false;

    // game loop
    while (!gameOver && !quitGame) {
      // render board
      renderBoard();

      // will return if the player has decided to quit the game
      quitGame = playerMove();

      gameOver = model.isGameOver();

      // if game won, render final game state and message "Game Over."
      if (gameOver) {
        renderBoard();
        renderMessage("Game over.");
      }
    }
  }

  /**
   * Handle player input and moves.
   *
   * @return whether the player has decided to quit the game.
   */
  private boolean playerMove() {
    boolean completeMove = false;
    MoveState state = MoveState.GET_SRC;

    // will be set based on user input.
    PileType src = null;
    PileType dest = null;
    int sPileIndex = -1;
    int dPileIndex = -1;
    int cardIndex = -1;

    // user input
    String arg = "";

    // get user input. loop as long as move is incomplete
    while (!completeMove) {
      arg = readArg();

      // reader has reached end. Throw exception.
      if (arg.isEmpty()) {
        throw new IllegalStateException("Error: End of Readable");
      }

      // if player wants to quit, quit.
      if (arg.equals("q") || arg.equals("Q")) {
        renderMessage("Game quit prematurely.");
        return true;
      }
      switch (state) {
        case GET_SRC:
          try {
            // get source pile information
            src = getPile(arg.charAt(0));
            sPileIndex = Integer.parseInt(arg.substring(1));

            // move onto next moveState
            state = MoveState.GET_CARD;
          } catch (Exception e) {
            if (src == null) {
              renderMessage("Invalid PileType for source pile. Try again.\n");
            } else {
              renderMessage("Index of source pile must be an integer. Try again.\n");
            }
            src = null;
            sPileIndex = -1;
          }
          break;
        case GET_CARD:
          try {
            cardIndex = Integer.parseInt(arg);
            // move onto next moveState
            state = MoveState.GET_DEST;
          } catch (Exception e) {
            renderMessage("Card index must be an integer. Try again.\n");
            cardIndex = -1;
          }
          break;
        case GET_DEST:
          try {
            // get dest pile information
            dest = getPile(arg.charAt(0));
            dPileIndex = Integer.parseInt(arg.substring(1));

            // move onto next moveState
            state = MoveState.GET_SRC;
          } catch (Exception e) {
            if (src == null) {
              renderMessage("Invalid PileType for destination pile. Try again.\n");
            } else {
              renderMessage("Index of destination pile must be an integer. Try again.\n");
            }
            dest = null;
            dPileIndex = -1;
          }
          break;
        default:
          throw new IllegalStateException("Error: Invalid Move State");
      }

      // if the parameters for move are defined, attempt to perform move
      if (src != null && dest != null) {
        if (attemptMove(src, sPileIndex, cardIndex, dest, dPileIndex)) {
          // attempt move
          completeMove = true;
        } else {
          // reset parameters
          src = null;
          cardIndex = -1;
          sPileIndex = -1;
          dPileIndex = -1;
          dest = null;
          state = MoveState.GET_SRC;
          // redo loop
          continue;
        }
      }
    }
    return false;
  }

  /**
   * Get the next input by the player.
   *
   * @return arg The next input of the player.
   */
  private String readArg() {
    boolean done = false;
    char in;
    String arg = "";
    do {
      // read next char in input
      CharBuffer cb = CharBuffer.allocate(1);
      try {
        rd.read(cb);
      } catch (Exception e) {
        throw new IllegalStateException("Error: Unable to read input");
      }

      // get input as char array
      in = cb.array()[0];

      if (Character.isWhitespace(in)) {
        done = !arg.isEmpty();
      } else if (in == '\0') {
        done = true;
      } else {
        arg += in;
      }
    }
    while (!done);

    return arg;
  }

  /**
   * Try to make the desired move.
   *
   * @param src        Source pile type.
   * @param sPileIndex Source pile index.
   * @param cardIndex  Source card index.
   * @param dest       Destination pile type.
   * @param dPileIndex Destination pile index
   * @return whether move was successful or not.
   */
  private boolean attemptMove(PileType src, int sPileIndex, int cardIndex,
                              PileType dest, int dPileIndex) {
    try {
      // attempt move
      model.move(src, sPileIndex - 1, cardIndex - 1,
              dest, dPileIndex - 1);
      return true;
    } catch (Exception e) {
      // invalid move
      renderMessage("Invalid move. Try again.\n");
      return false;
    }
  }

  /**
   * Get correlating pile type to the given char.
   *
   * @param c Char correlating to pile type.
   * @return the correlating pile type.
   */
  private PileType getPile(char c) {
    switch (c) {
      case 'c':
      case 'C':
        return PileType.CASCADE;
      case 'f':
      case 'F':
        return PileType.FOUNDATION;
      case 'o':
      case 'O':
        return PileType.OPEN;
      default:
        throw new IllegalArgumentException("Error: No Correlating PileType");
    }
  }

  /**
   * Try to render the given message in view.
   *
   * @param msg The desired message.
   */
  private void renderMessage(String msg) {
    // try to render message
    try {
      view.renderMessage(msg);
    } catch (Exception e) {
      throw new IllegalStateException("Error: Unable to render message");
    }
  }

  /**
   * Try to render the board.
   */
  private void renderBoard() {
    // try to render board
    try {
      view.renderBoard();
      ap.append('\n');
    } catch (Exception e) {
      throw new IllegalStateException("Error: Unable to render board");
    }
  }
}
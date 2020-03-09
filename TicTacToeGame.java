import java.util.ArrayList;

public class TicTacToeGame {

  private GameBoard gameBoard;

  private int nextPlayer;

  public TicTacToeGame() {
    this.gameBoard = new GameBoard();
    this.nextPlayer = 1;
  }

  /**
   * flips the turn so that the other player will place a symbol on the board
   *
   */

  public void flipPlayer() {
    this.nextPlayer = this.nextPlayer == 1 ? 2 : 1;
  }

  /**
   * returns the turn of the current player
   *
   * @return the player that is next to have their turn
   */

  public int getNextPlayer() {
    return this.nextPlayer;
  }

  /**
   * The player that indicates the move will have their symbol placed on the current board
   *
   * @param playerId player that is placing a symbol
   * @param row row on the board
   * @param col column on the board
   * @throws InvalidMoveException
   */


  public void makeNextPlay(int playerId, int row, int col) throws InvalidMoveException {
    if(gameBoard.isOccupied(row, col)) {
      throw new InvalidMoveException("occupied");
    }else{
      gameBoard.set(playerId+1, row, col);
    }
  }

  /**
   * the current boardmap being used
   *
   * @return
   */

  public String getBoardMap(){
      return gameBoard.asString();
  }

  /**
   * Prints the current board map
   *
   */

  public void printBoard(){
      gameBoard.printBoard();
  }

  /**
   * Checks win condition, if 3 of the same symbol are in a line, game is over
   *
   * @return the player who won
   */

  public int hasWinner() {
    ArrayList<Integer> gameEnd = new ArrayList<>();
    gameEnd.add(gameBoard.allOccupiedAndSame(0, 0, 1, 0, 2, 0));
    gameEnd.add(gameBoard.allOccupiedAndSame(0, 1, 1, 1, 2, 1));
    gameEnd.add(gameBoard.allOccupiedAndSame(0, 2, 1, 2, 2, 2));
    gameEnd.add(gameBoard.allOccupiedAndSame(0, 1, 0, 1, 0, 2));
    gameEnd.add(gameBoard.allOccupiedAndSame(1, 0, 1, 1, 1, 2));
    gameEnd.add(gameBoard.allOccupiedAndSame(2, 0 , 2, 1, 2, 2));
    gameEnd.add(gameBoard.allOccupiedAndSame(0, 0, 1, 1, 2, 2));
    gameEnd.add(gameBoard.allOccupiedAndSame(2, 0, 1, 1, 0, 2));
    for(int i : gameEnd) {
      if (i > 0) {
        return i;
      }
    }
    return 0;
  }

  public boolean isTie() {
    return gameBoard.checkTie();
  }
}

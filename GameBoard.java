public class GameBoard {
  private int[][] board;

  public GameBoard() {
    this("000000000");
  }

  /**
   * a valid input whether it be 0, 1, or 2
   *
   * @param c the character being checked
   * @return returns a 0, 1, or 2
   */

  private boolean isValid(char c) {
    return c == '0' || c == '1' || c == '2';
  }

  /**
   * creation of the game board
   *
   * @param preset the String that will be used to create the board
   * @throws IllegalArgumentException
   */

  public GameBoard(String preset) throws IllegalArgumentException {
    this.board = new int[3][3];
    char[] p = preset.trim().toCharArray();
    if (p.length != 9) {
      throw new IllegalArgumentException("Invalid board (" + preset + ")");
    }
    for (int i = 0; i < 3; i++) {
      for (int j = 0; j < 3; j++) {
        int pos = i * 3 + j;
        if (this.isValid(p[pos])) {
          this.set(p[pos] - '0', i, j);
        } else {
          throw new IllegalArgumentException("Invalid board (" + preset + ")");
        }
      }
    }
  }

  /**
   * Sets the board with a integer which indicates whether the symbol will be a "O" or an "X"
   *
   * @param playerId the player who's turn it is
   * @param row the row that has been indicated
   * @param col the column that has been indicated
   */

  public void set(int playerId, int row, int col) {
    this.board[row][col] = playerId;
  }

  /**
   * Gets the current board and returns the integer (the player's ID) in that position
   *
   * @param row row indication
   * @param col column indication
   * @return the integer at the given location
   */

  private int get(int row, int col) {
    return this.board[row][col];
  }

  /**
   * If the position is occupied, returns true
   *
   * @param row row indication
   * @param col column indication
   * @return boolean whether the position is occupied or not
   */

  public boolean isOccupied(int row, int col) {
    return this.get(row, col) != 0;
  }

  /**
   * The current mapping "000000000" of the board that will be used as the board skeleton
   * @return string of integers that indicate current moves from users
   */

  public String asString() {
    String s = "";
    for (int i = 0; i < 3; i++) {
      for (int j = 0; j < 3; j++) {
        s += this.board[i][j];
      }
    }
    return s;
  }

  /**
   * if the board is full (e.g. no 0s on the board map) the game is a tie and a false is given
   *
   * @return the game state of if the game has available moves or not
   */

  public boolean checkTie(){
    String temp = asString();
    if (temp.contains("0")){
      return false;
    }else
      return true;
  }

  /**
   * checks all positions to see if a win condition has been found by either player
   *
   * @param r1
   * @param c1
   * @param r2
   * @param c2
   * @param r3
   * @param c3
   * @return
   */


  public int allOccupiedAndSame(int r1, int c1, int r2, int c2, int r3, int c3) {
    boolean isSame = get(r1, c1) == get(r2, c2)
        && get(r1, c1) == get(r3, c3)
        && get(r2, c2) == get(r3, c3);

    return isSame ? get(r1, c1) : 0;
  }

  /**
   * symbols for creating the board
   *
   * @param s integer of the string of characters i.e. "000000000"
   * @return a spacing, an X or an O
   */

  private String convertToString(int s) {
    return s == 0 ? " " : s == 1 ? "O" : "X";
  }

  /**
   *  prints the board
   */

  public void printBoard() {
    for (int i = 0; i < 3; i++) {
      System.out.printf(" %s | %s | %s \n",
          convertToString(get(i, 0)),
          convertToString(get(i, 1)),
          convertToString(get(i, 2)));
      if (i != 2) {
        System.out.println("----------");
      }
    }
  }

}

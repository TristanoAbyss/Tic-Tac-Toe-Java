public class RowCol {
    private int row;

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    private int col;

    public RowCol(int row, int col) {
        this.row = row;
        this.col = col;
    }

    public String toString() {
        return row + " " + col;
    }
}

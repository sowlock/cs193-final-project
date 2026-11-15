package tetris.model;

/** stores landed blocks  */
public class Board {
    public static final int ROWS = 20;
    public static final int COLUMNS = 10;
    public static final char EMPTY = '.';

    private final char[][] cells;

    public Board() {
        cells = new char[ROWS][COLUMNS];
        clear();
    }

    /** empties the board  */
    public void clear() {
        for (int row = 0; row < ROWS; row++) {
            for (int column = 0; column < COLUMNS; column++) {
                cells[row][column] = EMPTY;
            }
        }
    }

    public boolean isInside(int row, int column) {
        return row >= 0 && row < ROWS && column >= 0 && column < COLUMNS;
    }

    /** outside the board counts as blocked. */
    public boolean isEmpty(int row, int column) {
        return isInside(row, column) && cells[row][column] == EMPTY;
    }

    /** reads one cell for rendering without exposing the mutable array. */
    public char getCell(int row, int column) {
        if (!isInside(row, column)) {
            throw new IndexOutOfBoundsException("Cell is outside the board.");
        }
        return cells[row][column];
    }



    public boolean canPlace(int[][] offsets, int row, int column) {
        for (int i = 0; i < offsets.length; i++) {
            int cellRow = row + offsets[i][0];
            int cellColumn = column + offsets[i][1];
            if (!isEmpty(cellRow, cellColumn)) {
                return false;
            }
        }
        return true;
    }

    /** saves a landed piece only after checking that every cell fits. */
    public void placePiece(int[][] offsets, int row, int column, char symbol) {
        if (symbol != 'I' && symbol != 'S' && symbol != 'O'
                && symbol != 'L' && symbol != 'Z') {
            throw new IllegalArgumentException("Unknown piece symbol.");
        }
        if (!canPlace(offsets, row, column)) {
            throw new IllegalArgumentException("Piece cannot fit at this position.");
        }
        for (int i = 0; i < offsets.length; i++) {
            cells[row + offsets[i][0]][column + offsets[i][1]] = symbol;
        }
    }

    /** removes full row */
    public int clearFullRows() {
        int cleared = 0;
        int destination = ROWS - 1;

        
        for (int row = ROWS - 1; row >= 0; row--) {
            boolean full = true;
            for (int column = 0; column < COLUMNS; column++) {
                if (cells[row][column] == EMPTY) {
                    full = false;
                    break;
                }
            }

            if (full) {
                cleared++;
            } else {
                for (int column = 0; column < COLUMNS; column++) {
                    cells[destination][column] = cells[row][column];
                }
                destination--;
            }
        }

        for (int row = 0; row <= destination; row++) {
            for (int column = 0; column < COLUMNS; column++) {
                cells[row][column] = EMPTY;
            }
        }
        return cleared;
    }
}

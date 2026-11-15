package tetris.model;


public class Piece {
    private final Tetromino type;
    private int row;
    private int column;
    private int rotation;

    /** starts the shape in rotation zero at the given board position. */
    public Piece(Tetromino type, int row, int column) {
        if (type == null) {
            throw new IllegalArgumentException("Piece type cannot be null.");
        }
        this.type = type;
        this.row = row;
        this.column = column;
        this.rotation = 0;
    }

    public Tetromino getType() {
        return type;
    }

    public char getSymbol() {
        return type.getSymbol();
    }

    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }

    public int getRotation() {
        return rotation;
    }

    
    public int[][] getCells() {
        return type.getCells(rotation);
    }

    
    public int[][] getNextRotationCells() {
        return type.getCells(type.nextRotation(rotation));
    }

    
    public void move(int rowChange, int columnChange) {
        row += rowChange;
        column += columnChange;
    }

    
    public void rotateClockwise() {
        rotation = type.nextRotation(rotation);
    }
}

package tetris.engine;

import java.util.Random;
import tetris.model.Board;
import tetris.model.Piece;
import tetris.model.Tetromino;

public class GameEngine {
    private final Board board;
    private final Random random;
    private Piece activePiece;
    private GameState state;
    private int score;

    public GameEngine() {
        this(new Random());
    }

    public GameEngine(Random random) {
        if (random == null) {
            throw new IllegalArgumentException("Random source cannot be null.");
        }
        this.random = random;
        board = new Board();
        restart();
    }

    public GameState getState() {
        return state;
    }

    public int getScore() {
        return score;
    }

    
    public char getBoardCell(int row, int column) {
        return board.getCell(row, column);
    }

    
    public Piece getActivePiece() {
        if (activePiece == null) {
            return null;
        }
        Piece copy = new Piece(activePiece.getType(),
                activePiece.getRow(), activePiece.getColumn());
        for (int i = 0; i < activePiece.getRotation(); i++) {
            copy.rotateClockwise();
        }
        return copy;
    }

    public boolean moveLeft() {
        return tryMove(0, -1);
    }

    public boolean moveRight() {
        return tryMove(0, 1);
    }

    private boolean tryMove(int rowChange, int columnChange) {
        if (state != GameState.RUNNING) {
            return false;
        }
        int row = activePiece.getRow() + rowChange;
        int column = activePiece.getColumn() + columnChange;
        if (!board.canPlace(activePiece.getCells(), row, column)) {
            return false;
        }
        activePiece.move(rowChange, columnChange);
        return true;
    }

 

    private void lockPiece() {
        board.placePiece(activePiece.getCells(), activePiece.getRow(),
                activePiece.getColumn(), activePiece.getSymbol());
        int cleared = board.clearFullRows();
        switch (cleared) {
            case 1: score += 100; break;
            case 2: score += 300; break;
            case 3: score += 500; break;
            case 4: score += 800; break;
            default: break;
        }
        spawnPiece();
    }

    private void spawnPiece() {
        Tetromino[] types = Tetromino.values();
        Tetromino type = types[random.nextInt(types.length)];
        int[][] cells = type.getCells(0);
        int width = 0;
        for (int i = 0; i < cells.length; i++) {
            width = Math.max(width, cells[i][1] + 1);
        }
        int column = (Board.COLUMNS - width) / 2;
        if (board.canPlace(cells, 0, column)) {
            activePiece = new Piece(type, 0, column);
        } else {
            activePiece = null;
            state = GameState.GAME_OVER;
        }
    }

    public void togglePause() {
        if (state == GameState.RUNNING) {
            state = GameState.PAUSED;
        } else if (state == GameState.PAUSED) {
            state = GameState.RUNNING;
        }
    }

    public void restart() {
        board.clear();
        score = 0;
        state = GameState.RUNNING;
        spawnPiece();
    }
}

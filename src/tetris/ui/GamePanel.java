package tetris.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import javax.swing.JPanel;
import tetris.engine.GameEngine;
import tetris.engine.GameState;
import tetris.model.Board;
import tetris.model.Piece;

public class GamePanel extends JPanel {
    public static final int CELL_SIZE = 25;
    private static final Color BOARD_COLOR = new Color(24, 32, 42);
    private static final Color GRID_COLOR = new Color(45, 56, 68);
    private final GameEngine game;

    public GamePanel(GameEngine game) {
        if (game == null) {
            throw new IllegalArgumentException("Game engine cannot be null.");
        }
        this.game = game;
        setPreferredSize(new Dimension(Board.COLUMNS * CELL_SIZE,
                Board.ROWS * CELL_SIZE));
        setBackground(BOARD_COLOR);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        int size = Math.min(getWidth() / Board.COLUMNS, getHeight() / Board.ROWS);
        if (size < 1) {
            return;
        }
        int width = size * Board.COLUMNS;
        int height = size * Board.ROWS;
        int left = (getWidth() - width) / 2;
        int top = (getHeight() - height) / 2;

        for (int row = 0; row < Board.ROWS; row++) {
            for (int column = 0;column < Board.COLUMNS; column++) {
                char symbol = game.getBoardCell(row, column);
                if (symbol != Board.EMPTY) {
                    drawBlock(g, left + column * size, top + row * size, size, symbol);
                }
            }
        }

        Piece piece = game.getActivePiece();
        if (piece != null) {
            int[][] cells = piece.getCells();
            for (int i = 0; i < cells.length; i++) {
                int row = piece.getRow() + cells[i][0];
                int column = piece.getColumn() + cells[i][1];
                drawBlock(g, left + column * size, top + row * size,
                        size, piece.getSymbol());
            }
        }

        g.setColor(GRID_COLOR);
        for (int row = 0; row < Board.ROWS; row++) {
            g.drawLine(left, top + row * size, left + width - 1, top + row * size);
        }
        for (int column = 0; column < Board.COLUMNS; column++) {
            g.drawLine(left + column * size, top, left + column * size, top + height - 1);
        }
        g.drawRect(left, top, width - 1, height - 1);

        if (game.getState() == GameState.PAUSED) {
            drawMessage(g, left, top, width, height, "Paused");
        } else if (game.getState() == GameState.GAME_OVER) {
            drawMessage(g, left, top, width, height, "Game Over");
        }
    }

    private void drawBlock(Graphics g, int x, int y, int size, char symbol) {
        switch (symbol) {
            case 'I': g.setColor(new Color(55, 200, 220)); break;
            case 'S': g.setColor(new Color(95, 200, 110)); break;
            case 'O': g.setColor(new Color(245, 210, 70)); break;
            case 'L': g.setColor(new Color(245, 155, 60)); break;
            case 'Z': g.setColor(new Color(230, 85, 100)); break;
            default: g.setColor(Color.GRAY); break;
        }
        g.fillRect(x, y, size, size);
    }

    private void drawMessage(Graphics g, int left, int top, int width,
            int height, String message) {
        g.setColor(new Color(0, 0, 0, 170));
        g.fillRect(left, top, width, height);
        g.setColor(Color.WHITE);
        g.setFont(new Font(Font.SANS_SERIF, Font.BOLD, Math.max(1, width / 10)));
        FontMetrics metrics = g.getFontMetrics();
        int x = left + (width - metrics.stringWidth(message)) / 2;
        int y = top + (height - metrics.getHeight()) / 2 + metrics.getAscent();
        g.drawString(message, x, y);
    }
}

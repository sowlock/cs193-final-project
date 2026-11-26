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

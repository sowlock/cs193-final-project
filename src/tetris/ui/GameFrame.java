package tetris.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.Timer;
import tetris.engine.GameEngine;
import tetris.engine.GameState;


public class GameFrame extends JFrame {
    private static final int FALL_INTERVAL_MS = 500;

    private final GameEngine game;
    private final GamePanel gamePanel;
    private final JLabel scoreLabel;
    private final JLabel stateLabel;
    private final JButton pauseButton;
    private final Timer gravity;

    private JPanel createSidebar() {
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setPreferredSize(new Dimension(190, 500));

        JLabel title = new JLabel("TETRIS");
        title.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 24));
        scoreLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 18));
        sidebar.add(title);
        sidebar.add(Box.createVerticalStrut(24));
        sidebar.add(scoreLabel);
        sidebar.add(Box.createVerticalStrut(8));
        sidebar.add(stateLabel);
        sidebar.add(Box.createVerticalStrut(24));

        sidebar.add(new JLabel("Left / Right: move"));
        sidebar.add(new JLabel("Up: rotate"));
        sidebar.add(new JLabel("Down: drop one row"));
        sidebar.add(new JLabel("P: pause / resume"));
        sidebar.add(new JLabel("R: restart"));
        sidebar.add(Box.createVerticalStrut(12));
        sidebar.add(new JLabel("Gravity: 0.5 seconds"));
        sidebar.add(Box.createVerticalStrut(24));

        
        pauseButton.setFocusable(false);
        pauseButton.addActionListener(event -> togglePause());
        sidebar.add(pauseButton);
        sidebar.add(Box.createVerticalStrut(8));

        JButton restartButton = new JButton("Restart");
        restartButton.setFocusable(false);
        restartButton.addActionListener(event -> restart());
        sidebar.add(restartButton);
        return sidebar;
    }

    private void bindKey(String key, String name, Runnable action) {
        getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
                .put(KeyStroke.getKeyStroke(key), name);
        getRootPane().getActionMap().put(name, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent event) {
                action.run();
                refresh();
            }
        });
    }

    private void togglePause() {
        game.togglePause();
        if (game.getState() == GameState.RUNNING) {
            gravity.restart();
        } else {
            gravity.stop();
        }
        refresh();
    }

    private void restart() {
        game.restart();
        gravity.restart();
        refresh();
    }

    private void refresh() {
        scoreLabel.setText("Score: " + game.getScore());
        GameState state = game.getState();
        if (state == GameState.RUNNING) {
            stateLabel.setText("Playing");
        } else if (state == GameState.PAUSED) {
            stateLabel.setText("Paused");
        } else {
            stateLabel.setText("Game over - restart to play");
        }
        pauseButton.setText(state == GameState.PAUSED ? "Resume" : "Pause");
        pauseButton.setEnabled(state != GameState.GAME_OVER);
        if (state != GameState.RUNNING) {
            gravity.stop();
        }
        gamePanel.repaint();
    }

    @Override
    public void dispose() {
        gravity.stop();
        super.dispose();
    }
}

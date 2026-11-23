package tetris.console;

import java.util.Locale;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;
import tetris.engine.GameEngine;
import tetris.engine.GameState;
import tetris.model.Board;
import tetris.model.Piece;


public class ConsoleMain {
    private static final long FALL_INTERVAL_MS = 500;
    public static void main(String[] args) {
        GameEngine game = new GameEngine();
        System.out.println("Java Tetris - Console Edition");
        System.out.println("Type a command and press Enter.");
        System.out.println("A: left | D: right | W: rotate | S or Enter: down");
        System.out.println("P: pause/resume | R: restart | Q: quit");
        System.out.println("@: falling piece | letters: landed blocks | .: empty");
        System.out.println("Pieces fall automatically every 0.5 seconds.");

        
        Timer gravity = new Timer("tetris-gravity", true);
        boolean[] playing = {true};
        printBoard(game);
        System.out.print("Command: ");
        gravity.schedule(new TimerTask() {
            @Override
            public void run() {
                synchronized (game) {
                    if (playing[0] && game.getState() == GameState.RUNNING) {
                        game.tick();
                        printBoard(game);
                        System.out.print("Command: ");
                    }
                }
            }
        }, FALL_INTERVAL_MS, FALL_INTERVAL_MS);

        try (Scanner input = new Scanner(System.in)) {
            while (input.hasNextLine()) {
                String command = input.nextLine().trim().toLowerCase(Locale.ROOT);
                synchronized (game) {
                    switch (command) {
                        case "a": game.moveLeft(); break;
                        case "d": game.moveRight(); break;
                        case "w": game.rotateClockwise(); break;
                        case "s": game.softDrop(); break;
                        case "": game.tick(); break;
                        case "p": game.togglePause(); break;
                        case "r": game.restart(); break;
                        case "q": playing[0] = false; break;
                        default:
                            System.out.println("Unknown command. Use A, D, W, S, P, R, Q, or Enter.");
                            break;
                    }
                    if (!playing[0]) {
                        break;
                    }
                    printBoard(game);
                    System.out.print("Command: ");
                }
            }
        } finally {
            synchronized (game) {
                playing[0] = false;
                gravity.cancel();
            }
        }
        System.out.println("Thanks for playing!");
    }


    private static void printBoard(GameEngine game) {
        char[][] display = new char[Board.ROWS][Board.COLUMNS];
        for (int row = 0; row < Board.ROWS; row++) {
            for (int column = 0; column < Board.COLUMNS; column++) {
                display[row][column] = game.getBoardCell(row, column);
            }
        }

        Piece piece = game.getActivePiece();
        if (piece != null) {
            int[][] cells = piece.getCells();
            for (int i = 0; i < cells.length; i++) {
                int row = piece.getRow() + cells[i][0];
                int column = piece.getColumn() + cells[i][1];
                display[row][column] = '@';
            }
        }

        System.out.println();
        System.out.println("Score: " + game.getScore() + " | State: " + game.getState());
        printBorder();
        for (int row = 0; row < Board.ROWS; row++) {
            System.out.print("|");
            for (int column = 0; column < Board.COLUMNS; column++) {
                System.out.print(display[row][column]);
            }
            System.out.println("|");
        }
        printBorder();

        if (game.getState() == GameState.PAUSED) {
            System.out.println("Paused. Enter P to resume, R to restart, or Q to quit.");
        } else if (game.getState() == GameState.GAME_OVER) {
            System.out.println("Game over! Enter R to restart or Q to quit.");
        }
    }

    private static void printBorder() {
        System.out.print("+");
        for (int column = 0; column < Board.COLUMNS; column++) {
            System.out.print("-");
        }
        System.out.println("+");
    }

}

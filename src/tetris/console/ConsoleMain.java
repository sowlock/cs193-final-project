package tetris.console;

import java.util.Locale;
import java.util.Scanner;
import tetris.engine.GameEngine;
import tetris.engine.GameState;
import tetris.model.Board;
import tetris.model.Piece;


public class ConsoleMain {
    public static void main(String[] args) {
        GameEngine game = new GameEngine();
        System.out.println("Java Tetris - Console Edition");
        System.out.println("Type a command and press Enter.");
        System.out.println("A: left | D: right | W: rotate | S or Enter: down");
        System.out.println("P: pause/resume | R: restart | Q: quit");
        System.out.println("@: falling piece | letters: landed blocks | .: empty");
        System.out.println("Pieces fall only when you enter S or press Enter.");

        try (Scanner input = new Scanner(System.in)) {
            boolean playing = true;
            while (playing) {
                printBoard(game); //print play board
                System.out.print("Command: ");
                if (!input.hasNextLine()) {
                    break;
                }
                String command = input.nextLine().trim().toLowerCase(Locale.ROOT);
                switch (command) {
                    case "a": game.moveLeft(); break;
                    case "d": game.moveRight(); break;
                    case "w": game.rotateClockwise(); break;
                    case "s": game.softDrop(); break;
                    case "": game.tick(); break;
                    case "p": game.togglePause(); break;
                    case "r": game.restart(); break;
                    case "q": playing = false; break;
                    default:
                        System.out.println("Unknown command. Use A, D, W, S, P, R, Q, or Enter.");
                        break;
                }
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

package tetris.model;

/** the five shapes */
public enum Tetromino {
    I(new int[][][] {
        {{0, 0}, {0, 1}, {0, 2}, {0, 3}},
        {{0, 0}, {1, 0}, {2, 0}, {3, 0}}
    }),
    S(new int[][][] {
        {{0, 1}, {0, 2}, {1, 0}, {1, 1}},
        {{0, 0}, {1, 0}, {1, 1}, {2, 1}}
    }),
    O(new int[][][] {
        {{0, 0}, {0, 1}, {1, 0}, {1, 1}}
    }),
    L(new int[][][] {
        {{0, 2}, {1, 0}, {1, 1}, {1, 2}},
        {{0, 0}, {1, 0}, {2, 0}, {2, 1}},
        {{0, 0}, {0, 1}, {0, 2}, {1, 0}},
        {{0, 0}, {0, 1}, {1, 1}, {2, 1}}
    }),
    Z(new int[][][] {
        {{0, 0}, {0, 1}, {1, 1}, {1, 2}},
        {{0, 1}, {1, 0}, {1, 1}, {2, 0}}
    });

    private final int[][][] rotations;

    Tetromino(int[][][] rotations) {
        this.rotations = rotations;
    }

    public char getSymbol() {
        return name().charAt(0);
    }

    public int getRotationCount() {
        return rotations.length;
    }

    /** returns the next state */
    public int nextRotation(int rotation) {
        checkRotation(rotation);
        return (rotation + 1) % rotations.length;
    }

    
    public int[][] getCells(int rotation) {
        checkRotation(rotation);
        int[][] copy = new int[4][2];
        for (int cell = 0; cell < 4; cell++) {
            copy[cell][0] = rotations[rotation][cell][0];
            copy[cell][1] = rotations[rotation][cell][1];
        }
        return copy;
    }

    private void checkRotation(int rotation) {
        if (rotation < 0 || rotation >= rotations.length) {
            throw new IllegalArgumentException("Invalid rotation number.");
        }
    }
}

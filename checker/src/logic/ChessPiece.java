package checker.src.logic;

import checker.src.Constant;

public class ChessPiece {
    int row;
    int col;
    public int chessColor;
    public boolean isKing;
    // int id;

    public ChessPiece(int row, int col, int color, boolean isKing) {
        this.row = row;
        this.col = col;
        this.chessColor = color;
        this.isKing = isKing;
    }

    public ChessPiece(ChessPiece cp) {
        this.row = cp.row;
        this.col = cp.col;
        this.chessColor = cp.chessColor;
        this.isKing = cp.isKing;
    }

    public boolean canGoTo(int[] dest, int distance) {
        int changeInRow = chessColor * -1 * distance;
        int kingChangeInRow = chessColor * distance;
        if (dest[0] != row + changeInRow && !(isKing && dest[0] == row + kingChangeInRow)) {
            return false;
        }
        if (dest[1] - col != -1 * distance && dest[1] - col != distance) {
            return false;
        }
        return true;
    }

    public int[] position() {
        int[] position = new int[2];
        position[0] = row;
        position[1] = col;
        return position;
    }

    public boolean reachingEnd(int[] move) {
        int target = (chessColor == Constant.WHITE) ? 7: 0;
        return move[0] == target;
    }

    @Override
    public String toString() {
        return "Chess piece at " + row + ", " + col
            + " of color " + chessColor;
    }

    public static int[] getMid(int[] a, int[] b) {
        int[] mid = new int[2];
        mid[0] = (a[0] + b[0]) / 2;
        mid[1] = (a[1] + b[1]) / 2;
        return mid;
    }
}

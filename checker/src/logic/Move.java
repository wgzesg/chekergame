package checker.src.logic;

import java.util.ArrayList;

public class Move {
    int[] from;
    int[] to;
    boolean captive;
    public boolean isCrowningMove = false;

    public Move(ArrayList<Integer> positions) {
        from = new int[2];
        to = new int[2];
        from[0] = positions.get(0);
        from[1] = positions.get(1);
        to[0] = positions.get(2);
        to[1] = positions.get(3);
        captive = (Math.abs(from[0] - to[0]) > 1);
    }

    public Move(int[] from, int[] to) {
        this.from = from;
        this.to = to;
        captive = (Math.abs(from[0] - to[0]) > 1);
    }

    public int[] getFrom() {
        return from;
    }

    public int[] getTo() {
        return to;
    }

    public boolean isCaptive() {
        return captive;
    }

    public boolean originates(Move nextMove) {
        return nextMove.from[0] == to[0] && nextMove.from[1] == to[1];
    }

    @Override
    public String toString() {
        return String.format("Chess (%d, %d) going to (%d, %d)", from[0], from[1], to[0], to[1]);
    }
}

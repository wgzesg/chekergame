package checker.src;

public class Constant {
    public static int BLACK = 1;
    public static int WHITE = -1;
    public static int ROW_DIM = 8;
    public static int COL_DIM = 8;
    public static int NO_ONE = 0;
    public static int HUMAN = 1;
    public static int MACHINE = -1;
    public static String rules() {
        return new StringBuilder()
                .append("Game rules:")
                .append('\n')
                .append("1. You can move your piecs diagnoally for 1 block. You can only move to front")
                .append('\n')
                .append("2. You can capture an enemy by hopping over it. You can continue to capture enemies if possible.")
                .append('\n')
                .append("3. If you can capture an enemy, you have to do so")
                .append('\n')
                .append("4. After reaching the furthers row, that chess piece becomes \"King\" and gains power to move backward")
                .append('\n')
                .append("5. If you can capture a King, you become a king")
                .append('\n')
                .append("5. If your chess piece becomes a king in this turn, your turn stops")
                .append('\n')
                .append("6. If you have no more place to move, you lose")
                .toString();
    }
}

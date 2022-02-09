package checker.src.logic;

import java.util.ArrayList;

public class Player {
    GameTable table;
    public boolean hasCaptiveFlag = false;
    public boolean canContinue = false;
    public int myColor;
    public Move preCaptiveMove;
    private int lookDown = 1;
    
    public Player(int color, GameTable table) {
        this.myColor = color;
        this.table = table;
    }

    public Player(int color, GameTable table, int lookDown) {
        this.myColor = color;
        this.table = table;
        this.lookDown = lookDown;
    }

    public ArrayList<Move> getAllPossibleMoves(GameTable table) {
        return getAllPossibleMovesForPlayer(table, myColor);
    }

    private ArrayList<Move> getAllPossibleMovesForPlayer(GameTable table, int color) {
        ArrayList<Move> possibleMoves = new ArrayList<>();
        boolean hasCaptive = false;
        for(ChessPiece p : table.cellsWithChess.values()) {
            if(p.chessColor == color) {
                hasCaptive |= update(table, p, possibleMoves);
            }
        }
        if(hasCaptive) {
            possibleMoves.removeIf(s -> !s.captive);
            hasCaptiveFlag = true;
        } else {
            hasCaptiveFlag = false;
        }
        return possibleMoves;
    }

    public void setPreCaptiveMove(Move m) {
        this.preCaptiveMove = m;
        checkContinue();
    }

    private void checkContinue() {
        if(preCaptiveMove == null || preCaptiveMove.isCrowningMove) {
            canContinue = false;
            return;
        }
        ArrayList<Move> possibleMoves = new ArrayList<>();
        update(table, table.get(preCaptiveMove.to), possibleMoves);
        possibleMoves.removeIf(s -> !s.captive);
        if(possibleMoves.size() != 0) {
            canContinue = true;
            return;
        }
        preCaptiveMove = null;
        canContinue = false;
    }

    public Move getAMove() {
        Move m = minimax(lookDown);
        return m;
    }

    private boolean update(GameTable table, ChessPiece p, ArrayList<Move> possibleMoves) {
        ArrayList<Move> moves = table.getAllPositions(p);
        possibleMoves.addAll(moves);
        if(moves.size() != 0 && moves.get(0).isCaptive()) {
            return true;
        }
        return false;
    }

    public Move minimax(int furtherSteps) {
        GameTable copy = new GameTable(table);
        int score = Integer.MIN_VALUE;
        Move action_to_take = null;

        ArrayList<Move> allMoves = getAllPossibleMoves(copy);
        for (var move : allMoves) {
            int maxVal = minimise(result(copy, move), furtherSteps, score);
            if(maxVal >= score) {
                score = maxVal;
                action_to_take = move;
            }
        }

        return action_to_take;
    }

    public int minimise(GameTable original, int furthurSteps, int prevMax) {
        if(furthurSteps == 0) {
            return original.evaluate(myColor);
        }

        int score = Integer.MAX_VALUE;
        ArrayList<Move> allmoves = getAllPossibleMovesForPlayer(original, -1 * myColor);
        if(allmoves.size() == 0) {
            return Integer.MAX_VALUE;
        }
        for(var move: allmoves) {
            int minVal = maximise(result(original, move), furthurSteps - 1, score);
            if(minVal <= prevMax) {
                return minVal;
            }
            if(minVal < score) {
                score = minVal;
            }
        }
        return score;
    }

    public int maximise(GameTable original, int furthurSteps, int prevMin) {
        if(furthurSteps == 0) {
            return original.evaluate(myColor);
        }

        int score = Integer.MIN_VALUE;
        ArrayList<Move> allmoves = getAllPossibleMovesForPlayer(original, myColor);
        if (allmoves.size() == 0) {
            return Integer.MIN_VALUE;
        }
        for(var move: allmoves) {
            int maxVal = minimise(result(original, move), furthurSteps - 1, score);
            if(maxVal >= prevMin) {
                return maxVal;
            }
            if(maxVal > score) {
                score = maxVal;
            }
        }
        return score;
    }

    public GameTable result(GameTable original, Move move) {
        GameTable copy = new GameTable(original);
        copy.makeMove(move);
        return copy;
    }
}
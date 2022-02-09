package checker.src.logic;
import java.util.ArrayList;
import java.util.HashMap;

import checker.src.Constant;

public class GameTable {

    HashMap<Integer, ChessPiece> cellsWithChess;

    private static int[][] directions = {{1,1}, {1,-1}, {-1,1}, {-1,-1}};
    private static int[][] captiveDirections = {{2,2}, {2,-2}, {-2,2}, {-2,-2}};

    public GameTable() {
        cellsWithChess = new HashMap<>();
    }

    public GameTable(GameTable gt) {
        cellsWithChess = new HashMap<>();
        for (var entry : gt.cellsWithChess.entrySet())
        {
            cellsWithChess.put(entry.getKey(), new ChessPiece(entry.getValue()));
            // cellsWithChess.put(entry.getKey(), new ChessPiece(entry.getValue()));
        }
    }

    public void spawnChessPieces(int row, int col, int color) {
        int key = simpleHash(row, col);
        cellsWithChess.put(key, new ChessPiece(row, col, color, false));
    }

    public  Result verifyMove(Move newMove, Player p) {
        ChessPiece from = get(newMove.getFrom());
        p.getAllPossibleMoves(this);
        Result rs = new Result(false, "");
        if(from == null) {
            rs.msg = "You did not select a valid chess piece!";
            return rs;
        }
        if(p.hasCaptiveFlag && !newMove.isCaptive()) {
            rs.msg = "You have to do a capture move!";
            return rs;
        }
        if(p.preCaptiveMove != null && !p.preCaptiveMove.originates(newMove)) {
            rs.msg = "You have to move the element that captured in previous round!";
            return rs;
        }
        if(from.chessColor != p.myColor) {
            rs.msg = "Not your chess piece!";
            return rs;
        }
        if(!canGoTo(from, newMove.getTo())) {
            rs.msg = "You cannot reach that place";
            return rs;
        }
        rs.returnCode = true;
        return rs;
    }
    
    public ChessPiece get(int[] key) {
        return cellsWithChess.get(simpleHash(key[0], key[1]));
    }

    public ArrayList<Move> getAllPositions(ChessPiece p) {
        ArrayList<Move> possibleMoves = getCaptive(p);
        if(possibleMoves.size() != 0) {
            return possibleMoves;
        }
        int[] myPosition = p.position();

        for(var dir : directions) {
            int[] possilePosition = new int[2];
            possilePosition[0] = myPosition[0] + dir[0];
            possilePosition[1] = myPosition[1] + dir[1];
            if(withinBound(possilePosition[0]) && withinBound(possilePosition[1]) && canGoTo(p, possilePosition)) {
                Move nm = new Move(myPosition, possilePosition);
                if(crowningMove(p, nm)) {
                    nm.isCrowningMove = true;
                }
                possibleMoves.add(nm);
            }
        }
        return possibleMoves;
    }

    public ArrayList<Move> getCaptive(ChessPiece p) {
        ArrayList<Move> possibleMoves = new ArrayList<>();
        int[] myPosition = p.position();

        for(var dir : captiveDirections) {
            int[] possiblePosition = new int[2];
            possiblePosition[0] = myPosition[0] + dir[0];
            possiblePosition[1] = myPosition[1] + dir[1];
            if(withinBound(possiblePosition[0]) && withinBound(possiblePosition[1]) && canGoTo(p, possiblePosition)) {
                Move nm = new Move(myPosition, possiblePosition);
                if(crowningMove(p, nm)) {
                    nm.isCrowningMove = true;
                }
                possibleMoves.add(nm);
            }
        }
        return possibleMoves;
    }

    public boolean crowningMove(ChessPiece p, Move mv) {
        if (p.isKing) {
            return false;
        }
        if (p.reachingEnd(mv.getTo())) {
            return true;
        }
        if (mv.isCaptive()) {
            int[] middleKey = ChessPiece.getMid(mv.getFrom(), mv.getTo());
            ChessPiece middle = get(middleKey);
            if(middle != null && middle.isKing) {
                return true;
            }
        }
        return false;
    }

    private boolean withinBound(int val) {
        return val >= 0 && val < Constant.ROW_DIM;
    }

    public boolean canGoTo(ChessPiece from, int[] to) {
        if(get(to) != null) {
            return false;
        }
        if(from.canGoTo(to, 1)) {
            return true;
        } else if (from.canGoTo(to, 2)) {
            int[] middleKey = ChessPiece.getMid(from.position(), to);
            ChessPiece middle = get(middleKey);
            if(middle != null && middle.chessColor != from.chessColor) {
                return true;
            }
        }
        return false;

    }

    public void makeMove(Move newMove) {
        int[] from = newMove.getFrom();
        int[] to = newMove.getTo();
        int pc_color = this.get(from).chessColor;
        if(newMove.isCrowningMove) {
        }
        put(to, pc_color, newMove.isCrowningMove || this.get(from).isKing);
        remove(from);
        if(newMove.isCaptive()) {
            int[] mid = ChessPiece.getMid(from, to);
            remove(mid);
        }

    }

    private void remove(int[] key) {
        cellsWithChess.remove(simpleHash(key));
    }

    private void put(int[] key, int color, boolean isKing) {
        cellsWithChess.put(simpleHash(key), new ChessPiece(key[0], key[1], color, isKing));
    }

    private int simpleHash(int row, int col) {
        return 10 * row + col;
    }

    private int simpleHash(int[] key) {
        return 10 * key[0] + key[1];
    }

    public boolean stillHaveMoves(int color) {
        for(ChessPiece p : cellsWithChess.values()) {
            if(p.chessColor == color) {
                ArrayList<Move> moves = getAllPositions(p);
                if (moves.size() != 0) {
                    return true;
                }
            }
        }
        return false;
    }

    public int evaluate(int machineColor) {
        int myScore = 0;
        for(var p : cellsWithChess.values()) {
            if(p.chessColor == machineColor) {
                myScore += (p.isKing) ? 2 : 1;
            } else {
                myScore -= (p.isKing) ? 2 : 1;
            }
        }
        return myScore;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < Constant.COL_DIM; i++) {
            for(int j = 0; j < Constant.COL_DIM; j++) {
                int[] key = {i,j};
                if(get(key) != null) {
                    sb.append((get(key).chessColor == Constant.BLACK) ? 'X' : '*');
                } else  {
                    sb.append(' ');
                }
            }
            sb.append('\n');
        }
        return sb.toString();
    }
}

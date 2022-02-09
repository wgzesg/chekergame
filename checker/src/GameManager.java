package checker.src;

import java.util.ArrayList;
import checker.src.logic.GameTable;
import checker.src.logic.Move;
import checker.src.logic.Player;
import checker.src.logic.Result;
import checker.src.ui.SimpleUi;

public class GameManager {
    
    private static GameManager gm = null;
    SimpleUi ui;
    int humanColor = Constant.BLACK;
    int machineColor = Constant.WHITE;
    Player machine;
    Player human;
    int whoseTurn;
    boolean gameEneded = false;
    GameTable table;
    int winner = Constant.NO_ONE;
    ArrayList<Integer> positions = new ArrayList<>();

    private GameManager() {
        ui = new SimpleUi();
        table = new GameTable();
        machine = new Player(machineColor, table);
        human = new Player(humanColor, table);
        whoseTurn = Constant.HUMAN;
    }

    public static GameManager getInstance()
    {
        if (gm == null)
            gm = new GameManager();
 
        return gm;
    }

    public void run() {
        ui.initGUi();
    }

    public void startGame(int difficulty) {
        ui.initComponents();
        table = new GameTable();
        machine = new Player(machineColor, table, difficulty);
        human = new Player(humanColor, table);
        whoseTurn = Constant.HUMAN;
        gameEneded = false;
        whoseTurn = Constant.HUMAN;
        winner = Constant.NO_ONE;
        spawnChessPieces();
    }
    
    public void selectChessPiece(int x, int y) {
        if(gameEneded) {
            ui.unselect(x,y);
            return;
        }
        positions.add(x);
        positions.add(y);
        if(positions.size() == 4) {
            ui.unselect(positions.get(0), positions.get(1));
            ui.unselect(positions.get(2), positions.get(3));
            boolean isSuccessful = huamnMakeMove();
            if(!isSuccessful || human.canContinue) {
                return;
            }
            if(winner == Constant.NO_ONE) {
                machineMakeMove();
            }
        }
    }

    private void makeMove(Move newMove, int color) {
        ui.makeMove(newMove, color);
        table.makeMove(newMove);
    }

    private boolean huamnMakeMove() {
        whoseTurn = Constant.HUMAN;
        Move m = new Move(positions);
        positions.clear();
        Result rs = table.verifyMove(m, human);
        if(!rs.returnCode) {
            ui.setMessage(rs.msg);
            return false;
        }
        ui.setMessage("");
        m.isCrowningMove = table.crowningMove(table.get(m.getFrom()), m);
        makeMove(m, human.myColor);
        winner = checkWinLose();
        if(m.isCaptive() && !m.isCrowningMove) {
            human.setPreCaptiveMove(m);
        } else {
            human.setPreCaptiveMove(null);
        }
        return true;
    }

    private void machineMakeMove() {
        whoseTurn = Constant.MACHINE;
        do {
            Move newMove = machine.getAMove();
            makeMove(newMove, machine.myColor);
            winner = checkWinLose();
            if(newMove.isCaptive() && !newMove.isCrowningMove) {
                machine.setPreCaptiveMove(newMove);
            } else {
                machine.setPreCaptiveMove(null);
            }
        } while(machine.canContinue);
    }

    private int checkWinLose() {
        if(whoseTurn == Constant.HUMAN && !table.stillHaveMoves(machineColor)) {
            gameEneded = true;
            ui.setMessage("You win!");
            return Constant.HUMAN;
        } else if(whoseTurn == Constant.MACHINE && !table.stillHaveMoves(humanColor)) {
            gameEneded = true;
            ui.setMessage("You lose!");
            return Constant.MACHINE;
        }
        return Constant.NO_ONE;
    }

    private void spawnChessPieces() {
        for(int i = 0; i < Constant.ROW_DIM; i++) {
            for(int j = 0; j < Constant.COL_DIM; j++) {
                if((i + j) % 2 == 1) {
                    if(i < 3) {
                        ui.drawChess(i, j, Constant.WHITE);
                        table.spawnChessPieces(i, j, Constant.WHITE);
                    } else if (i > 4) {
                        ui.drawChess(i, j, Constant.BLACK);
                        table.spawnChessPieces(i, j, Constant.BLACK);
                    }
                }
            }
        }
    }
}
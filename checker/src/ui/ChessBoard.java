package checker.src.ui;
import javax.swing.*;

import checker.src.Constant;

import java.awt.*;

public class ChessBoard extends JPanel {

    CellAvatar[][] table = new CellAvatar[Constant.ROW_DIM][Constant.COL_DIM];

    public ChessBoard() {
        this.setLayout(new GridLayout(Constant.ROW_DIM,Constant.COL_DIM));
        this.setSize(240, 240);
        for(int i = 0; i < Constant.ROW_DIM; i++) {
            for(int j = 0; j < Constant.COL_DIM; j++) {
                CellAvatar cell = new CellAvatar(i, j);
                table[i][j] = cell;
                this.add(cell);
            }
        }
    }

    public void drawChess(int row, int col, int color, boolean isKing) {
        table[row][col].chessPiece = color;
        table[row][col].isKing |= isKing;
        table[row][col].updateUI();
    }

    public boolean getCellCrowningState(int row, int col) {
        return table[row][col].isKing;
    }

    public CellAvatar[][] getTable() {
        return this.table;
    }

    public void unselect(int x, int y) {
        table[x][y].selected = false;
        table[x][y].updateUI();;
    }
}

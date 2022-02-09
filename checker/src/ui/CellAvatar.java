package checker.src.ui;

import java.awt.Rectangle;
import java.awt.geom.Ellipse2D;
import java.awt.Graphics2D;
import javax.swing.JPanel;

import checker.src.GameManager;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.Color;
import java.awt.Graphics;

public class CellAvatar extends JPanel {
    private static GameManager gm = GameManager.getInstance();
    
    private Rectangle rect = new Rectangle(0, 0, 30, 30);
    private Ellipse2D circle = new Ellipse2D.Double(5, 5, 20, 20);
    private Ellipse2D kingCrown = new Ellipse2D.Double(10, 10, 10, 10);
    int row;
    int col;
    boolean color;
    int chessPiece;
    boolean isKing = false;
    boolean selected = false;

    public CellAvatar(int row, int col) {
        this.row = row;
        this.col = col;
        color = (row + col) % 2 == 0;
        chessPiece = 0;
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent me) {
                super.mouseClicked(me);
                selected = true;
                updateUI();
                gm.selectChessPiece(row, col);
            }
        });
    }

    @Override
    protected void paintComponent(Graphics grphcs) {
        super.paintComponent(grphcs);
        Graphics2D g2d = (Graphics2D) grphcs;
        if (selected) {
            g2d.setColor(Color.BLUE);
        } else if (color == true) {
            g2d.setColor(Color.WHITE);
        } else {
            g2d.setColor(Color.BLACK);
        }
        g2d.fill(rect);
        if(chessPiece == -1) {
            g2d.setColor(Color.WHITE);
            g2d.fill(circle);
        } else if (chessPiece == 1){
            g2d.setColor(Color.RED);
            g2d.fill(circle);
        } else {
            isKing = false;
        }
        if(isKing) {
            g2d.setColor(Color.YELLOW);
            g2d.fill(kingCrown);
        }
    }

}

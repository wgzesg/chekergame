package checker.src.ui;

import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import checker.src.Constant;
import checker.src.GameManager;
import checker.src.logic.ChessPiece;
import checker.src.logic.Move;

public class SimpleUi {

    JFrame frame;
    Container container;

    ChessBoard chessBoard;
    TextArea warningMessages;
    JButton newGame;
    JButton rules;

    public void initGUi() {
        frame = new JFrame();
        frame.setTitle("Checker game");
        container = frame.getContentPane();
        container.setLayout(new GridLayout(3, 1));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        initComponents();
        frame.setSize(240, 240*3);
        frame.setResizable(false);
        frame.setVisible(true);
    }

    public void initComponents() {
        container.removeAll();
        initChessBoard();
        initButtons();
        initMessageField();
    }

    private void initMessageField() {
        warningMessages = new TextArea("");
        warningMessages.setEditable(false);
        container.add(warningMessages);
    }

    public void setMessage(String s) {
        warningMessages.setText(s);
    }

    public void drawChess(int row, int col, int color) {
        chessBoard.drawChess(row, col, color, false);
    }

    private void initChessBoard() {
        chessBoard = new ChessBoard();
        container.add(chessBoard);
    }

    private void initButtons() {
        JPanel buttonFrames = new JPanel();
        newGame = new JButton("New game");
        buttonFrames.add(newGame);
        String[] options = {"Easy", "Middle", "Hard"};
        newGame.addActionListener(new ActionListener() { 
            @Override
            public void actionPerformed(ActionEvent e) {
                int x = JOptionPane.showOptionDialog(null, "What difficulty level to choose?",
                    "Difficulty choice",
                    JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);
                if(x == -1) {
                    return;
                }
                GameManager.getInstance().startGame(2 * x + 2);
            } 
        } );
        rules = new JButton("Rules");
        buttonFrames.add(rules);
        rules.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                JOptionPane.showMessageDialog(buttonFrames, 
                    Constant.rules(),
                    "Game rule",
                    JOptionPane.INFORMATION_MESSAGE);
            }
        });

        container.add(buttonFrames);

    }

    public void makeMove(Move newMove, int color) {
        int[] from = newMove.getFrom();
        int[] to = newMove.getTo();
        boolean isKing = chessBoard.getCellCrowningState(from[0], from[1]);
        chessBoard.drawChess(from[0], from[1], 0, false);
        chessBoard.drawChess(to[0], to[1], color, isKing || newMove.isCrowningMove);
        if(newMove.isCaptive()) {
            int[] mid = ChessPiece.getMid(from, to);
            chessBoard.drawChess(mid[0], mid[1], 0, false);
        }
    }

    public void unselect(int x, int y) {
        chessBoard.unselect(x,y);
    }
}
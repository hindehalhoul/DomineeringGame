/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package domi.game;

/**
 *
 * @author HP
 */
import static domi.game.Domineering.HORIZONTAL;
import static domi.game.Domineering.VERTICAL;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

public class DomineeringGUI extends JFrame {

    private Domineering domineering;
    private JButton[][] buttons;

    private DomineeringPosition currentPosition;

    public DomineeringGUI() {
        domineering = new Domineering();
        initializeGUI();
    }

    private void initializeGUI() {
        int rows = 8;
        int cols = 8;

        buttons = new JButton[rows][cols];
        JPanel boardPanel = new JPanel(new GridLayout(rows, cols));

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                buttons[i][j] = new JButton();
                buttons[i][j].setPreferredSize(new Dimension(50, 50));
                buttons[i][j].addActionListener(new ButtonClickListener(i, j));
                boardPanel.add(buttons[i][j]);
            }
        }

        add(boardPanel, BorderLayout.CENTER);

        JButton resetButton = new JButton("Reset Game");
        resetButton.addActionListener(new ActionListener() {
            @Override

            public void actionPerformed(ActionEvent e) {
                resetGame();
            }
        });

        add(resetButton, BorderLayout.SOUTH);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private class ButtonClickListener implements ActionListener {

        private int row;
        private int col;

        public ButtonClickListener(int row, int col) {
            this.row = row;
            this.col = col;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            DomineeringPosition position = (DomineeringPosition) domineering.getPosition();
            int endRow, endCol, orientation;
            int ver = 2, hor = 1;

            if (domineering.isComputerTurn()) {
                // Computer player, set orientation to VERTICAL
                endRow = row + 1;
                endCol = col;
                orientation = ver;
            } else {
                // Human player, set orientation to HORIZONTAL
                endRow = row;
                endCol = col + 1;
                orientation = hor;
            }

            // Make the move
            if (position.isValidMove(row, col, endRow, endCol, orientation)) {
                DomineeringMove move = new DomineeringMove(row, col, domineering.isComputerTurn());
                domineering.makeMove(domineering.getPosition(), domineering.isComputerTurn(), move);

                updateGUI(row, col, orientation);

                // Check if the game has ended
                if (!domineering.wonPosition(position, domineering.isComputerTurn())) {
                    // Computer's turn
                    computerMove();
                }
            } else {
                System.out.println("Invalid move!");
            }
        }

    }

//    private void computerMove() {
//        System.out.print("computer move");
//        Vector v = domineering.alphaBeta(0, domineering.getPosition(), domineering.isComputerTurn());
//        if (v.size() > 1) {
//            MoveResult moveResult = new MoveResult((DomineeringPosition) domineering.getPosition(), null);
//            moveResult.makeMoveComputer(domineering.getPosition(), domineering.isComputerTurn(), (Move) v.elementAt(1));
//            int[] placedDominoResult = moveResult.getPlacedDominoResult();
//            int lastI = placedDominoResult[0];
//            int lastJ = placedDominoResult[1];
//            updateGUI(lastI, lastJ, 2);
//        }
//
//    }
    private void computerMove() {
        System.out.print("computer move");
        Vector v = domineering.alphaBeta(0, domineering.getPosition(), domineering.isComputerTurn());
        System.out.print("\n VECTOR  \n " + v);

        if (v.size() > 1) {
            Position p = (Position) v.elementAt(1);
            DomineeringPosition pos = (DomineeringPosition) p;
            DomineeringMove move = new DomineeringMove(pos);

            // Correct the order of arguments in the following line
            int[] placedDominoResult = domineering.makeMoveComputer(p, move.startRow, move.startCol, move.endRow, move.endCol, 2, domineering.isComputerTurn());

            System.out.print("\nMOVE  0: " + placedDominoResult[0]);
            System.out.print("\nMOVE  1: " + placedDominoResult[1]);
            updateGUI(placedDominoResult[0], placedDominoResult[1], 2);
        }
    }
    //    private void makeProgramMove() {
            //        currentPosition.makeProgramMove();
            //        updateGUI();
            //        
            //    }


    private void updateGUI(int row, int col, int orientation) {
        int[][] board = domineering.getBoard();

        if (orientation == Domineering.HORIZONTAL) {
            buttons[row][col].setText("H");
            buttons[row][col + 1].setText("H");
            buttons[row][col].setEnabled(false);
            buttons[row][col + 1].setEnabled(false);
        } else if (orientation == Domineering.VERTICAL) {
            buttons[row][col].setText("M");
            buttons[row + 1][col].setText("M");
            buttons[row][col].setEnabled(false);
            buttons[row + 1][col].setEnabled(false);
        } else {
            buttons[row][col].setText("AH");
        }
    }

    private void resetGame() {
        domineering = new Domineering();
        for (int i = 0; i < buttons.length; i++) {
            for (int j = 0; j < buttons[i].length; j++) {
                buttons[i][j].setText("");
                buttons[i][j].setEnabled(true);
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new DomineeringGUI());
    }
}

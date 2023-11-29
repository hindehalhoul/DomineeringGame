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
import javax.swing.text.Position;

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
        currentPosition = new DomineeringPosition(rows, cols);

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
            if (currentPosition.isValidMove(row, col, endRow, endCol, orientation)) {
                DomineeringMove move = new DomineeringMove(row, col, domineering.isComputerTurn());
                domineering.makeMove(currentPosition, domineering.isComputerTurn(), move);

                updateGUI(row, col, orientation);

                // Check if the game has ended
                if (!domineering.wonPosition(currentPosition, domineering.isComputerTurn())) {
                    // Computer's turn
                    computerMove(currentPosition);
                }
            } else {
                System.out.println("Invalid move!");
            }
        }
    }

   
    private void computerMove(DomineeringPosition currentPosition) {
        System.out.print("computer move");
        Vector v = domineering.alphaBeta(0, currentPosition, !domineering.isComputerTurn());
        System.out.print("\n VECTOR  \n " + v);

        if (v.size() > 1) {
//            Position p = (Position) v.elementAt(1);
//            System.out.print("\n p \n " + p.toString());
            DomineeringPosition pos = (DomineeringPosition) v.elementAt(1);
            DomineeringMove move = new DomineeringMove(pos);

            System.out.print("\n X  " + pos.getRows());
            System.out.print("\n Y  " + pos.getCols());

            // Find the position of the vertical domino (value 2)
            int verticalRow = -1;
            int verticalCol = -1;

            for (int i = 0; i < pos.getRows(); i++) {
                for (int j = 0; j < pos.getCols(); j++) {
                    if (pos.getBoard()[i][j] == 2) {
                        verticalRow = i;
                        verticalCol = j;
                        break;
                    }
                }
                if (verticalRow != -1) {
                    break;
                }
            }

            domineering.makeMove(pos, !domineering.isComputerTurn(), move);

            updateGUI(verticalRow, verticalCol, 2);
            // Now verticalRow and verticalCol contain the position of the vertical domino
            System.out.println("\nVertical Domino Position: (" + verticalRow + ", " + verticalCol + ")");
        }
    }

    private void updateGUI(int row, int col, int orientation) {

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
        DomineeringPosition p = new DomineeringPosition(8, 8);
        Domineering domineering = new Domineering();
        domineering.currentPosition = p; // Set the initial position
        SwingUtilities.invokeLater(() -> new DomineeringGUI());
    }
}

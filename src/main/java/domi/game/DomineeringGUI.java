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

    private Position currentPosition;

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
            System.out.print("gui 1\n");
            DomineeringPosition position = (DomineeringPosition) domineering.getPosition();
            int endRow, endCol, orientation;
            int ver = 2, hor = 1;

            if (domineering.isComputerTurn()) {
                // Computer player, set orientation to VERTICAL
                endRow = row + 1;
                endCol = col;
                orientation = ver;
                System.out.print("gui \n");
            } else {
                // Human player, set orientation to HORIZONTAL
                endRow = row;
                endCol = col + 1;
                orientation = hor;
                System.out.print("gui 3\n");
            }

            // Make the move
            if (position.isValidMove(row, col, endRow, endCol, orientation)) {
                System.out.print("gui 4\n");
                DomineeringMove move = new DomineeringMove(row, col, domineering.isComputerTurn());
                System.out.print("gui 5\n");
                domineering.makeMove(domineering.getPosition(), domineering.isComputerTurn(), move);
                System.out.print("gui 6\n");
                // Update the GUI
                updateGUI();

                // Check if the game has ended
                if (!domineering.wonPosition(domineering.getPosition(), domineering.isComputerTurn())) {
                    // Computer's turn
                    computerMove();
                }
            } else {
                System.out.println("Invalid move!");
            }
        }

    }

    private void computerMove() {
        System.out.print("computer move");
        Vector v = domineering.alphaBeta(0, domineering.getPosition(), domineering.isComputerTurn());
        if (v.size() > 1) {
            domineering.makeMove(domineering.getPosition(), domineering.isComputerTurn(), (Move) v.elementAt(1));
            updateGUI();
        }
    }

    private void updateGUI() {
        System.out.print("update\n");
        System.out.print("update board");
        int[][] board = domineering.getBoard();
        for (int i = 0; i < buttons.length; i++) {
            for (int j = 0; j < buttons[i].length; j++) {
                if (board[i][j] == Domineering.HORIZONTAL) {
                    buttons[i][j].setText("H");
                } else if (board[i][j] == Domineering.VERTICAL) {
                    buttons[i][j].setText("V");
                } else {
                    buttons[i][j].setText("");
                }
                buttons[i][j].setEnabled(false); // Disable buttons after each move
            }
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

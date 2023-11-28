/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package domi.game;

/**
 *
 * @author HP
 */
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Vector;

public class DomineeringGUI extends JFrame {

    private Domineering domineering;
    private JButton[][] buttons;
    private int[][] board;
    
    private int HUMAN = 3;
    private int COMPUTER = 4;

    public DomineeringGUI() {
        // Set up the JFrame
        super("Domineering Game");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 400);

        // Initialize the game with an empty board
        domineering = new Domineering(new DomineeringPosition(8, 8));
        domineering.isComputerTurn = false;

        // Create the grid of buttons
        int rows = domineering.getRows();
        int cols = domineering.getCols();
        buttons = new JButton[rows][cols];

        // Create a panel with GridLayout to hold the buttons
        JPanel panel = new JPanel(new GridLayout(rows, cols));

        // Initialize buttons and add them to the panel
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                buttons[i][j] = new JButton();
                buttons[i][j].setFont(new Font("Arial", Font.PLAIN, 20));
                buttons[i][j].setFocusPainted(false);
                final int row = i;
                final int col = j;
                buttons[i][j].addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        onButtonClick(row, col);
                    }
                });
                panel.add(buttons[i][j]);
            }
        }

        // Add a mouse listener for domino placement
        panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int x = e.getX() / (getSize().width / cols);
                int y = e.getY() / (getSize().height / rows);
                onButtonClick(y, x); // Adapt to row, col format
            }
        });

        // Add the panel to the JFrame
        add(panel);

        // Display the JFrame
        setVisible(true);

        // Initialize the game board
        board = new int[rows][cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                board[i][j] = Domineering.EMPTY;
            }
        }

        // Start the game
        playGame();
    }

    private void onButtonClick(int row, int col) {
        if (!domineering.isComputerTurn() && domineering.isValidMove(row, col)) {
            DomineeringPosition p = new DomineeringPosition(row, col);
            DomineeringMove move = new DomineeringMove(row, col, domineering.isComputerTurn());
            domineering.makeMove(p, domineering.isComputerTurn(), move);

            // Update the game board based on the move
//            board[row][col] = domineering.isComputerTurn() ? Domineering.COMPUTER : Domineering.HUMAN;

            updateButtons();

            // Check if the game is over after the human player's move
            if (!domineering.wonPosition(domineering.getPosition(), false)) {
                // If not, proceed with the computer's turn
                playComputerTurn();
            }
        }
    }

    private void updateButtons() {
        // Update button text based on the game board state
        for (int i = 0; i < buttons.length; i++) {
            for (int j = 0; j < buttons[i].length; j++) {
                if (board[i][j] == 4) {
                    buttons[i][j].setText("C");
                } else if (board[i][j] == 3) {
                    buttons[i][j].setText("H");
                } else {
                    buttons[i][j].setText("");
                }
            }
        }
    }

    private void playGame() {
        // Human's turn
        domineering.isComputerTurn = true; // Set to true to allow computer's turn in the next iteration

        // Check if the game is over after the human player's move
        if (!domineering.wonPosition(domineering.getPosition(), false)) {
            // Computer's turn
            playComputerTurn();
        }
    }

    private void playComputerTurn() {
        // Computer's turn
        domineering.isComputerTurn = true;
        // Implement your computer move logic here
        // ...

        // Update the game board and buttons
        updateButtons();

        // Check if the game is over after the computer's move
        if (!domineering.wonPosition(domineering.getPosition(), true)) {
            // Allow the EDT to handle events before starting the human's turn
            SwingUtilities.invokeLater(() -> playHumanTurn());
        }
    }

    private void playHumanTurn() {
        // Human's turn
        domineering.isComputerTurn = false;

        // Check if the game is over after the human player's move
        if (!domineering.wonPosition(domineering.getPosition(), false)) {
            // Continue the game with the computer's turn
            playComputerTurn();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new DomineeringGUI();
            }
        });
    }
}

















/*public class DomineeringGUI extends JFrame {

    private Domineering domineering;
    private JButton[][] buttons;
    private int[][] board;

    public DomineeringGUI() {
        
        // Set up the JFrame
        super("Domineering Game");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 400);

        // Initialize the game with an empty board
        domineering = new Domineering(new DomineeringPosition(8, 8));
        domineering.isComputerTurn = false;

        // Create the grid of buttons
        int rows = domineering.getRows();
        int cols = domineering.getCols();
        buttons = new JButton[rows][cols];

        // Create a panel with GridLayout to hold the buttons
        JPanel panel = new JPanel(new GridLayout(rows, cols));
        board = new int[rows][cols];
        // Set all cells to an initial state, e.g., empty
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                board[i][j] = Domineering.EMPTY;
            }
        }

        // Initialize buttons and add them to the panel
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                buttons[i][j] = new JButton();
                buttons[i][j].setFont(new Font("Arial", Font.PLAIN, 20));
                buttons[i][j].setFocusPainted(false);
                final int row = i;
                final int col = j;
                buttons[i][j].addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        onButtonClick(row, col);
                    }
                });
                panel.add(buttons[i][j]);
            }
        }

        // Add the panel to the JFrame
        add(panel);

        // Display the JFrame
        setVisible(true);

        // Start the game
        playGame();
    }

    private void onButtonClick(int row, int col) {
    if (!domineering.isComputerTurn() && domineering.isValidMove(row, col)) {
        DomineeringPosition p = new DomineeringPosition(row, col);
        DomineeringMove move = new DomineeringMove(row, col, domineering.isComputerTurn());
        domineering.makeMove(p, domineering.isComputerTurn(), move);
        updateButtons();

        // Check if the game is over after the human player's move
        if (!domineering.wonPosition(domineering.getPosition(), false)) {
            // If not, proceed with the computer's turn
            playComputerTurn();
        }
    }
}


    private void updateButtons() {
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
            }
        }
    }

    private void playGame() {
    // Start the game with the computer's turn
    playComputerTurn();
}

private void playComputerTurn() {
    // Computer's turn
    domineering.isComputerTurn = true;
    Vector v = domineering.alphaBeta(0, domineering.getPosition(), true);

    if (v.size() > 1 && v.elementAt(1) instanceof DomineeringMove) {
        int rows = domineering.getRows();
        int cols = domineering.getCols();
        DomineeringPosition p = new DomineeringPosition(rows, cols);

        DomineeringMove computerMove = (DomineeringMove) v.elementAt(1);
        domineering.makeMove(p, domineering.isComputerTurn(), computerMove);
        updateButtons();
    }

    // Check if the game is over after the computer's move
    if (!domineering.wonPosition(domineering.getPosition(), true)) {
        // Allow the EDT to handle events before starting the human's turn
        SwingUtilities.invokeLater(() -> playHumanTurn());
    }
}

private void playHumanTurn() {
    // Human's turn
    domineering.isComputerTurn = false;

    // Check if the game is over after the human player's move
    if (!domineering.wonPosition(domineering.getPosition(), false)) {
        // Continue the game with the computer's turn
        playComputerTurn();
    }
}


    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new DomineeringGUI();
            }
        });
    }
}
*/
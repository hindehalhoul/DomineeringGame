package domi.game;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;
import javax.swing.JButton;

public class Domineering extends GameSearch {

    // Define constants for the Domineering game
    public static final int EMPTY = 0;
    public static final int HORIZONTAL = 1;
    public static final int VERTICAL = 2;
    public boolean isComputerTurn = false; // computerTurn = 0;
    public DomineeringPosition currentPosition;

    public int computerMoveRow;
    public int computerMoveCol;

    public Domineering() {
        currentPosition = new DomineeringPosition(8, 8);

    }

    public Domineering(DomineeringPosition initialPosition) {
        System.out.print("construct init");
        currentPosition = initialPosition;
    }

    @Override
    public boolean wonPosition(Position p, boolean player) {
        // Implement the winning position check for Domineering
        DomineeringPosition pos = (DomineeringPosition) p;
        int marker = (player) ? HORIZONTAL : VERTICAL;

        for (int i = 0; i < pos.getRows(); i++) {
            for (int j = 0; j < pos.getCols(); j++) {
                if (pos.getBoard()[i][j] == marker) {
                    // Check if the placed domino creates a horizontal line
                    if (pos.isValidMove(i, j, i, j + 1, HORIZONTAL)) {
                        return true;
                    }

                    // Check if the placed domino creates a vertical line
                    if (pos.isValidMove(i, j, i + 1, j, VERTICAL)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    @Override
    public float positionEvaluation(Position p, boolean player) {
        DomineeringPosition pos = (DomineeringPosition) p;
        int playerMoves = possibleMoves(pos, player).length;
        int opponentMoves = possibleMoves(pos, !player).length;

        if (playerMoves == 0 && opponentMoves > 0) {
            // Le joueur n'a pas de mouvements possibles, donc il perd
            return -1.0f;
        } else if (opponentMoves == 0 && playerMoves > 0) {
            // L'adversaire n'a pas de mouvements possibles, donc le joueur gagne
            return 1.0f;
        } else {
            // Sinon, évaluez la position en fonction du nombre de mouvements possibles
            return playerMoves - opponentMoves;
        }
    }

    @Override
    public void printPosition(Position p) {

        // Implement the printing of the Domineering board
        DomineeringPosition pos = (DomineeringPosition) p;
        int[][] board = pos.getBoard();
        for (int row = 0; row < pos.getRows(); row++) {
            for (int col = 0; col < pos.getCols(); col++) {
                if (pos.getBoard()[row][col] == HORIZONTAL) {
                    System.out.print("H ");
                } else if (pos.getBoard()[row][col] == VERTICAL) {
                    System.out.print("V ");
                } else {
                    System.out.print(pos.getBoard()[row][col] + " ");
                }
            }
            System.out.println();
        }
        System.out.println();
    }

    @Override
    public Position[] possibleMoves(Position p, boolean player) {
        // Implement generating possible moves for Domineering
        DomineeringPosition pos = (DomineeringPosition) p;
        List<Position> moves = new ArrayList<>();

        for (int row = 0; row < pos.getRows() - 1; row++) {
            for (int col = 0; col < pos.getCols(); col++) {
                // If the player is the computer, only consider vertical moves
                if (!player && pos.isValidMove(row, col, row + 1, col, VERTICAL)) {
                    DomineeringPosition newPos = new DomineeringPosition(pos);
                    newPos.placeDomino(row, col, row + 1, col, VERTICAL, player);
                    moves.add(newPos);
                }
                // If the player is the human, only consider horizontal moves
                if (player && col < pos.getCols() - 1 && pos.getBoard()[row][col] != VERTICAL && pos.getBoard()[row][col + 1] != VERTICAL && pos.isValidMove(row, col, row, col + 1, HORIZONTAL)) {
                    DomineeringPosition newPos = new DomineeringPosition(pos);
                    newPos.placeDomino(row, col, row, col + 1, HORIZONTAL, player);
                    moves.add(newPos);
                }
            }
        }

        return moves.toArray(new Position[0]);
    }

    public int[] makeMoveComputer(Position p,int startRow, int startCol, int endRow, int endCol, int orientation, boolean player) {
        isComputerTurn = !player;
        DomineeringPosition pos = (DomineeringPosition) p;
        DomineeringPosition newPos = new DomineeringPosition(pos);

        // Make a move and get the result (row, col)
        int[] placedDominoResult = newPos.placeDomino(startRow, startCol, endRow, endCol, orientation, player);

        // Update the current position
        currentPosition = new DomineeringPosition(newPos);

        return placedDominoResult;
    }

    @Override
    public Position makeMove(Position p, boolean player, Move move) {

        isComputerTurn = !player;
        DomineeringPosition pos = (DomineeringPosition) p;
        DomineeringMove domMove = (DomineeringMove) move;

        DomineeringPosition newPos = new DomineeringPosition(pos);
        newPos.placeDomino(domMove.startRow, domMove.startCol, domMove.endRow, domMove.endCol, domMove.orientation, player);

        return newPos;
    }

    public boolean reachedMaxDepth(Position p, int depth) {
        boolean ret = false;
        if (depth >= 3) {
            return true;
        }
        if (wonPosition(p, false)) {
            ret = true;
        } else if (wonPosition(p, true)) {
            ret = true;
        }
        if (GameSearch.DEBUG) {
            System.out.println("reachedMaxDepth: pos=" + p.toString() + ", depth=" + depth
                    + ", ret=" + ret);
        }
        return ret;
    }

    public boolean isComputerTurn() {
        return isComputerTurn;  // computerTurn = true; 
    }

    public void resetTurn() {
        isComputerTurn = false;
    }

    public Position getPosition() {
        return currentPosition;
    }
    // In the Domineering class

    public int getRows() {
        return ((DomineeringPosition) getPosition()).getRows();
    }

    public int getCols() {
        return ((DomineeringPosition) getPosition()).getCols();
    }

    public int[][] getBoard() {
        return ((DomineeringPosition) getPosition()).getBoard();
    }

    @Override
    public Move createMove() {
        System.out.print("createMove");
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter move (startRow startCol): ");
        int startRow = scanner.nextInt();
        int startCol = scanner.nextInt();
        int endRow, endCol, orientation;

        if (isComputerTurn()) {
            // Computer player, set orientation to VERTICAL
            endRow = startRow + 1;
            endCol = startCol;
            orientation = VERTICAL;
        } else {
            // Human player, set orientation to HORIZONTAL
            endRow = startRow;
            endCol = startCol + 1;
            orientation = HORIZONTAL;
        }
        DomineeringMove move = new DomineeringMove(startRow, startCol, endRow, endCol, orientation);

        return move;
    }

    public static void main(String[] args) {
        DomineeringPosition p = new DomineeringPosition(8, 8);
        Domineering domineering = new Domineering();
        domineering.currentPosition = p; // Set the initial position

        domineering.isComputerTurn = false; // Initialize the flag at the beginning of the game
        domineering.playGame(p, true);
        domineering.resetTurn();
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

package domi.game;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Domineering extends GameSearch {

    // Define constants for the Domineering game
    public static final int EMPTY = 0;
    public static final int HORIZONTAL = 1;
    public static final int VERTICAL = 2;
    private boolean isComputerTurn = false; // computerTurn = 0;

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
        // Implement the position evaluation for Domineering (you may need to customize this)
        // Return a positive value if the current player is winning, a negative value if losing,
        // and 0 if the position is drawn.
        return 0.0f; // Placeholder, customize as needed
    }

    @Override
    public void printPosition(Position p) {
        // Implement the printing of the Domineering board
        DomineeringPosition pos = (DomineeringPosition) p;
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
                if (player && pos.getBoard()[row][col] != VERTICAL && pos.isValidMove(row, col, row, col + 1, HORIZONTAL)) {
                    DomineeringPosition newPos = new DomineeringPosition(pos);
                    newPos.placeDomino(row, col, row, col + 1, HORIZONTAL, player);
                    moves.add(newPos);
                }
            }
        }

        return moves.toArray(new Position[0]);
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

    private boolean isComputerTurn() {
        return isComputerTurn;  // computerTurn = true; 
    }

    public void resetTurn() {
        isComputerTurn = false;
    }

    @Override
    public Move createMove() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter move (startRow startCol): ");
        int startRow = scanner.nextInt();
        int startCol = scanner.nextInt();
//        int orientation = scanner.nextInt();

//        int endRow, endCol;
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
        domineering.isComputerTurn = false; // Initialize the flag at the beginning of the game
        domineering.playGame(p, true);
        domineering.resetTurn();
    }
}

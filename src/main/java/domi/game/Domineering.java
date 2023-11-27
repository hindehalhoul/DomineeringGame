package domi.game;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Domineering extends GameSearch {

    // Define constants for the Domineering game
    public static final int EMPTY = 0;
    public static final int HORIZONTAL = 1;
    public static final int VERTICAL = 2;

    @Override
    public boolean drawnPosition(Position p) {
        // Implement the drawn position check for Domineering
        DomineeringPosition pos = (DomineeringPosition) p;
        for (int i = 0; i < pos.getRows(); i++) {
            for (int j = 0; j < pos.getCols(); j++) {
                if (pos.getBoard()[i][j] == EMPTY) {
                    return false; // If there is an empty spot, the game is not drawn
                }
            }
        }
        return true; // All spots are filled, and no player has won, so it's a draw
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
        // Implement the position evaluation for Domineering (you may need to customize this)
        // Return a positive value if the current player is winning, a negative value if losing,
        // and 0 if the position is drawn.
        return 0.0f; // Placeholder, customize as needed
    }

//    @Override
//    public void printPosition(Position p) {
//        // Implement the printing of the Domineering board
//        DomineeringPosition pos = (DomineeringPosition) p;
//        for (int row = 0; row < pos.getRows(); row++) {
//            for (int col = 0; col < pos.getCols(); col++) {
//                if (pos.getBoard()[row][col] == HORIZONTAL) {
//                    System.out.print("H");
//                } else if (pos.getBoard()[row][col] == VERTICAL) {
//                    System.out.print("V");
//                } else {
//                    System.out.print("0 ");
//                }
//            }
//            System.out.println();
//        }
//        System.out.println();
//    }
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
                if (pos.isValidMove(row, col, row + 1, col, VERTICAL)) {
                    DomineeringPosition newPos = new DomineeringPosition(pos);
                    newPos.placeDomino(row, col, row + 1, col, VERTICAL, player);
                    moves.add(newPos);
                }

                if (pos.isValidMove(row, col, row, col + 1, HORIZONTAL)) {
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
        // Implement making a move for Domineering
        DomineeringPosition pos = (DomineeringPosition) p;
        DomineeringMove domMove = (DomineeringMove) move;

        DomineeringPosition newPos = new DomineeringPosition(pos);
        newPos.placeDomino(domMove.startRow, domMove.startCol, domMove.endRow, domMove.endCol, domMove.orientation, player);

        return newPos;
    }

    @Override
    public boolean reachedMaxDepth(Position p, int depth) {
        // Implement the max depth check for Domineering
        return depth >= 5; // Adjust the depth as needed
    }

//    @Override
//    public Move createMove() {
//        // Implement creating a move for Domineering (you may need to customize this)
//        Scanner scanner = new Scanner(System.in);
//        System.out.print("Enter move (startRow startCol endRow endCol orientation): ");
//        int startRow = scanner.nextInt();
//        int startCol = scanner.nextInt();
//        int endRow = scanner.nextInt();
//        int endCol = scanner.nextInt();
//        int orientation = scanner.nextInt();
//
//        DomineeringMove move = new DomineeringMove(startRow, startCol, endRow, endCol, orientation);
//
//        return move;
//    }
    @Override
    public Move createMove() {
        // Implement creating a move for Domineering based on start position and orientation
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter move (startRow startCol orientation)(Horizontal = 1; Vertical = 2): ");
        int startRow = scanner.nextInt();
        int startCol = scanner.nextInt();
        int orientation = scanner.nextInt();
//        int orientation = scanner.nextInt();

        int endRow, endCol;

        // Calculate end position based on the size of the domino (size 2)
        if (orientation == HORIZONTAL) {
            endRow = startRow;
            endCol = startCol + 1;
        } else {
            endRow = startRow + 1;
            endCol = startCol;
        }

        DomineeringMove move = new DomineeringMove(startRow, startCol, endRow, endCol, orientation);

        return move;
    }

    public static void main(String[] args) {
        DomineeringPosition p = new DomineeringPosition(8, 8);
        Domineering domineering = new Domineering();
        domineering.playGame(p, true);
    }
}

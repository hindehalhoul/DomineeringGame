package domi.game;

import java.util.Arrays;

public class DomineeringPosition extends Position implements Cloneable {

    int[][] board;

    public DomineeringPosition(int rows, int cols) {
        board = new int[rows][cols];
    }

    public DomineeringPosition(DomineeringPosition existingPosition) {
        int rows = existingPosition.getRows();
        int cols = existingPosition.getCols();
        this.board = new int[rows][cols];

        for (int i = 0; i < rows; i++) {
            this.board[i] = existingPosition.getBoard()[i].clone();
        }
    }

    public int[][] getBoard() {
        return board;
    }

    // Add the methods getRows and getCols
    public int getRows() {
        return board.length;
    }

    public int getCols() {
        return board[0].length;
    }
    public void placeDomino(int startRow, int startCol, int endRow, int endCol, int orientation, boolean player) {
    // Check if the move is valid before placing the domino
    if (isValidMove(startRow, startCol, endRow, endCol, orientation)) {
        int marker = (player) ? Domineering.HORIZONTAL : Domineering.VERTICAL;

        // Place the domino on the board
        for (int i = startRow; i <= endRow && i < getRows(); i++) {
            for (int j = startCol; j <= endCol && j < getCols(); j++) {
                board[i][j] = marker;
            }
        }
    } else {
        // Handle invalid move
        System.out.println("Invalid move!");
    }
}


    public boolean isValidMove(int startRow, int startCol, int endRow, int endCol, int orientation) {
        // Check if the move is within the bounds of the board
        if (startRow < 0 || startCol < 0 || endRow >= getRows() || endCol >= getCols()) {
            return false;
        }

        // Check if the positions are empty
        for (int i = startRow; i <= endRow; i++) {
            for (int j = startCol; j <= endCol; j++) {
                if (board[i][j] != Domineering.EMPTY) {
                    return false;
                }
            }
        }

        // Check if the move is valid based on the orientation
        if (orientation == Domineering.HORIZONTAL && startRow == endRow && startCol + 1 == endCol) {
            return true;
        } else if (orientation == Domineering.VERTICAL && startRow + 1 == endRow && startCol == endCol) {
            return true;
        }

        return false;
    }
    
    // Implementation of the clone method
    @Override
    public DomineeringPosition clone() {
        DomineeringPosition clonedPosition = new DomineeringPosition(getRows(), getCols());

        for (int i = 0; i < getRows(); i++) {
            clonedPosition.getBoard()[i] = board[i].clone();
        }

        return clonedPosition;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        int[][] board = getBoard();

        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                sb.append(board[i][j]).append(" ");
            }
            sb.append("\n");
        }

        return sb.toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        DomineeringPosition other = (DomineeringPosition) obj;

        // Compare only the board configurations
        return Arrays.deepEquals(board, other.board);
    }

    @Override
    public int hashCode() {
        return Arrays.deepHashCode(board);
    }

}

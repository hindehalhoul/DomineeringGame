package domi.game;

import java.util.Arrays;

public class DomineeringPosition extends Position implements Cloneable {

    private int[][] board;

    public DomineeringPosition(int rows, int cols) {
        board = new int[rows][cols];
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

//    @Override
//    public boolean equals(Object obj) {
//        if (this == obj) {
//            return true;
//        }
//        if (obj == null || getClass() != obj.getClass()) {
//            return false;
//        }
//        DomineeringPosition other = (DomineeringPosition) obj;
//        return Arrays.deepEquals(board, other.board);
//    }
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

package domi.game;

public class DomineeringPosition extends Position implements Cloneable {
    int[][] board;

    public DomineeringPosition(int rows, int cols) {
        board = new int[rows][cols];
    }

    public int[][] getBoard() {
        return board;
    }

    // Ajout des méthodes getRows et getCols
    public int getRows() {
        return board.length;
    }

    public int getCols() {
        return board[0].length;
    }

    // Implémentation de la méthode clone
    @Override
    public DomineeringPosition clone() {
        DomineeringPosition clonedPosition = new DomineeringPosition(getRows(), getCols());

        for (int i = 0; i < getRows(); i++) {
            clonedPosition.getBoard()[i] = board[i].clone();
        }

        return clonedPosition;
    }
}

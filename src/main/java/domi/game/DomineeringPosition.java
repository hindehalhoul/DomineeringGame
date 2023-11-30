package domi.game;

import java.util.Arrays;

public class DomineeringPosition extends Position implements Cloneable {

    int[][] board;

    // Constructeur pour initialiser le plateau de jeu avec le nombre de lignes et de colonnes spécifié
    public DomineeringPosition(int rows, int cols) {
        board = new int[rows][cols];
    }

    // Constructeur pour créer une nouvelle instance en copiant une position existante
    public DomineeringPosition(DomineeringPosition existingPosition) {
        int rows = existingPosition.getRows();
        int cols = existingPosition.getCols();
        this.board = new int[rows][cols];

        for (int i = 0; i < rows; i++) {
            this.board[i] = existingPosition.getBoard()[i].clone();
        }
    }

    // Méthode pour obtenir le plateau de jeu
    public int[][] getBoard() {
        return board;
    }

    // Méthode pour obtenir le nombre de lignes du plateau de jeu
    public int getRows() {
        return board.length;
    }

    // Méthode pour obtenir le nombre de colonnes du plateau de jeu
    public int getCols() {
        return board[0].length;
    }

    // Méthode pour placer un domino sur le plateau de jeu
    public void placeDomino(int startRow, int startCol, int endRow, int endCol, int orientation, boolean player) {
        if (isValidMove(startRow, startCol, endRow, endCol, orientation)) {
            int marker = (player) ? Domineering.HORIZONTAL : Domineering.VERTICAL;

            for (int i = startRow; i <= endRow && i < getRows(); i++) {
                for (int j = startCol; j <= endCol && j < getCols(); j++) {
                    board[i][j] = marker;
                }
            }
        } else {
            System.out.println("Mouvement invalide !");
        }
    }

    // Méthode pour vérifier la validité d'un mouvement
    public boolean isValidMove(int startRow, int startCol, int endRow, int endCol, int orientation) {
        if (startRow < 0 || startCol < 0 || endRow >= getRows() || endCol >= getCols()) {
            return false;
        }

        for (int i = startRow; i <= endRow; i++) {
            for (int j = startCol; j <= endCol; j++) {
                if (board[i][j] != Domineering.EMPTY) {
                    return false;
                }
            }
        }

        if (orientation == Domineering.HORIZONTAL && startRow == endRow && startCol + 1 == endCol) {
            return true;
        } else if (orientation == Domineering.VERTICAL && startRow + 1 == endRow && startCol == endCol) {
            return true;
        }

        return false;
    }

    // Méthode pour cloner la position actuelle
    @Override
    public DomineeringPosition clone() {
        DomineeringPosition clonedPosition = new DomineeringPosition(getRows(), getCols());

        for (int i = 0; i < getRows(); i++) {
            clonedPosition.getBoard()[i] = board[i].clone();
        }

        return clonedPosition;
    }

    // Méthode pour générer une représentation textuelle du plateau de jeu
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

    // Méthode pour comparer deux positions et déterminer leur égalité
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        DomineeringPosition other = (DomineeringPosition) obj;

        return Arrays.deepEquals(board, other.board);
    }

    // Méthode pour générer un code de hachage basé sur le contenu du plateau de jeu
    @Override
    public int hashCode() {
        return Arrays.deepHashCode(board);
    }
}

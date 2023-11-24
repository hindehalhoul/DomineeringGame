package domi.game;

import java.util.Scanner;
import java.util.Vector;
import javax.swing.*;
import java.awt.*;

public class DomineeGame extends GameSearch {
     private JPanel chessboardPanel;  // Ajoutez cette ligne

    public JPanel getChessboardPanel() {
        return chessboardPanel;
    }

   @Override
public boolean drawnPosition(Position p) {
    DomineeringPosition pos = (DomineeringPosition) p;
    int[][] board = pos.getBoard();

    // Parcourir le plateau pour vérifier s'il y a encore des mouvements possibles
    for (int i = 0; i < board.length - 1; i++) {
        for (int j = 0; j < board[i].length; j++) {
            if (board[i][j] == 0 && board[i + 1][j] == 0) {
                return false; // Il y a encore des mouvements possibles
            }
        }
    }
    return true; // Aucun mouvement possible, la partie est un match nul
}
   @Override
public boolean wonPosition(Position p, boolean player) {
    DomineeringPosition pos = (DomineeringPosition) p;
    int[][] board = pos.getBoard();

    // Vérifier si le joueur actuel ne peut pas effectuer de mouvement valide
    for (int i = 0; i < board.length - 1; i++) {
        for (int j = 0; j < board[i].length; j++) {
            if (board[i][j] == 0 && board[i + 1][j] == 0) {
                // Il y a un mouvement valide, la partie continue
                return false;
            }
        }
    }

    // Aucun mouvement valide pour le joueur actuel, l'autre joueur gagne
    return true;
}


    @Override
    public float positionEvaluation(Position p, boolean player) {
        // Évaluation de la position
        return 0.0f;
    }

  @Override
    public void printPosition(Position p) {
        // Affichage de la position
        if (p instanceof DomineeringPosition) {
            DomineeringPosition domineeringPosition = (DomineeringPosition) p;
            int[][] board = domineeringPosition.getBoard();

            for (int i = 0; i < board.length; i++) {
                for (int j = 0; j < board[i].length; j++) {
                    System.out.print(board[i][j] + " ");
                }
                System.out.println();
            }
        }
    }

    @Override
public Position[] possibleMoves(Position p, boolean player) {
    if (p instanceof DomineeringPosition) {
        DomineeringPosition domineeringPosition = (DomineeringPosition) p;
        int rows = domineeringPosition.getRows();
        int cols = domineeringPosition.getCols();
        int count = 0;

        // Compter le nombre de mouvements possibles
        for (int i = 0; i < rows - 1; i++) {
            for (int j = 0; j < cols; j++) {
                if (domineeringPosition.getBoard()[i][j] == 0 && domineeringPosition.getBoard()[i + 1][j] == 0) {
                    count++;
                }
            }
        }

        // Si aucun mouvement possible, retourner un tableau vide
        if (count == 0) {
            return new Position[0];
        }

        // Créer un tableau de positions pour les mouvements possibles
        Position[] ret = new Position[count];
        count = 0;

        // Générer les positions pour les mouvements possibles
        for (int i = 0; i < rows - 1; i++) {
            for (int j = 0; j < cols; j++) {
                if (domineeringPosition.getBoard()[i][j] == 0 && domineeringPosition.getBoard()[i + 1][j] == 0) {
                    DomineeringPosition pos2 = new DomineeringPosition(rows, cols);
                    // Copier le tableau
                    for (int k = 0; k < rows; k++) {
                        pos2.getBoard()[k] = domineeringPosition.getBoard()[k].clone();
                    }
                    // Mettre à jour la nouvelle position
                    if (player) {
                        pos2.getBoard()[i][j] = 1;
                        pos2.getBoard()[i + 1][j] = 1;
                    } else {
                        pos2.getBoard()[i][j] = -1;
                        pos2.getBoard()[i + 1][j] = -1;
                    }
                    ret[count++] = pos2;
                }
            }
        }
        return ret;
    }
    return new Position[0];
}






     @Override
public Position makeMove(Position p, boolean player, Move move) {
    if (p instanceof DomineeringPosition && move instanceof DomineeringMove) {
        DomineeringPosition domineeringPosition = (DomineeringPosition) p;
        DomineeringMove domineeringMove = (DomineeringMove) move;

        // Assurez-vous que le mouvement est valide avant de mettre à jour la position
        if (isValidMove(domineeringPosition, domineeringMove)) {
            DomineeringPosition newPos = domineeringPosition.clone();

            // Mettre à jour la nouvelle position en fonction de l'orientation
            if (domineeringMove.orientation.equalsIgnoreCase("V")) {
                newPos.getBoard()[domineeringMove.row][domineeringMove.col] = player ? 1 : -1;
                newPos.getBoard()[domineeringMove.row + 1][domineeringMove.col] = player ? 1 : -1;
            } else if (domineeringMove.orientation.equalsIgnoreCase("H")) {
                newPos.getBoard()[domineeringMove.row][domineeringMove.col] = player ? 1 : -1;
                newPos.getBoard()[domineeringMove.row][domineeringMove.col + 1] = player ? 1 : -1;
            }

            return newPos;
        }
    }
    return null;
}

    private boolean isValidMove(DomineeringPosition position, DomineeringMove move) {
        // Ajoutez ici la logique pour vérifier si le mouvement est valide
        // Par exemple, assurez-vous que les cases sont vides avant de placer une pièce.
        int row = move.row;
        int col = move.col;

        if (row < 0 || row >= position.getRows() - 1 || col < 0 || col >= position.getCols()) {
            return false; // Mouvement en dehors des limites
        }

        if (position.getBoard()[row][col] != 0 || position.getBoard()[row + 1][col] != 0) {
            return false; // Cases déjà occupées
        }

        return true;
    }

   @Override
public boolean reachedMaxDepth(Position p, int depth) {
    // Vérification de la profondeur maximale
    // Tu peux ajuster la condition en fonction de la logique spécifique au jeu

    // Dans l'exemple ci-dessous, on limite la profondeur à 10
    if (depth >= 10) {
        return true;
    }

    // Tu peux également ajouter d'autres conditions spécifiques au jeu si nécessaire
    // Par exemple, si la partie est déjà gagnée, tu pourrais vouloir limiter la recherche

    return false;
}


 @Override
public Move createMove() {
    // Saisie du mouvement par le joueur humain
    Scanner scanner = new Scanner(System.in);
    System.out.println("Enter row, column, and orientation for your move (e.g., 0 1 H):");
    int row = scanner.nextInt();
    int col = scanner.nextInt();
    String orientation = scanner.next();

    // Vérifie si l'orientation est valide (H pour horizontal, V pour vertical)
    if (!orientation.equalsIgnoreCase("H") && !orientation.equalsIgnoreCase("V")) {
        System.out.println("Invalid orientation. Please enter 'H' for horizontal or 'V' for vertical.");
        return createMove(); // Demande une nouvelle saisie
    }

    return new DomineeringMove(row, col, orientation);
}


    public static void main(String[] args) {
        DomineeringPosition startingPosition = new DomineeringPosition(8, 8);
        DomineeGame domineeGame = new DomineeGame();
        domineeGame.playGame(startingPosition, true);
    }
}

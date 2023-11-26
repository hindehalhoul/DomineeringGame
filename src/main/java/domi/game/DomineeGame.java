package domi.game;

import java.util.*;
import javax.swing.*;
import java.awt.*;

public class DomineeGame extends GameSearch {

    private JPanel chessboardPanel;

    public JPanel getChessboardPanel() {
        return chessboardPanel;
    }

    public DomineeGame() {
    }

    @Override
    public boolean drawnPosition(Position p) {
        if (p == null || !(p instanceof DomineeringPosition)) {
            return false; // Handle the case where the position is null or not of the expected type
        }

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
        if (p == null) {
            return false;  // Handle the case where the position is null
        }

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
        return true;
    }

    @Override
    public float positionEvaluation(Position p, boolean player) {
        if (p instanceof DomineeringPosition) {
            DomineeringPosition position = (DomineeringPosition) p;
            int[][] board = position.getBoard();

            // You might want to customize this based on your specific game rules
            // Simple evaluation: count the number of horizontal and vertical pieces for each player
            int playerScore = 0;
            int opponentScore = 0;

            for (int i = 0; i < position.getRows(); i++) {
                for (int j = 0; j < position.getCols(); j++) {
                    if (board[i][j] == 1) {
                        playerScore++;
                    } else if (board[i][j] == -1) {
                        opponentScore++;
                    }
                }
            }

            // Return the difference in scores
            return (player ? 1 : -1) * (playerScore - opponentScore);
        }

        // Default evaluation if the position is not a DomineeringPosition
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
        if (depth >= 5) {
            return true;
        }
        return false;
    }

////// new fct
    @Override
public Move getBestMove(Position p, boolean player) {
    Vector<Object> v = alphaBeta(0, p, player);

    Position[] moves = possibleMoves(p, player);

    System.out.println("Possible moves:");
    for (Position move : moves) {
        float eval = positionEvaluation(move, player);
        System.out.println("Move: \n" + move.toString() + ", Evaluation: " + eval);
    }

    if (v != null && v.size() > 1) {
        Position bestMove = (Position) v.elementAt(1);

        if (bestMove != null) {
            System.out.println("Best move: \n" + bestMove.toString());

            // Check if the best move is in the list of possible moves
            boolean isValidBestMove = false;
            for (Position move : moves) {
                if (move != null && move.equals(bestMove)) {
                    isValidBestMove = true;
                    break;
                }
            }

            if (!isValidBestMove) {
                System.out.println("Invalid best move.");
                return null;
            }

            // Check if the best move is null before creating a DomineeringMove
            if (bestMove instanceof DomineeringPosition) {
                // Find the corresponding DomineeringMove
                for (Position move : moves) {
                    if (move != null && move.equals(bestMove)) {
                        return new DomineeringMove((DomineeringPosition) move);
                    }
                }
            }
        } else {
            System.out.println("Best move is null.");
        }
    }

    return null;
}




    @Override
    public boolean gameOver(Position p) {
        return wonPosition(p, true) || wonPosition(p, false) || drawnPosition(p);
    }

//////////////
    @Override
    public Move createMove() {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("Enter row, column, and orientation for your move (e.g., 0 1 (V for human)):");
            int row = scanner.nextInt();
            int col = scanner.nextInt();
            String orientation = scanner.next();

            if (isValidOrientation(orientation)) {
                return new DomineeringMove(row, col, orientation);
            } else {
                System.out.println("Invalid orientation. Please enter 'H' for horizontal or 'V' for vertical.");
            }
        }
    }

    private boolean isValidOrientation(String orientation) {
        return orientation.equalsIgnoreCase("H") || orientation.equalsIgnoreCase("V");
    }

    public static void main(String[] args) {
        DomineeringPosition startingPosition = new DomineeringPosition(8, 8);
        DomineeGame domineeGame = new DomineeGame();

        int moveNumber = 1;
        while (true) {
            System.out.println("Move " + moveNumber + ":");
            domineeGame.printPosition(startingPosition);

            Move move;
            if (moveNumber % 2 == 1) {
                System.out.println("Human player's turn.");
                move = domineeGame.createMove();
            } else {
                System.out.println("Computer player's turn.");
                move = domineeGame.getBestMove(startingPosition, false);
                if (move != null) {
    System.out.println("Computer move: " + move.toString()); // Add this line to print the computer's move
} else {
    System.out.println("Computer move is null. MAIN PRINT");
}
System.out.println("Arrived here");

                System.out.println("Arrived here");
            }

            if (move == null) {
                System.out.println("Invalid move. Please try again.");
                break;
            }

            startingPosition = (DomineeringPosition) domineeGame.makeMove(startingPosition, true, move);
            moveNumber++;

            if (domineeGame.gameOver(startingPosition)) {
                System.out.println("Game over!");
                break;
            }
        }
    }
}

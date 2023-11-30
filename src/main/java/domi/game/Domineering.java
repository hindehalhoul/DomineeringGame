package domi.game;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;
import javax.swing.JButton;

public class Domineering extends GameSearch {

    public static final int EMPTY = 0;
    public static final int HORIZONTAL = 1;
    public static final int VERTICAL = 2;
    public boolean isComputerTurn = false; // computerTurn = 0;
    public DomineeringPosition currentPosition;

    public Domineering() {
    }

    @Override
    public boolean wonPosition(Position p, boolean player) {
        // Vérifie si le joueur a gagné la position actuelle
        DomineeringPosition pos = (DomineeringPosition) p;
        int marker = (player) ? HORIZONTAL : VERTICAL;

        for (int i = 0; i < pos.getRows(); i++) {
            for (int j = 0; j < pos.getCols(); j++) {
                if (pos.getBoard()[i][j] == marker) {
                    if (pos.isValidMove(i, j, i, j + 1, HORIZONTAL)) {
                        return true;
                    }

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
        // Évalue la position actuelle en fonction du nombre de mouvements possibles
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
        // Affiche la position actuelle sur la console
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
        // Calcule et retourne les mouvements possibles pour un joueur donné
        DomineeringPosition pos = (DomineeringPosition) p;
        List<Position> moves = new ArrayList<>();

        for (int row = 0; row < pos.getRows() - 1; row++) {
            for (int col = 0; col < pos.getCols(); col++) {
                if (!player && pos.isValidMove(row, col, row + 1, col, VERTICAL)) {
                    DomineeringPosition newPos = new DomineeringPosition(pos);
                    newPos.placeDomino(row, col, row + 1, col, VERTICAL, player);
                    moves.add(newPos);
                }
                if (player && col < pos.getCols() - 1 && pos.getBoard()[row][col] != VERTICAL
                        && pos.getBoard()[row][col + 1] != VERTICAL
                        && pos.isValidMove(row, col, row, col + 1, HORIZONTAL)) {
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
        // Effectue un mouvement sur la position actuelle
        isComputerTurn = !player;
        DomineeringPosition pos = (DomineeringPosition) p;
        DomineeringMove domMove = (DomineeringMove) move;

        DomineeringPosition newPos = new DomineeringPosition(pos);
        newPos.placeDomino(domMove.startRow, domMove.startCol, domMove.endRow, domMove.endCol, domMove.orientation,
                player);

        return newPos;
    }

    public boolean reachedMaxDepth(Position p, int depth) {
        // Vérifie si la profondeur maximale a été atteinte lors de la recherche
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
        return isComputerTurn;
    }

    public void resetTurn() {
        isComputerTurn = false;
    }

    public Position getPosition() {
        return currentPosition;
    }

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
        // Crée un mouvement en demandant à l'utilisateur de saisir les coordonnées
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter move (startRow startCol): ");
        int startRow = scanner.nextInt();
        int startCol = scanner.nextInt();
        int endRow, endCol, orientation;

        if (isComputerTurn()) {
            // Joueur ordinateur, orientation définie sur VERTICAL
            endRow = startRow + 1;
            endCol = startCol;
            orientation = VERTICAL;
        } else {
            // Joueur humain, orientation définie sur HORIZONTAL
            endRow = startRow;
            endCol = startCol + 1;
            orientation = HORIZONTAL;
        }
        DomineeringMove move = new DomineeringMove(startRow, startCol, endRow, endCol, orientation);

        return move;
    }

    public static void main(String[] args) {
        // Initialisation du jeu Domineering et lancement d'une partie
        DomineeringPosition p = new DomineeringPosition(8, 8);
        Domineering domineering = new Domineering();
        domineering.currentPosition = p;

        domineering.isComputerTurn = false;
        domineering.playGame(p, true);
        domineering.resetTurn();
    }
}

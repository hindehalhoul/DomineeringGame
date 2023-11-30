package domi.game;

import java.util.*;
import javax.swing.JButton;

// Classe abstraite représentant la recherche de jeu
public abstract class GameSearch {

    // Constantes pour le débogage et les joueurs
    public static final boolean DEBUG = false;
    public static boolean PROGRAM = false;
    public static boolean HUMAN = true;

    // Méthodes abstraites pour évaluer une position gagnante, évaluer une position,
    // afficher une position, générer les mouvements possibles, effectuer un mouvement,
    // déterminer si une profondeur maximale a été atteinte, créer un mouvement,
    // et vérifier si le jeu est terminé par une égalité.
    public abstract boolean wonPosition(Position p, boolean player);

    public abstract float positionEvaluation(Position p, boolean player);

    public abstract void printPosition(Position p);

    public abstract Position[] possibleMoves(Position p, boolean player);

    public abstract Position makeMove(Position p, boolean player, Move move);

    public abstract boolean reachedMaxDepth(Position p, int depth);

    public abstract Move createMove();

    // Algorithme Alpha-Bêta principal
    protected Vector alphaBeta(int depth, Position p, boolean player) {
        Vector v = alphaBetaHelper(depth, p, player, 1000000.0f, -1000000.0f);
        return v;
    }

    // Fonction d'assistance pour l'algorithme Alpha-Bêta
    protected Vector alphaBetaHelper(int depth, Position p,
            boolean player, float alpha, float beta) {
        if (GameSearch.DEBUG) {
            System.out.println("alphaBetaHelper(" + depth + "," + p + "," + alpha + "," + beta + ")");
        }

        // Si la profondeur maximale est atteinte, évalue la position et retourne le résultat
        if (reachedMaxDepth(p, depth)) {
            Vector v = new Vector(2);
            float value = positionEvaluation(p, player);
            v.addElement(new Float(value));
            v.addElement(null);
            if (GameSearch.DEBUG) {
                System.out.println(" alphaBetaHelper: mx depth at " + depth
                        + ", value=" + value);
            }
            return v;
        }

        Vector best = new Vector();
        Position[] moves = possibleMoves(p, player);

        // Parcourt les mouvements possibles
        for (int i = 0; i < moves.length; i++) {
            // Appelle récursivement alphaBetaHelper pour les mouvements suivants
            Vector v2 = alphaBetaHelper(depth + 1, moves[i], !player, -beta, -alpha);
            float value = -((Float) v2.elementAt(0)).floatValue();

            // Met à jour les valeurs alpha et beta
            if (value > beta) {
                if (GameSearch.DEBUG) {
                    System.out.println(" ! ! ! value=" + value + ", beta=" + beta);
                }
                beta = value;
                best = new Vector();
                best.addElement(moves[i]);
                Enumeration enum2 = v2.elements();
                enum2.nextElement();
                while (enum2.hasMoreElements()) {
                    Object o = enum2.nextElement();
                    if (o != null) {
                        best.addElement(o);
                    }
                }
            }
            // Coupe si beta est plus grand ou égal à alpha
            if (beta >= alpha) {
                break;
            }
        }

        // Construit et retourne le vecteur résultant
        Vector v3 = new Vector();
        v3.addElement(new Float(beta));
        Enumeration enum2 = best.elements();
        while (enum2.hasMoreElements()) {
            v3.addElement(enum2.nextElement());
        }
        return v3;
    }

    // Méthode principale pour jouer au jeu
    public void playGame(Position startingPosition, boolean humanPlayFirst) {
        // Si l'ordinateur commence, utilise Alpha-Bêta pour choisir le premier mouvement
        if (!humanPlayFirst) {
            Vector v = alphaBeta(0, startingPosition, PROGRAM);
            if (v.size() > 1) {
                startingPosition = (Position) v.elementAt(1);
            } else {
                System.out.println("Erreur : Pas d'élément à l'indice 1 dans le vecteur v");
                return;
            }
        }

        // Boucle principale du jeu
        while (true) {
            printPosition(startingPosition);

            // Vérifie si l'ordinateur ou l'humain a gagné
            if (wonPosition(startingPosition, PROGRAM)) {
                System.out.println("Program won");
                break;
            }

            if (wonPosition(startingPosition, HUMAN)) {
                System.out.println("Human won");
                break;
            }

            // Demande à l'utilisateur de faire un mouvement
            System.out.print("\nYour move:");
            Position[] humanMoves = possibleMoves(startingPosition, HUMAN);
            if (humanMoves.length == 0) {
                System.out.println("Computer won");
                break;
            }
            Move move = createMove();
            startingPosition = makeMove(startingPosition, HUMAN, move);
            printPosition(startingPosition);

            // Vérifie si l'humain a gagné
            if (wonPosition(startingPosition, HUMAN)) {
                System.out.println("Human won");
                break;
            }

            // Vérifie si le jeu est terminé par une égalité
            if (isGameDrawn(startingPosition)) {
                System.out.println("Drawn game");
                break;
            }

            // L'ordinateur choisit son mouvement en utilisant Alpha-Bêta
            System.out.print("\nComputer's move: \n");
            Vector v = alphaBeta(0, startingPosition, PROGRAM);
            if (v.size() > 1) {
                startingPosition = (Position) v.elementAt(1);
            } else {
                System.out.println("Human won");
                break;
            }
        }
    }

    // Vérifie si le jeu est terminé par une égalité
    private boolean isGameDrawn(Position p) {
        Position[] computerMoves = possibleMoves(p, PROGRAM);
        Position[] humanMoves = possibleMoves(p, HUMAN);
        return computerMoves.length == 0 || humanMoves.length == 0;
    }
}

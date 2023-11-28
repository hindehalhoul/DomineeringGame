package domi.game;

import java.util.*;

public abstract class GameSearch {

    public static final boolean DEBUG = false;

    /*
     * Note: the abstract Position also needs to be
     *       subclassed to write a new game program.
     */
 /*
     * Note: the abstract class Move also needs to be subclassed.
     *       
     */
    public static boolean PROGRAM = false;
    public static boolean HUMAN = true;

    /**
     * Notes: PROGRAM false -1, HUMAN true 1
     */

    /*
     * Abstract methods:
     */
//    public abstract boolean drawnPosition(Position p);

    public abstract boolean wonPosition(Position p, boolean player);

    public abstract float positionEvaluation(Position p, boolean player);

    public abstract void printPosition(Position p);

    public abstract Position[] possibleMoves(Position p, boolean player);

    public abstract Position makeMove(Position p, boolean player, Move move);

    public abstract boolean reachedMaxDepth(Position p, int depth);

    public abstract Move createMove();


    /*
     * Search utility methods:
     */
    protected Vector alphaBeta(int depth, Position p, boolean player) {
        Vector v = alphaBetaHelper(depth, p, player, 1000000.0f, -1000000.0f);
        return v;
    }
    
    
    
    protected Vector alphaBetaHelper(int depth, Position p,
                                     boolean player, float alpha, float beta) {
        if (GameSearch.DEBUG) System.out.println("alphaBetaHelper("+depth+","+p+","+alpha+","+beta+")");
        if (reachedMaxDepth(p, depth)) {
            Vector v = new Vector(2);
            float value = positionEvaluation(p, player);
            v.addElement(new Float(value));
            v.addElement(null);
            if(GameSearch.DEBUG) {
                System.out.println(" alphaBetaHelper: mx depth at " + depth+
                                   ", value="+value);
            }
            return v;
        }
        Vector best = new Vector();
        Position [] moves = possibleMoves(p, player);
        for (int i=0; i<moves.length; i++) {
            Vector v2 = alphaBetaHelper(depth + 1, moves[i], !player, -beta, -alpha);
            //  if (v2 == null || v2.size() < 1) continue;
            float value = -((Float)v2.elementAt(0)).floatValue();
            if (value > beta) {
                if(GameSearch.DEBUG) System.out.println(" ! ! ! value="+value+", beta="+beta);
                beta = value;
                best = new Vector();
                best.addElement(moves[i]);
                Enumeration enum2 = v2.elements();
                enum2.nextElement(); // skip previous value
                while (enum2.hasMoreElements()) {
                    Object o = enum2.nextElement();
                    if (o != null) best.addElement(o);
                }
            }
            if (beta >= alpha) {
                break;
            }
        }
        Vector v3 = new Vector();
        v3.addElement(new Float(beta));
        Enumeration enum2 = best.elements();
        while (enum2.hasMoreElements()) {
            v3.addElement(enum2.nextElement());
        }
        return v3;
    }


    
    public void playGame(Position startingPosition, boolean humanPlayFirst) {
    if (!humanPlayFirst) {
        Vector v = alphaBeta(0, startingPosition, PROGRAM);
        if (v.size() > 1) {
            startingPosition = (Position) v.elementAt(1);
        } else {
            System.out.println("Erreur : Pas d'élément à l'indice 1 dans le vecteur v");
            return;
        }
    }

    while (true) {
        printPosition(startingPosition);

        if (wonPosition(startingPosition, PROGRAM)) {
            System.out.println("Program won");
            break;
        }

        if (wonPosition(startingPosition, HUMAN)) {
            System.out.println("Human won");
            break;
        }

        System.out.print("\nYour move:");
        // Check if there are available moves for the human player
        Position[] humanMoves = possibleMoves(startingPosition, HUMAN);
        if (humanMoves.length == 0) {
            System.out.println("No more available moves for the human player. Game over.");
            break;
        }
        Move move = createMove();
        startingPosition = makeMove(startingPosition, HUMAN, move);
        printPosition(startingPosition);

        // Check if the game is over after the human move
        if (wonPosition(startingPosition, HUMAN)) {
            System.out.println("Human won");
            break;
        }

        // Check if the game is drawn
        if (isGameDrawn(startingPosition)) {
            System.out.println("Drawn game");
            break;
        }

        System.out.print("\nComputer's move: \n");
        Vector v = alphaBeta(0, startingPosition, PROGRAM);
        if (v.size() > 1) {
            startingPosition = (Position) v.elementAt(1);
            printPosition(startingPosition); // Print computer's move
        } else {
            System.out.println("Drawn game");
            break;
        }
    }
}

// Add a method to check if the game is drawn
private boolean isGameDrawn(Position p) {
    Position[] computerMoves = possibleMoves(p, PROGRAM);
    Position[] humanMoves = possibleMoves(p, HUMAN);
    return computerMoves.length == 0 || humanMoves.length == 0;
}

    
    
    
}
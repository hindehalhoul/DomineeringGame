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
    public abstract boolean drawnPosition(Position p);

    public abstract boolean wonPosition(Position p, boolean player);

    public abstract float positionEvaluation(Position p, boolean player);

    public abstract void printPosition(Position p);

    public abstract Position[] possibleMoves(Position p, boolean player);

    public abstract Position makeMove(Position p, boolean player, Move move);

    public abstract boolean reachedMaxDepth(Position p, int depth);

    public abstract Move createMove();

    // new
    public abstract Move getBestMove(Position p, boolean player);

    public abstract boolean gameOver(Position p);

    /*
     * Search utility methods:
     */
//    protected Vector alphaBeta(int depth, Position p, boolean player) {
//        Vector v = alphaBetaHelper(depth, p, player, 1000000.0f, -1000000.0f);
//        //System.out.println("^^ v(0): " + v.elementAt(0) + ", v(1): " + v.elementAt(1));
//        return v;
//    }
    protected Vector alphaBeta(int depth, Position p, boolean player) {
        Vector v = alphaBetaHelper(depth, p, player, 1000000.0f, -1000000.0f);
        //System.out.println("^^ v(0): " + v.elementAt(0) + ", v(1): " + v.elementAt(1));
        return v;
    }
    
    
    
    protected Vector alphaBetaHelper(int depth, Position p, boolean player, float alpha, float beta) {
    if (GameSearch.DEBUG) {
        System.out.println("alphaBetaHelper(" + depth + "," + p + "," + alpha + "," + beta + ")");
    }
    if (reachedMaxDepth(p, depth)) {
        Vector v = new Vector(2);
        float value = positionEvaluation(p, player);
        v.addElement(new Float(value));
        v.addElement(null);
        if (GameSearch.DEBUG) {
            System.out.println(" alphaBetaHelper: mx depth at " + depth + ", value=" + value);
        }
        return v;
    }

    Position[] moves = possibleMoves(p, player);
    Vector best = new Vector();
    if (moves.length > 0) {
        best.addElement(moves[0]);
        Vector v2 = alphaBetaHelper(depth + 1, moves[0], !player, -beta, -alpha);
        float value = -((Float) v2.elementAt(0)).floatValue();
        beta = value;
    }

    for (int i = 1; i < moves.length; i++) {
        Vector v2 = alphaBetaHelper(depth + 1, moves[i], !player, -beta, -alpha);
        float value = -((Float) v2.elementAt(0)).floatValue();
        if (value > beta) {
            if (GameSearch.DEBUG) {
                System.out.println(" ! ! ! value=" + value + ", beta=" + beta + ", move=" + moves[i]);
            }
            beta = value;
            best.set(0, moves[i]);
//            best = new Vector();
//            best.addElement(moves[i]);
            Enumeration enum2 = v2.elements();
            enum2.nextElement(); // skip previous value
            while (enum2.hasMoreElements()) {
                Object o = enum2.nextElement();
                if (o != null) {
                    best.addElement(o);
                }
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
        if (humanPlayFirst == false) {
            Vector v = alphaBeta(0, startingPosition, PROGRAM);
            startingPosition = (Position) v.elementAt(1);
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
            if (drawnPosition(startingPosition)) {
                System.out.println("Drawn game");
                break;
            }
            System.out.print("\nYour move:");
            Move move = createMove();
            startingPosition = makeMove(startingPosition, HUMAN, move);
            printPosition(startingPosition);
            if (wonPosition(startingPosition, HUMAN)) {
                System.out.println("Human won");
                break;
            }
            Vector v = alphaBeta(0, startingPosition, PROGRAM);
            Enumeration enum2 = v.elements();
            while (enum2.hasMoreElements()) {
                System.out.println(" next element: " + enum2.nextElement());
            }
            startingPosition = (Position) v.elementAt(1);
            if (startingPosition == null) {
                System.out.println("Drawn game");
                break;
            }
        }
    }
}
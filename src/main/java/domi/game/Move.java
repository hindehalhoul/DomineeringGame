/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package domi.game;

/**
 *
 * @author ASUS
 */
import java.util.ArrayList;
import java.util.List;

abstract public class Move {
    // Déclarations existantes de la classe Move

    // Nouvelles déclarations pour gérer les mouvements
    private List<Move> moves = new ArrayList<>();

    public List<Move> getMoves() {
        return moves;
    }

    public void setMoves(List<Move> moves) {
        this.moves = moves;
    }

    public void addMove(Move move) {
        this.moves.add(move);
    }
}


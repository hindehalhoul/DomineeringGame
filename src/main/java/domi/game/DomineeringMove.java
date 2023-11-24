/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package domi.game;

/**
 *
 * @author ASUS
 */
public class DomineeringMove extends Move {
    public int row;
    public int col;
    public String orientation;

    public DomineeringMove(int row, int col, String orientation) {
        this.row = row;
        this.col = col;
        this.orientation = orientation;
    }
}

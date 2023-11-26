/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package domi.game;

/**
 *
 * @author ASUS
 */
//public class DomineeringMove extends Move {
//    public int row;
//    public int col;
//    public String orientation;
//
//    public DomineeringMove(int row, int col, String orientation) {
//        this.row = row;
//        this.col = col;
//        this.orientation = orientation;
//    }
//    public DomineeringMove(DomineeringPosition position) {
//        // Extract row, col, and orientation from the DomineeringPosition or adjust as needed
//        // For example, you might use the position's state to determine these values
//        this.row = 0; // Replace with the appropriate value
//        this.col = 0; // Replace with the appropriate value
//        this.orientation = "H"; // Replace with the appropriate value
//    }
//}


public class DomineeringMove extends Move {
    public int row;
    public int col;
    public String orientation;

    public DomineeringMove(int row, int col, String orientation) {
        this.row = row;
        this.col = col;
        this.orientation = orientation;
    }

    public DomineeringMove(DomineeringPosition position) {
        // Modify this constructor to directly accept the values needed for a move
        this(0, 0, "H"); // Replace with the appropriate default values or logic
    }

    @Override
    public String toString() {
        return "Row: " + row + ", Column: " + col + ", Orientation: " + orientation;
    }
}

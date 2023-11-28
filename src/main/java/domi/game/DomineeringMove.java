
package domi.game;



public class DomineeringMove extends Move {

    public int startRow;
    public int startCol;
    public int endRow;
    public int endCol;
    public int orientation;

    public DomineeringMove(int startRow, int startCol, int endRow, int endCol, int orientation) {
        this.startRow = startRow;
        this.startCol = startCol;
        this.endRow = endRow;
        this.endCol = endCol;
        this.orientation = orientation;
    }
    
    public DomineeringMove(int startRow, int startCol, boolean isComputerTurn) {
        this.startRow = startRow;
        this.startCol = startCol;

        // Adjust other fields as needed based on isComputerTurn
        if (isComputerTurn) {
            this.endRow = startRow + 1;
            this.endCol = startCol;
            this.orientation = Domineering.VERTICAL;
        } else {
            this.endRow = startRow;
            this.endCol = startCol + 1;
            this.orientation = Domineering.HORIZONTAL;
        }
    }


    // Default constructor for creating a move based on a DomineeringPosition
    public DomineeringMove(DomineeringPosition position) {
        this(0, 0, 0, 0, 0);
        // Extract startRow, startCol, endRow, endCol, and orientation from the DomineeringPosition or adjust as needed
        // For example, you might use the position's state to determine these values
    }

    @Override
    public String toString() {
        return "StartRow: " + startRow + ", StartCol: " + startCol +
               ", EndRow: " + endRow + ", EndCol: " + endCol +
               ", Orientation: " + orientation;
    }
}

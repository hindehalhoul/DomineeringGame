// La classe DomineeringMove étend la classe Move et représente un mouvement dans le jeu Domineering.

public class DomineeringMove extends Move {

    // Attributs représentant les coordonnées de début et de fin, ainsi que l'orientation du mouvement.
    public int startRow;
    public int startCol;
    public int endRow;
    public int endCol;
    public int orientation;

    // Constructeur pour créer un mouvement avec des coordonnées de début et de fin spécifiées, ainsi qu'une orientation.
    public DomineeringMove(int startRow, int startCol, int endRow, int endCol, int orientation) {
        this.startRow = startRow;
        this.startCol = startCol;
        this.endRow = endRow;
        this.endCol = endCol;
        this.orientation = orientation;
    }

    // Constructeur pour créer un mouvement avec des coordonnées de début spécifiées (utilisé lors de l'initialisation).
    public DomineeringMove(int startRow, int startCol) {
        this.startRow = startRow;
        this.startCol = startCol;
    }

    // Constructeur pour créer un mouvement avec des coordonnées de début spécifiées et déterminer les autres champs en fonction du tour de l'ordinateur ou du joueur.
    public DomineeringMove(int startRow, int startCol, boolean isComputerTurn) {
        this.startRow = startRow;
        this.startCol = startCol;

        // Ajuste les autres champs en fonction de isComputerTurn
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

    // Constructeur par défaut pour créer un mouvement basé sur une position Domineering spécifique.
    public DomineeringMove(DomineeringPosition position) {
        this(position.getRows(), position.getRows());
    }

    // Méthode pour représenter le mouvement sous forme de chaîne de caractères.
    @Override
    public String toString() {
        return "StartRow: " + startRow + ", StartCol: " + startCol +
               ", EndRow: " + endRow + ", EndCol: " + endCol +
               ", Orientation: " + orientation;
    }
}

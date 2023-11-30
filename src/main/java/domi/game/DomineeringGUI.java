/*
 * Cette classe représente l'interface graphique du jeu Domineering.
 */
package domi.game;

import static domi.game.Domineering.HORIZONTAL;
import static domi.game.Domineering.VERTICAL;
import javax.swing.*;
import javax.swing.text.Position;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

public class DomineeringGUI extends JFrame {

    private Domineering domineering;
    private JButton[][] buttons;

    private DomineeringPosition currentPosition;

    /*
     * Constructeur de la classe DomineeringGUI.
     * Initialise l'interface graphique et le jeu Domineering.
     */
    public DomineeringGUI() {
        domineering = new Domineering();
        initializeGUI();
    }

    /*
     * Initialise l'interface graphique du jeu.
     */
    private void initializeGUI() {
        int rows = 8;
        int cols = 8;

        buttons = new JButton[rows][cols];
        JPanel boardPanel = new JPanel(new GridLayout(rows, cols));
        currentPosition = new DomineeringPosition(rows, cols);

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                buttons[i][j] = new JButton();
                buttons[i][j].setPreferredSize(new Dimension(50, 50));
                buttons[i][j].addActionListener(new ButtonClickListener(i, j));
                boardPanel.add(buttons[i][j]);
            }
        }

        add(boardPanel, BorderLayout.CENTER);

        JButton resetButton = new JButton("Réinitialiser le jeu");
        resetButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                resetGame();
            }
        });

        add(resetButton, BorderLayout.SOUTH);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    /*
     * Gestionnaire d'événements pour les boutons du jeu.
     */
    private class ButtonClickListener implements ActionListener {

        private int row;
        private int col;

        /*
         * Constructeur de la classe ButtonClickListener.
         * @param row Ligne du bouton.
         * @param col Colonne du bouton.
         */
        public ButtonClickListener(int row, int col) {
            this.row = row;
            this.col = col;
        }

        /*
         * Méthode appelée lorsqu'un bouton est cliqué.
         */
        @Override
        public void actionPerformed(ActionEvent e) {
            DomineeringPosition position = (DomineeringPosition) domineering.getPosition();
            int endRow, endCol, orientation;
            int ver = 2, hor = 1;

            if (domineering.isComputerTurn()) {
                // Joueur informatique, définir l'orientation sur VERTICAL
                endRow = row + 1;
                endCol = col;
                orientation = ver;
            } else {
                // Joueur humain, définir l'orientation sur HORIZONTAL
                endRow = row;
                endCol = col + 1;
                orientation = hor;
            }

            // Effectuer le mouvement
            if (currentPosition.isValidMove(row, col, endRow, endCol, orientation)) {
                DomineeringMove move = new DomineeringMove(row, col, domineering.isComputerTurn());
                domineering.makeMove(currentPosition, domineering.isComputerTurn(), move);

                updateGUI(row, col, orientation);

                // Vérifier si le jeu est terminé
                if (!domineering.wonPosition(currentPosition, domineering.isComputerTurn())) {
                    // Tour de l'ordinateur
                    computerMove(currentPosition);
                }
            } else {
                System.out.println("Mouvement non valide !");
            }
        }
    }

    /*
     * Gestion du mouvement de l'ordinateur.
     */
    private void computerMove(DomineeringPosition currentPosition) {
        System.out.println("Mouvement de l'ordinateur");

        Vector v = domineering.alphaBeta(0, currentPosition, !domineering.isComputerTurn());

        if (v.size() > 1) {
            DomineeringPosition pos = (DomineeringPosition) v.elementAt(1);

            System.out.println("Résultat Alpha-Bêta : " + v);
            System.out.println("Configuration du plateau :\n" + pos);

            int verticalRow = -1;
            int verticalCol = -1;

            for (int i = 0; i < pos.getRows(); i++) {
                for (int j = 0; j < pos.getCols(); j++) {
                    if (pos.getBoard()[i][j] == 2) {
                        verticalRow = i;
                        verticalCol = j;
                        break;
                    }
                }
                if (verticalRow != -1) {
                    break;
                }
            }
            System.out.println("Position du domino vertical : (" + verticalRow + ", " + verticalCol + ")");

            if (verticalRow != -1 && verticalCol != -1) {
                DomineeringMove move = new DomineeringMove(verticalRow, verticalCol, !domineering.isComputerTurn());
                System.out.println("Tentative de faire le mouvement : " + move);

                // Ajouter les déclarations de débogage suivantes
                System.out.println("Avant le mouvement :\n" + pos);

                domineering.makeMove(pos, !domineering.isComputerTurn(), move);

                System.out.println("Après le mouvement :\n" + pos);

                updateGUI(verticalRow, verticalCol, Domineering.VERTICAL);
            } else {
                System.out.println("Mouvement non valide !");
            }
        } else {
            System.out.println("Mouvement non valide !");
        }
    }

    /*
     * Met à jour l'interface graphique après un mouvement.
     */
    private void updateGUI(int row, int col, int orientation) {

        if (orientation == Domineering.HORIZONTAL) {
            buttons[row][col].setText("H");
            buttons[row][col + 1].setText("H");
            buttons[row][col].setEnabled(false);
            buttons[row][col + 1].setEnabled(false);
            currentPosition.placeDomino(row, col, row, col + 1, Domineering.HORIZONTAL, !domineering.isComputerTurn());
        } else if (orientation == Domineering.VERTICAL) {
            buttons[row][col].setText("M");
            buttons[row + 1][col].setText("M");
            buttons[row][col].setEnabled(false);
            buttons[row + 1][col].setEnabled(false);
            currentPosition.placeDomino(row, col, row + 1, col, Domineering.VERTICAL, !domineering.isComputerTurn());
        } else {
            buttons[row][col].setText("AH");
        }
    }

    /*
     * Réinitialise le jeu.
     */
    private void resetGame() {
        domineering = new Domineering();
        for (int i = 0; i < buttons.length; i++) {
            for (int j = 0; j < buttons[i].length; j++) {
                buttons[i][j].setText("");
                buttons[i][j].setEnabled(true);
            }
        }
    }

    /*
     * Méthode principale pour tester l'interface graphique du jeu Domineering.
     */
    public static void main(String[] args) {
        DomineeringPosition p = new DomineeringPosition(8, 8);
        Domineering domineering = new Domineering();
        domineering.currentPosition = p; // Définir la position initiale
        SwingUtilities.invokeLater(() -> new DomineeringGUI());
    }
}

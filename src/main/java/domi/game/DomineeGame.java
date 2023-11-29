package domi.game;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;

public class DomineeGame {

    private JPanel chessboardPanel;
    private int helpCount = 3; // Nombre d'aides disponibles
    private JButton helpButton;

    private Point helpCoordinates;

    private Point currentPoint;
    private boolean dominoPlaced;
    private List<Domino> placedDominos;
    private boolean currentDominoOrientation = true; // Par défaut, vertical

    public DomineeGame() {
        initializeChessboardPanel();
        placedDominos = new ArrayList<>();
    }

    public JPanel getChessboardPanel() {
        return chessboardPanel;
    }

    private void initializeChessboardPanel() {
        chessboardPanel = new JPanel() {
            @Override
            public void paint(Graphics g) {
                super.paint(g);
                boolean white = true;
                for (int y = 0; y < 8; y++) {
                    for (int x = 0; x < 8; x++) {
                        if (white) {
                            g.setColor(new Color(200, 200, 200));
                        } else {
                            g.setColor(new Color(100, 100, 100));
                        }
                        g.fillRect(x * 64, y * 64, 64, 64);
                        white = !white;
                    }
                    white = !white;
                }

                // Draw the placed dominos
                for (Domino domino : placedDominos) {
                    g.setColor(domino.getColor());
                    if (domino.isHorizontal()) {
                        g.fillRect(domino.getX() * 64, domino.getY() * 64, 128, 64);
                    } else {
                        g.fillRect(domino.getX() * 64, domino.getY() * 64, 64, 128);
                    }
                }

                // Draw the domino at the current mouse position with 50% opacity
                if (currentPoint != null && !dominoPlaced && currentPoint.y <= 7) {
                    int dominoWidth = currentDominoOrientation ? 128 : 64;
                    int dominoHeight = currentDominoOrientation ? 64 : 128;

                    // Check if the domino will fit within the board boundaries
                    if (currentPoint.x + (dominoWidth / 64) <= 8) {
                        if (currentDominoOrientation) {
                            g.setColor(new Color(242, 203, 255)); // 50% opacity purple
                            g.fillRect(currentPoint.x * 64, currentPoint.y * 64, dominoWidth, dominoHeight);
                        } else {
                            g.setColor(new Color(197, 228, 255)); // 50% opacity blue
                            g.fillRect(currentPoint.x * 64, currentPoint.y * 64, dominoWidth, dominoHeight);
                        }
                    }
                }
                if (helpCoordinates != null) {
                    g.setColor(Color.RED); // Couleur du contour
                    g.drawRect(helpCoordinates.x * 64, helpCoordinates.y * 64, 64, 64);
                }
            }

        };
        helpButton = new JButton("Help");
        helpButton.setFont(new Font("Segoe UI", Font.BOLD, 20));
        chessboardPanel.add(helpButton);

        helpButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (helpCount > 0) {
                    giveHelp();
                    helpCount--;
                    System.out.println("Aides restantes : " + helpCount);
                } else {
                    System.out.println("Vous n'avez plus d'aides disponibles.");
                }
            }
        });

        chessboardPanel.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                int x = e.getX() / 64;
                int y = e.getY() / 64;

                currentPoint = new Point(Math.min(Math.max(x, 0), 7), Math.min(Math.max(y, 0), 7));

                chessboardPanel.repaint();
            }
        });

        chessboardPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int dominoWidth = currentDominoOrientation ? 128 : 64;
                int dominoHeight = currentDominoOrientation ? 64 : 128;

                if (currentDominoOrientation) {
                    // Le domino est vertical
                    if (currentPoint.y <= 7 && currentPoint.x + (dominoWidth / 64) <= 8) {
                        // Code pour placer le domino vertical
                        if (!isPositionOccupied(currentPoint.x, currentPoint.y, true)
                                && !isPositionOccupiedByOtherPlayer(currentPoint.x, currentPoint.y, true)) {
                            placedDominos.add(new Domino(currentPoint.x, currentPoint.y, true, getColorForCurrentDomino()));
                            currentDominoOrientation = false;
                            chessboardPanel.repaint();
                        }
                    }
                } else {
                    // Le domino est horizontal
                    if (currentPoint.y < 7 && currentPoint.x + (dominoWidth / 64) <= 8) {
                        // Code pour placer le domino horizontal
                        if (!isPositionOccupied(currentPoint.x, currentPoint.y, false)
                                && !isPositionOccupiedByOtherPlayer(currentPoint.x, currentPoint.y, false)) {
                            placedDominos.add(new Domino(currentPoint.x, currentPoint.y, false, getColorForCurrentDomino()));
                            currentDominoOrientation = true;
                            chessboardPanel.repaint();
                        }
                    }
                }

                // Vérification pour déterminer le vainqueur
                if (!isSpaceAvailable()) {
                    if (currentDominoOrientation == false) {
                        System.out.println("Joueur 1 a vaincu !");
                    } else {
                        System.out.println("Joueur 2 a vaincu !");
                    }
                    helpCount = 3;
                }
            }
        });
    }

    //////////////////////////////////////////////////////////
    private Point getHelpCoordinates() {
        // Logique pour trouver où le joueur doit poser son domino

        // Trouver une position valide pour placer le domino
        for (int x = 0; x < 8; x++) {
            for (int y = 0; y < 8; y++) {
                // Vérifier si la position est valide pour le domino vertical
                if (!isPositionOccupied(x, y, true) && !isPositionOccupiedByOtherPlayer(x, y, true)) {
                    helpCoordinates = new Point(x, y);
                    return helpCoordinates;
                }

                // Vérifier si la position est valide pour le domino horizontal
                if (!isPositionOccupied(x, y, false) && !isPositionOccupiedByOtherPlayer(x, y, false)) {
                    helpCoordinates = new Point(x, y);
                    return helpCoordinates;
                }
            }
        }

        // Aucune position valide trouvée
        helpCoordinates = null;
        return null;
    }

    private void giveHelp() {
        Point helpCoordinates = getHelpCoordinates();

        if (helpCoordinates != null) {
            System.out.println("L'aide a été donnée. Placez votre domino à la position : ("
                    + helpCoordinates.x + ", " + helpCoordinates.y + ")");
        } else {
            System.out.println("Aucune position valide trouvée pour l'aide.");
        }
    }

//////////////////////////////////////////////////////////////////////////////////////////
    private boolean isSpaceAvailable() {
        // Vérifier s'il y a encore de la place sur le plateau
        // Tu peux utiliser la logique que tu utilises pour vérifier si une position est occupée
        // en parcourant le plateau pour voir s'il reste des places disponibles
        for (int x = 0; x < 8; x++) {
            for (int y = 0; y < 8; y++) {
                if (!isPositionOccupied(x, y, true) && !isPositionOccupiedByOtherPlayer(x, y, true)
                        && !isPositionOccupied(x, y, false) && !isPositionOccupiedByOtherPlayer(x, y, false)) {
                    return true; // Il reste de la place
                }
            }
        }
        return false; // Aucune place disponible
    }

    private boolean isPositionOccupied(int x, int y, boolean horizontal) {
        for (Domino domino : placedDominos) {
            if (domino.isHorizontal() == horizontal) {
                if (horizontal) {
                    if ((domino.getX() == x && domino.getY() == y) || (domino.getX() == x + 1 && domino.getY() == y) ) {
                        System.out.println(" HORIZONTAL");
                        return true;
                    }
                } else {
                    if ((domino.getX() == x  && domino.getY() == y) || (domino.getX() == x && domino.getY() == y + 1)  ) {
                        return true;
                    }
                }
            } else {
                if (!horizontal) {
                    if ((domino.getX() == x && domino.getY() == y) || (domino.getX() == x && domino.getY() == y + 1) ||(domino.getX() + 1 == x  && domino.getY() == y + 1) ||(domino.getX() + 1 == x  && domino.getY() == y )) {
                        return true;
                    }
                } else {
                    if ((domino.getX() == x && domino.getY() == y) || (domino.getX() == x + 1 && domino.getY() == y)|| (domino.getX() == x + 1 && domino.getY()+ 1 == y) || (domino.getX() == x  && domino.getY()+ 1 == y)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private boolean isPositionOccupiedByOtherPlayer(int x, int y, boolean horizontal) {
        for (Domino domino : placedDominos) {
            System.out.println("Checking position " + x + ", " + y + " for horizontal: " + horizontal);
            if (domino.isHorizontal() == horizontal) {
                if (horizontal) {
                    if (domino.getX() < x && x < domino.getX() + 2 && domino.getY() == y) {
                        System.out.println("Place occupied by other player HORIZONTAL");
                        return true;
                    }
                } else {
                    if (domino.getY() < y && y < domino.getY() + 2 && domino.getX() == x) {
                        System.out.println("Place occupied by other player VERTICAL");
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private Color getColorForCurrentDomino() {
        return currentDominoOrientation ? new Color(192, 0, 255) : new Color(0, 135, 255);
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame();
        frame.setBounds(10, 10, 512, 512);
        frame.setUndecorated(true);

        DomineeGame domineeGame = new DomineeGame();
        frame.add(domineeGame.getChessboardPanel());

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}

class Domino {

    private int x;
    private int y;
    private boolean horizontal;
    private Color color;

    public Domino(int x, int y, boolean horizontal, Color color) {
        this.x = x;
        this.y = y;
        this.horizontal = horizontal;
        this.color = color;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public boolean isHorizontal() {
        return horizontal;
    }

    public Color getColor() {
        return color;
    }
}

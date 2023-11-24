package domi.game;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;

public class DomineeGame {

    private JPanel chessboardPanel;
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
                if (currentPoint != null && !dominoPlaced && currentPoint.y < 7) {
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
            }
        };

        // Add a MouseMotionListener to handle mouse movement
        chessboardPanel.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                int x = e.getX() / 64;
                int y = e.getY() / 64;

                // Ensure the currentPoint is within the bounds of the board
                currentPoint = new Point(Math.min(Math.max(x, 0), 7), Math.min(Math.max(y, 0), 7));

                chessboardPanel.repaint();
            }
        });

        // Add a MouseListener to handle mouse clicks
        chessboardPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (currentPoint.y < 7) {
                    int dominoWidth = currentDominoOrientation ? 128 : 64;
                    int dominoHeight = currentDominoOrientation ? 64 : 128;

                    // Check if the domino will fit within the board boundaries
                    if (currentPoint.x + (dominoWidth / 64) <= 8) {
                        // Check if the selected position is already occupied by another domino
                        if (!isPositionOccupied(currentPoint.x, currentPoint.y)) {
                            placedDominos.add(new Domino(currentPoint.x, currentPoint.y, currentDominoOrientation, getColorForCurrentDomino()));
                            currentDominoOrientation = !currentDominoOrientation;
                            chessboardPanel.repaint();
                        }
                    }
                }
            }

            private boolean isPositionOccupied(int x, int y) {
                for (Domino domino : placedDominos) {
                    // Check for horizontal placement
                    if (domino.getX() <= x && x < domino.getX() + 1 && domino.getY() == y) {
                        System.out.println("Place occupied HORIZONTAL");
                        return true; // Position occupied
                    }
                    // Check for vertical placement
                    if (domino.getX() == x && domino.getY() <= y && y < domino.getY() + 1) {
                        System.out.println("Place occupied VERTICAL");
                        return true; // Position occupied
                    }
                }
                return false; // Position not occupied
            }

// ...
        });
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

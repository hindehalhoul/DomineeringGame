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
                    if (currentDominoOrientation) {
                        g.setColor(new Color(242, 203, 255)); // 50% opacity purple
                        g.fillRect(currentPoint.x * 64, currentPoint.y * 64, 128, 64);
                    } else {
                        g.setColor(new Color(197, 228, 255)); // 50% opacity blue
                        g.fillRect(currentPoint.x * 64, currentPoint.y * 64, 64, 128);    
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
                    // Place the domino with the current orientation
                    
                    if (currentDominoOrientation) {
                        placedDominos.add(new Domino(currentPoint.x, currentPoint.y, currentDominoOrientation, new Color(192, 0, 255)));
                    } else {
                        placedDominos.add(new Domino(currentPoint.x, currentPoint.y, currentDominoOrientation, new Color(0, 135, 255)));  
                    }

                    // Change l'orientation du domino survolé
                    currentDominoOrientation = !currentDominoOrientation;

                    chessboardPanel.repaint();
                }
            }
        });
    }

    private boolean isHorizontalDominoAt(int x, int y) {
        for (Domino domino : placedDominos) {
            if (domino.isHorizontal() && domino.getX() == x && domino.getY() == y) {
                return true;
            }
        }
        return false;
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

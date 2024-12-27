package org.cis1200.twenty48;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.*;
import java.util.*;

public class GameBoard extends JPanel {

    private Twenty48 board; // model for the game
    private JLabel scoreLabel; // current status text
    public static final int BOARD_WIDTH = 500;
    public static final int BOARD_HEIGHT = 500;

    public GameBoard(JLabel scoreLabel) {
        // creates border around the court area, JComponent method
        setBorder(BorderFactory.createLineBorder(Color.BLACK));

        // Enable keyboard focus on the court area. When this component has the
        // keyboard focus, key events are handled by its key listener.
        setFocusable(true);

        board = new Twenty48(); // initializes model for the game
        this.scoreLabel = scoreLabel; // initializes the status JLabel

        /*
         * Listens for mouseclicks. Updates the model, then updates the game
         * board based off of the updated model.
         */
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                Point p = e.getPoint();

                // updates the model given the coordinates of the mouseclick
                // ttt.playTurn(p.x / 100, p.y / 100);

                updateStatus(); // updates the status JLabel
                repaint(); // repaints the game board
            }
        });
        addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                int k = e.getKeyCode();
                switch (k) {
                    case KeyEvent.VK_UP:
                        board.up();
                        break;
                    case KeyEvent.VK_DOWN:
                        board.down();
                        break;
                    case KeyEvent.VK_LEFT:
                        board.left();
                        break;
                    case KeyEvent.VK_RIGHT:
                        board.right();
                        break;
                    default:
                        break;
                }
                // ttt.playTurn(p.x / 100, p.y / 100);
                scoreLabel.setText("Score: " + board.getScore());
                if (board.checkGameOver()) {
                    JOptionPane.showMessageDialog(
                            GameBoard.this,
                            "Game Over"
                    );
                }
                updateStatus(); // updates the status JLabel
                repaint(); // repaints the game board
            }
        });
    }

    public void save() throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("files/save.txt"))) {
            // writer.write(score + "\n");
            Stack<int[][]> stack = board.getStack();
            Stack<int[][]> temp = new Stack<>();
            while (!stack.isEmpty()) {
                int[][] e = stack.pop();
                for (int i = 0; i < 4; i++) {
                    for (int j = 0; j < 4; j++) {
                        writer.write(e[i][j] + " ");
                    }
                    writer.newLine();
                }
                temp.push(e);
            }
            while (!temp.isEmpty()) {
                int[][] e = temp.pop();
                stack.push(e);
            }
            System.out.println("Game saved successfully.");
        } catch (IOException e) {
            System.err.println("Error saving game: " + e.getMessage());
        }
        repaint();
        requestFocusInWindow();
    }

    public void load() throws IOException {
        BufferedReader br = new BufferedReader(
                new FileReader("files/save.txt")
        );
        String line;
        board.clear();
        Stack<int[][]> stack = board.getStack();
        Stack<int[][]> temp = new Stack<>();
        while ((line = br.readLine()) != null) {
            String[] values = line.split(" ");
            int[][] e = new int[4][4];
            for (int j = 0; j < 4; j++) {
                e[0][j] = Integer.parseInt(values[j]);
            }
            for (int i = 1; i < 4; i++) {
                values = br.readLine().split(" ");
                for (int j = 0; j < 4; j++) {
                    e[i][j] = Integer.parseInt(values[j]);
                }
            }
            temp.push(e);
        }
        while (!temp.isEmpty()) {
            stack.push(temp.pop());
        }
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                board.set(i, j, stack.peek()[i][j]);
            }
        }
        board.load();
        repaint(); // Refresh the GUI with the loaded state
        requestFocusInWindow();
    }

    public void reset() {
        board.reset();
        // status.setText("Player 1's Turn");
        repaint();

        // Makes sure this component has keyboard/mouse focus
        requestFocusInWindow();
    }

    public void undo() {
        board.undo();
        repaint();
        requestFocusInWindow();
    }

    private void updateStatus() {

    }
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Draws board grid
        int unitWidth = BOARD_WIDTH / 4;
        int unitHeight = BOARD_HEIGHT / 4;

        g.drawLine(unitWidth, 0, unitWidth, BOARD_HEIGHT);
        g.drawLine(unitWidth * 2, 0, unitWidth * 2, BOARD_HEIGHT);
        g.drawLine(0, unitHeight, BOARD_WIDTH, unitHeight);
        g.drawLine(0, unitHeight * 2, BOARD_WIDTH, unitHeight * 2);

        g.drawLine(unitWidth * 3, 0, unitWidth * 3, BOARD_HEIGHT);
        g.drawLine(0, unitHeight * 3, BOARD_WIDTH, unitHeight * 3);

        g.setFont(new Font("Arial", Font.BOLD, 40));
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                if (board.getCell(i, j) != 0) {
                    g.drawString(
                            Integer.toString(board.getCell(i, j)),
                            35 + 125 * i, 58 + 125 * j
                    );
                }
            }
        }
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(BOARD_WIDTH, BOARD_HEIGHT);
    }
}

package org.cis1200.twenty48;

import javax.swing.*;
import java.awt.*;
import java.io.*;
public class Run2048 implements Runnable {
    public void run() {
        // NOTE: the 'final' keyword denotes immutability even for local variables.

        // Top-level frame in which game components live
        final JFrame frame = new JFrame("2048");
        frame.setLocation(300, 300);

        // Status panel
        final JPanel score_panel = new JPanel();
        frame.add(score_panel, BorderLayout.SOUTH);
        final JLabel score = new JLabel("Score: 0");
        score_panel.add(score);

        // Game board
        final GameBoard board = new GameBoard(score);
        frame.add(board, BorderLayout.CENTER);

        // Reset button
        final JPanel control_panel = new JPanel();
        frame.add(control_panel, BorderLayout.NORTH);

        final JButton instructions = new JButton("How to");
        instructions.addActionListener(e -> showInstructions());
        control_panel.add(instructions);

        final JButton reset = new JButton("Reset");
        reset.addActionListener(e -> board.reset());
        control_panel.add(reset);

        final JButton undo = new JButton("Undo");
        undo.addActionListener(e -> board.undo());
        control_panel.add(undo);

        final JButton save = new JButton("Save");
        save.addActionListener(e -> {
            try {
                board.save();
            } catch (IOException ex) {
                System.err.println("Error saving game: " + ex.getMessage());
            }
        });
        control_panel.add(save);

        final JButton load = new JButton("Load");
        load.addActionListener(e -> {
            try {
                board.load();
            } catch (IOException ex) {
                System.err.println("Error loading game: " + ex.getMessage());
            }
        });
        control_panel.add(load);
        // Put the frame on the screen
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        // Start the game
        board.reset();
    }

    public void showInstructions() {
        JDialog instructionsDialog = new JDialog();
        instructionsDialog.setTitle("Game Instructions");

        // Create a JTextArea or JLabel to display the instructions
        JTextArea instructionsText = new JTextArea();
        instructionsText.setEditable(false); // Prevent editing
        instructionsText.setText("""
                Welcome to 2048!

                The goal of the game is to combine tiles with the same numbers
                to reach the 2048 tile. Use the following controls:

                - Arrow Keys: Move tiles in the specified direction.
                - Reset Button: Start a new game.
                - Undo Button: Undo the last move.
                - Save Button: Save your current progress.
                - Load Button: Load your previously saved game.""");
        JScrollPane scrollPane = new JScrollPane(instructionsText);
        instructionsDialog.add(scrollPane);
        instructionsDialog.setSize(400, 300);
        instructionsDialog.setLocationRelativeTo(null); // Center the window
        instructionsDialog.setVisible(true);
    }
}
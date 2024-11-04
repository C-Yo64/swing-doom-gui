package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Objects;

import static org.example.Main.sendInput;

public class ControlsWindow {
    // JFrame window that shows a text panel displaying the game and has buttons to control it

    static Font boldFont = new Font("Arial", Font.BOLD, 14);
    static Font plainFont = new Font("Arial", Font.PLAIN, 15);

    static JFrame frame = new JFrame("Game View");
    static JTextPane gameView;

    public static void openControlsWindow() {
        // Open the JFrame window
        frame.setSize(600, 400);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout(10, 10));

        // Initialize the buttons to control the game
        initDPadPanel();
        initActionPanel();

        // Add the text panel that displays the game to the center of the window
        frame.add(Main.terminalPanel, BorderLayout.CENTER);

        // Make the frame visible
        frame.setVisible(true);
//        frame.setLocationRelativeTo(null);
    }

    public static void initDPadPanel() {
        // Create the panel to hold the D-Pad buttons using a GridBagLayout
        // The GridBagLayout here also lets me put the buttons in a D-Pad shape
        JPanel DPadPanel = new JPanel(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.insets = new Insets(5, 5, 5, 5);
        constraints.fill = GridBagConstraints.NONE;
        constraints.weightx = 0;
        constraints.weighty = 0;

        // Create the up button on the D-Pad and position it at the top
        JButton upButton = new JButton("Up");
        upButton.setBackground(new Color(59, 89, 182));
        upButton.setForeground(Color.WHITE);
        upButton.setFont(boldFont);
        constraints.gridx = 1;
        constraints.gridy = 0;
        DPadPanel.add(upButton, constraints);

        // Create the down button on the D-Pad and position it at the bottom
        JButton downButton = new JButton("Down");
        downButton.setBackground(new Color(59, 89, 182));
        downButton.setForeground(Color.WHITE);
        downButton.setFont(boldFont);
        constraints.gridx = 1;
        constraints.gridy = 2;
        DPadPanel.add(downButton, constraints);

        // Create the left button on the D-Pad and position it on the left
        JButton leftButton = new JButton("Left");
        leftButton.setBackground(new Color(59, 89, 182));
        leftButton.setForeground(Color.WHITE);
        leftButton.setFont(boldFont);
        constraints.gridx = 0;
        constraints.gridy = 1;
        DPadPanel.add(leftButton, constraints);

        // Create the right button on the D-Pad and position it on the right
        JButton rightButton = new JButton("Right");
        rightButton.setBackground(new Color(59, 89, 182));
        rightButton.setForeground(Color.WHITE);
        rightButton.setFont(boldFont);
        constraints.gridx = 2;
        constraints.gridy = 1;
        DPadPanel.add(rightButton, constraints);

        if (Objects.equals(Main.cameraButtons, "D-Pad")) {
            // If the camera movement buttons are set on the D-Pad, add them and position them to the left and right of the up button
            JButton cameraLeftButton = new JButton("C Left");
            cameraLeftButton.setBackground(new Color(59, 89, 182));
            cameraLeftButton.setForeground(Color.WHITE);
            cameraLeftButton.setFont(boldFont);
            constraints.gridx = 0;
            constraints.gridy = 0;
            DPadPanel.add(cameraLeftButton, constraints);

            JButton cameraRightButton = new JButton("C Right");
            cameraRightButton.setBackground(new Color(59, 89, 182));
            cameraRightButton.setForeground(Color.WHITE);
            cameraRightButton.setFont(boldFont);
            constraints.gridx = 2;
            constraints.gridy = 0;
            DPadPanel.add(cameraRightButton, constraints);

            // Add custom action listeners to the camera buttons to be able to hold down the buttons
            setupInputButtons(cameraLeftButton, "j");
            setupInputButtons(cameraRightButton, "l");
        }

        // Add the panel containing the D-Pad to the window
        frame.add(DPadPanel, BorderLayout.WEST);

        // Add custom action listeners to the D-Pad buttons
        // These check if the mouse is holding down the button, and continuously send the input until the button is released
        setupInputButtons(upButton, "w");
        setupInputButtons(downButton, "s");
        setupInputButtons(leftButton, "a");
        setupInputButtons(rightButton, "d");
    }

    public static void initActionPanel() {
        // Create the panel to hold the rest of the buttons using a GridBagLayout
        JPanel actionButtonPanel = new JPanel(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.insets = new Insets(5, 5, 5, 5); // Add padding around buttons
        constraints.fill = GridBagConstraints.NONE; // Prevent stretching
        constraints.weightx = 0;
        constraints.weighty = 0;

        // Create the fire button and have it send the spacebar input to the game when it's pressed
        JButton fireButton = new JButton("Fire");
        fireButton.setBackground(new Color(59, 89, 182));
        fireButton.setForeground(Color.WHITE);
        fireButton.setFont(boldFont);
        fireButton.addActionListener(_ -> sendInput(" "));

        // Create the interact button and have it send the "e" key input to the game when it's pressed
        JButton useButton = new JButton("Interact");
        useButton.setBackground(new Color(59, 89, 182));
        useButton.setForeground(Color.WHITE);
        useButton.setFont(boldFont);
        useButton.addActionListener(_ -> sendInput("e"));

        switch (Main.fireButtonPos) {
            // Change the position of the two action buttons based on the user's configuration
            case "Top":
                // If the fire button is set to be at the top, position it above the interact button
                constraints.gridx = 1;
                constraints.gridy = 0;
                actionButtonPanel.add(fireButton, constraints);

                constraints.gridx = 1;
                constraints.gridy = 1;
                actionButtonPanel.add(useButton, constraints);
                break;

            case "Bottom":
                // If the fire button is set to be at the bottom, position it below the interact button
                constraints.gridx = 1;
                constraints.gridy = 1;
                actionButtonPanel.add(fireButton, constraints);

                constraints.gridx = 1;
                constraints.gridy = 0;
                actionButtonPanel.add(useButton, constraints);
                break;
        }

        if (Objects.equals(Main.cameraButtons, "Action Buttons")) {
            // If the camera movement buttons are set on the action buttons, add them and position them to the left and right of the button on the top
            JButton cameraLeftButton = new JButton("C Left");
            cameraLeftButton.setBackground(new Color(59, 89, 182));
            cameraLeftButton.setForeground(Color.WHITE);
            cameraLeftButton.setFont(boldFont);
            constraints.gridx = 0;
            constraints.gridy = 0;
            actionButtonPanel.add(cameraLeftButton, constraints);

            JButton cameraRightButton = new JButton("C Right");
            cameraRightButton.setBackground(new Color(59, 89, 182));
            cameraRightButton.setForeground(Color.WHITE);
            cameraRightButton.setFont(boldFont);
            constraints.gridx = 2;
            constraints.gridy = 0;
            actionButtonPanel.add(cameraRightButton, constraints);

            // Add custom action listeners to the camera buttons to be able to hold down the buttons
            setupInputButtons(cameraLeftButton, "j");
            setupInputButtons(cameraRightButton, "l");
        }

        // Add the panel containing the action buttons to the window
        frame.add(actionButtonPanel, BorderLayout.EAST);

        // Create the enter button and have it send the enter key input to the game when it's pressed
        JButton enterButton = new JButton("Enter");
        enterButton.setBackground(new Color(59, 89, 182));
        enterButton.setForeground(Color.WHITE);
        enterButton.setFont(boldFont);
        enterButton.addActionListener(_ -> sendInput("\r\n"));
        switch (Main.enterButtonPos) {
            // Set its position based on the user's configuration
            case "Top":
                // If it's set to the top, add it to the top of the window
                frame.add(enterButton, BorderLayout.NORTH);
                break;
            case "Bottom":
                // If it's set to the bottom, add it to the bottom of the window
                frame.add(enterButton, BorderLayout.SOUTH);
                break;
        }
    }

    private static void setupInputButtons(JButton button, String input) {
        // Detects if the mouse is being held down over the specified button
        // Keeps sending the input until the mouse is released

        // Create a timer that will repeatedly send the input every 10ms
        Timer timer = new Timer(10, e -> sendInput(input));
        timer.setInitialDelay(50); // Wait 50ms before starting the timer loop

        // Add a mouse listener to the button that can detect when the mouse is clicking the button
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                // If the mouse presses over the button, send the input repeatedly
                sendInput(input);
                timer.start();
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                // If the mouse releases, stop sending the input
                timer.stop();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                // If the mouse stops hovering over the button, stop sending the input
                timer.stop();
            }
        });
    }
}

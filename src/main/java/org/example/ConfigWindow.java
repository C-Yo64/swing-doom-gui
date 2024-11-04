package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

public class ConfigWindow {
    // JFrame window that can change the control customizations and save/load them using a text file

    static Font boldFont = new Font("Arial", Font.BOLD, 14);
    static Font plainFont = new Font("Arial", Font.PLAIN, 15);

    static JFrame frame = new JFrame("Game Configuration");
    static JTextPane gameView;

    static JTextField layoutName;

    static JComboBox cameraOptionsBox;
    static String[] cameraOptions = {"D-Pad", "Action Buttons"};
    static JComboBox fireButtonOptionsBox;
    static String[] fireButtonOptions = {"Top", "Bottom"};
    static JComboBox enterButtonOptionsBox;
    static String[] enterButtonOptions = {"Top", "Bottom"};

    public static void openConfigWindow() {
        // Open a JFrame window
        frame.setSize(600, 400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout(10, 10));

        // Create a JPanel with a field for inputting the name of the control layout and a label indicating that
        JPanel inputPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        JLabel layoutLabel = new JLabel("Controller Layout");
        layoutLabel.setFont(boldFont);
        layoutName = new JTextField(10);
        layoutName.setFont(plainFont);
        inputPanel.add(layoutLabel);
        inputPanel.add(layoutName);
        frame.add(inputPanel, BorderLayout.NORTH);

        // Initialise the buttons and drop down menus
        initGameConfig();
        initSaveLoad();

        // Display the window and set it to the center of the screen
        frame.setVisible(true);
        frame.setLocationRelativeTo(null);
    }

    public static void initGameConfig() {
        // Create a panel to display the drop down menus using a GridBagLayout
        // I used GridBagLayouts because it was easier to figure out where things went on the panel since I could just specify the x and y values
        JPanel dropDownPanel = new JPanel(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.insets = new Insets(5, 5, 5, 5); // Add extra room around items in the panel
        constraints.fill = GridBagConstraints.NONE; // Prevent stretching
        constraints.weightx = 0;
        constraints.weighty = 0;

        // Add a drop down box to set where the camera buttons should go and a label to indicate that
        JLabel cameraLabel = new JLabel("Camera button position");
        cameraLabel.setFont(boldFont);
        cameraOptionsBox = new JComboBox(cameraOptions);
        // Set the position of the box and label
        constraints.gridx = 0;
        constraints.gridy = 1;
        dropDownPanel.add(cameraOptionsBox, constraints);
        constraints.gridy = 0;
        dropDownPanel.add(cameraLabel, constraints);
        cameraOptionsBox.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                try {
                    // When an option is selected through the drop down menu, assign the corresponding variable to that option
                    // The variable will then be used when setting up the game window to determine where the controls should go
                    Main.cameraButtons = cameraOptionsBox.getSelectedItem().toString();
//                    System.out.println(Main.cameraButtons);
                } catch (Exception ex) {
                    // If it doesn't work for some reason, print the error
                    System.out.println(ex);
                }
            }
        });

        // Same thing as the section above but for the position of the fire button
        JLabel fireButtonLabel = new JLabel("Fire button position");
        fireButtonLabel.setFont(boldFont);
        fireButtonOptionsBox = new JComboBox(fireButtonOptions);
        constraints.gridx = 0;
        constraints.gridy = 3;
        dropDownPanel.add(fireButtonOptionsBox, constraints);
        constraints.gridy = 2;
        dropDownPanel.add(fireButtonLabel, constraints);
        fireButtonOptionsBox.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                try {
                    Main.fireButtonPos = fireButtonOptionsBox.getSelectedItem().toString();
//                    System.out.println(Main.fireButtonPos);
                } catch (Exception ex) {
                    System.out.println(ex);
                }
            }
        });

        // Same thing as the section above but for the position of the enter button
        JLabel enterButtonLabel = new JLabel("Enter button position");
        enterButtonLabel.setFont(boldFont);
        enterButtonOptionsBox = new JComboBox(enterButtonOptions);
        constraints.gridx = 0;
        constraints.gridy = 5;
        dropDownPanel.add(enterButtonOptionsBox, constraints);
        constraints.gridy = 4;
        dropDownPanel.add(enterButtonLabel, constraints);
        enterButtonOptionsBox.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                try {
                    Main.enterButtonPos = enterButtonOptionsBox.getSelectedItem().toString();
//                    System.out.println(Main.enterButtonPos);
                } catch (Exception ex) {
                    System.out.println(ex);
                }
            }
        });

        // Add the panel with the drop down menus to the center of the window
        frame.add(dropDownPanel, BorderLayout.CENTER);

        // Add a button to start the game to the bottom of the window
        JButton startButton = new JButton("Start Game");
        startButton.setBackground(new Color(59, 89, 182));
        startButton.setForeground(Color.WHITE);
        startButton.setFont(boldFont);
        frame.add(startButton, BorderLayout.SOUTH);
        startButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    // When the button is clicked, remove the current window, start the game, and open the game window
                    frame.dispose();
                    Main.startGame();
                    ControlsWindow.openControlsWindow();
                } catch (IOException ex) {
                    // If it doesn't work, show a pop up error message with instructions to check if the file exists
//                    throw new RuntimeException(ex);
                    JOptionPane.showMessageDialog(frame, "An error occurred when starting the game\nMake sure that 'doom_ascii.exe', 'DOOM.WAD', and '.default.cfg' are in the root directory of this project", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }

    public static void initSaveLoad() {
        // Create a panel to display the save and load buttons
        JPanel saveButtonPanel = new JPanel(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.insets = new Insets(5, 5, 5, 5);
        constraints.fill = GridBagConstraints.NONE;
        constraints.weightx = 0;
        constraints.weighty = 0;

        // Create a button to save the layout, and add it to the panel
        JButton saveButton = new JButton("Save Layout");
        saveButton.setBackground(new Color(59, 89, 182));
        saveButton.setForeground(Color.WHITE);
        saveButton.setFont(boldFont);
        constraints.gridx = 0;
        constraints.gridy = 0;
        saveButtonPanel.add(saveButton, constraints);
        saveButton.addActionListener(new ActionListener() {
            // When the button is clicked, save the configuration with a name attached to it
            public void actionPerformed(ActionEvent e) {
                if (Objects.equals(layoutName.getText(), "") || Objects.equals(layoutName.getText(), null)) {
                    // If nothing was inputted into the layout name field, show an error pop up indicating that a name is required
                    JOptionPane.showMessageDialog(frame, "A name is required to save the configuration\nInput a name and try again", "Saving Error", JOptionPane.ERROR_MESSAGE);
                } else {
                    try {
                        // Try to find an existing configuration in the text file with the same name, and overwrite it
                        String[] lines = {layoutName.getText(), Main.cameraButtons, Main.fireButtonPos, Main.enterButtonPos};
                        Main.overwriteLines("config.txt", Main.findLineInTxt(layoutName.getText(), "config.txt"), 4, List.of(lines));
                    } catch (IOException ex) {
                        try {
                            // If it can't overwrite an existing configuration, create a new one in the text file
                            Main.write("\n\n" + layoutName.getText() + "\n" + Main.cameraButtons + "\n" + Main.fireButtonPos + "\n" + Main.enterButtonPos, "config.txt");
                        } catch (IOException exc) {
                            // If that also doesn't work, show an error pop up
//                        throw new RuntimeException(exc);
                            JOptionPane.showMessageDialog(frame, "An error occurred while saving the configuration", "Saving Error", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                }
            }
        });

        // Create a button to load existing layouts, and add it to the panel
        JButton loadButton = new JButton("Load Layout");
        loadButton.setBackground(new Color(59, 89, 182));
        loadButton.setForeground(Color.WHITE);
        loadButton.setFont(boldFont);
        constraints.gridx = 0;
        constraints.gridy = 1;
        saveButtonPanel.add(loadButton, constraints);
        loadButton.addActionListener(new ActionListener() {
            // When the button is clicked, load the layout that was specified in the layout name field
            public void actionPerformed(ActionEvent e) {
                if (Objects.equals(layoutName.getText(), "") || Objects.equals(layoutName.getText(), null)) {
                    // If nothing was inputted into the layout name field, show an error pop up indicating that a name is required
                    JOptionPane.showMessageDialog(frame, "A name is required to load the configuration\nSpecify a name and try again", "Loading Error", JOptionPane.ERROR_MESSAGE);
                } else {
                    try {
                        // Use the findLineInTxt function to find what text file line the configuration is at and position the BufferedReader at that line
                        // The position of the BufferedReader is increased until it matches the line found in the function
                        BufferedReader reader = new BufferedReader(new FileReader("config.txt"));
                        for (int i = 0; i < Main.findLineInTxt(layoutName.getText(), "config.txt"); i++) {
                            reader.readLine();
                        }
                        // The next three lines after the layout name contain the custom configurations
                        // Load each of those configurations and store them in their corresponding variables
                        Main.cameraButtons = reader.readLine();
                        Main.fireButtonPos = reader.readLine();
                        Main.enterButtonPos = reader.readLine();

                        // Update the drop down boxes to match the new value of the variables
                        cameraOptionsBox.setSelectedItem(Main.cameraButtons);
                        fireButtonOptionsBox.setSelectedItem(Main.fireButtonPos);
                        enterButtonOptionsBox.setSelectedItem(Main.enterButtonPos);
                    } catch (IOException ex) {
                        // If it doesn't work, show an error pop up
                        // It tells the user to make sure the name they inputted is correct, because this error will show if the name they inputted couldn't be found in the text file
//                        throw new RuntimeException(ex);
                        JOptionPane.showMessageDialog(frame, "Could not load the specified configuration\nMake sure the name is correct", "Loading Error", JOptionPane.ERROR_MESSAGE);

                    }
                }
            }
        });

        // Add the panel containing the save/load buttons to the window
        frame.add(saveButtonPanel, BorderLayout.EAST);
    }
}

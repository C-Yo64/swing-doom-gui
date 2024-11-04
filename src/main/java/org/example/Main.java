package org.example;

import com.jediterm.terminal.ProcessTtyConnector;
import com.jediterm.terminal.TerminalColor;
import com.jediterm.terminal.TtyConnector;
import com.pty4j.*;
import com.jediterm.terminal.ui.*;
import com.jediterm.terminal.ui.settings.DefaultSettingsProvider;

import java.io.*;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.List;
import java.util.Objects;

public class Main {
    private static PtyProcess process;
    private static InputStream inputStream;
    private static OutputStream outputStream;

    public static String cameraButtons = "D-Pad";
    public static String fireButtonPos = "Top";
    public static String enterButtonPos = "Top";

    public static DefaultSettingsProvider settingsProvider;
    public static JediTermWidget terminalPanel;

    public static void main(String[] args) throws IOException {

        // Check if the control configuration file exists, and if not, create it
        Path path = Paths.get("config.txt");
        if (Files.exists(path)) {
        } else {
            Files.createFile(path);
        }

        // Open the window to customize controls
        ConfigWindow.openConfigWindow();

    }

    public static void startGame() throws IOException {
        // Setup the file path to the text-based doom executable
        File executable = new File("doom_ascii.exe");

        // Check if the executable exists
        if (!executable.exists()) {
            System.err.println("Executable not found at: " + executable.getAbsolutePath());
            return;
        } else {
            System.out.println("Executing " + executable.getAbsolutePath());
        }

        // Create a string with the command to start the game
        String[] command = {executable.getAbsolutePath(), "-scaling", "5"};

        // Create the settings object for the terminal JPane, and set the background colour to the same as the Windows terminal colour
        settingsProvider = new DefaultSettingsProvider() {
            public TerminalColor getDefaultBackground() {
                return new TerminalColor(12, 12, 12);
            }
        };
        // Create the JPanel for the doom terminal
        terminalPanel = new JediTermWidget(settingsProvider);

        // Start the doom terminal process
        PtyProcessBuilder builder = new PtyProcessBuilder(command);
        builder.setInitialColumns(141);
        builder.setInitialRows(44);
        process = builder.start();

        // Connect the terminal process to the terminal panel
        TtyConnector ttyConnector = new ProcessTtyConnector(process, Charset.defaultCharset()) {
            @Override
            public String getName() {
                return "Game Process";
            }
        };
        terminalPanel.setTtyConnector(ttyConnector);
        terminalPanel.start();

//        inputStream = process.getInputStream();
        outputStream = process.getOutputStream();
    }

    public static void sendInput(String input) {
        // Send button inputs to the terminal process
        // The keyboard button that get sent is determined in a string e.g. "w"
        try {
            outputStream.write(input.getBytes());
            outputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void write(String text, String textFile) throws IOException {
        // Take an input of the string to write and a text file path, and write that string to the text file

        // Create a bufferedWriter object to be able to write to the file
        FileWriter writer = new FileWriter(textFile, true);
        BufferedWriter bufferedWriter = new BufferedWriter(writer);

        bufferedWriter.write(text); // Write the contents of the string to the text file
        bufferedWriter.close(); // Close the bufferedWriter to free memory and allow creating another one under the same name
    }

    public static void overwriteLines(String textFile, int startLine, int numLinesToOverwrite, List<String> newLines) throws IOException {
        // Goes to the specified line in a text file, and overwrites the next few lines (as many as specified) without changing the rest of the file
        // Since the writer can't change existing lines, the text file is copied into memory, the overwritten lines are removed and replaced with the new lines, then all of that is written back to the text file

        // Copy the text file into a list of lines
        List<String> lines = Files.readAllLines(Paths.get(textFile), StandardCharsets.UTF_8);

        // Check if the lines exist and throw an error if not
        int endLine = startLine + numLinesToOverwrite - 1;
        if (startLine < 1 || endLine > lines.size()) {
            throw new IOException();
        }

        // Remove the overwritten lines
        for (int i = 0; i < numLinesToOverwrite; i++) {
            lines.remove(startLine - 1);
        }

        // Add the new lines where the old ones were
        lines.addAll(startLine - 1, newLines);


        // Write everything back to the file with the changes
        Files.write(Paths.get(textFile), lines, StandardCharsets.UTF_8);
    }


    public static int findLineInTxt(String line, String textFile) throws IOException {
        // Finds what line a specified string is on in a text file

        // Create a new BufferedReader to be able to read the text file
        BufferedReader reader = new BufferedReader(new FileReader(textFile));

        // Increase the line number until the current line equals the specified string
        String currentLine = "";
        int lineNumber = 0;
        while (!Objects.equals(currentLine, line)) {
            currentLine = reader.readLine();
            lineNumber++;
            if (currentLine == null) {
                // If it gets to the end of the text file without matching the current line with the string, throw an error
                reader.close();
                throw new IOException();
            }
        }

        // Close the BufferedReader and return the number that the line is on
        reader.close();
        return (lineNumber);
    }
}
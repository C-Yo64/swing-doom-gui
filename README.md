# Swing Doom GUI
A Java Swing GUI for text-rendered Doom

---------------------------------------------------------------
This is a GUI with an on-screen controller to play a text-rendered version of Doom. It has a configuration window to let you customize the position of the controller buttons, and you can save your configs to a file.

------------------
Prerequisites
----------------------
Java 22 or higher
- Make sure that the JAVA_HOME environment variable is set to the JDK 22 install path, and that the system PATH has "%JAVA_HOME%\bin" as an entry

Maven


Building and running
----------------------
- Download/clone the project

- Compile it by running "mvn clean compile" in the project directory

- Run it with "mvn exec:java"

Using the app
--------------
When running the project, it opens a JFrame window with some drop down menus to select your config options, and buttons to save and load them. 
- To save, you have to input a name into the text field at the top of the window and press save, and you can load that config by entering the same name that you saved it to. - You can have multiple configs saved at once, if they all have different names.
- To overwrite a config, press save with the same name inputted.
- Once you are ready to boot the game, press the button at the bottom of the screen.

Once the game is started, the project will open a PTY process that will run doom_ascii.exe. You will need to have the exe, a Doom WAD (likely either DOOM.WAD or DOOM1.WAD), and .default.cfg in the root directory of the project.


A JFrame window will open with the custom control layout selected. The PTY process will open a JPanel in the middle of the window, which will display its output. The game is rendered in ASCII characters, which are displayed in the window.
- To interact with the game, on-screen buttons are on the sides of the window, as determined by your custom layout.
- The D-Pad and camera buttons can be held down to continuously send the input to the game. The other buttons have to be clicked each time you want the input to be sent.



-------------------
[ASCII rendered Doom source code](https://github.com/wojciech-graj/doom-ascii)

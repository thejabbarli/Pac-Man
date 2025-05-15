package Main;

import Map.Third.GameSettings;

import javax.swing.*;
import java.awt.*;


public class GameFrame extends JFrame {

    private MainMenu mainMenu;
    private MapSelection mapSelection;

    GamePanel gamePanel;

    public GameFrame() {
        setTitle("Pac-Man");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBackground(Color.BLACK);
        setResizable(true); // Allow resizing

        showMainMenu();
    }

    public void setMapConfig(String mapName) {
        GameSettings.getInstance().setMapConfig(mapName);
    }
    public void showMainMenu() {
        mainMenu = new MainMenu();
        setContentPane(mainMenu);// Start with main menu
        pack();
        setLocationRelativeTo(null);
    }

    public void showMapSelectionScreen() {
        mapSelection = new MapSelection(this);
        setContentPane(mapSelection); // Replace content pane with MapSelection panel
        revalidate();
        pack();
    }


    public void startGame(String mapFilePath) {
        Keys keyListener = new Keys();
        setFocusable(true);
        addKeyListener(keyListener);
        gamePanel = new GamePanel(mapFilePath, keyListener);
        setContentPane(gamePanel);
        pack();
        setLocationRelativeTo(null);
    }

}

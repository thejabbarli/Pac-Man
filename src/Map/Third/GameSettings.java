package Map.Third;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class GameSettings {
    private static GameSettings instance;

    private int life = 3;

    private Point playerStart;
    private List<Point> ghostStarts; // List to store ghost positions

    private GameSettings() {
        playerStart = new Point(750, 550); // Default player start position
        ghostStarts = new ArrayList<>();
        ghostStarts.add(new Point(750, 550)); // Default ghost start position
    }

    public static GameSettings getInstance() {
        if (instance == null) {
            instance = new GameSettings();
        }
        return instance;
    }

    public void setMapConfig(String mapName) {
        switch (mapName) {
            case "map20x20":
                playerStart = new Point(50, 50);
                ghostStarts.clear();
                ghostStarts.add(new Point(150, 150)); // Example ghost start position
                break;
            case "map15x15":
                playerStart = new Point(50, 50);
                ghostStarts.clear();
                ghostStarts.add(new Point(150, 150)); // Example ghost start position
                break;
            // Add more cases for other maps or configurations
            default:
                break;
        }
    }

    public Point getPlayerStart() {
        return playerStart;
    }

    public List<Point> getGhostStarts() {
        return ghostStarts;
    }
}

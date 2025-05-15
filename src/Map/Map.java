package Map;

import Main.MapLoader;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Map extends JPanel {
    private char[][] mapData;
    private int tileSize = 50;
    private MapLoader mapLoader = new MapLoader();

    public Map(String mapFilePath, int tileSize) {
        this.tileSize = tileSize;
        mapData = mapLoader.loadMapFromFile(mapFilePath);
        mapLoader.setupMap(this, mapData, tileSize);
    }

    public char[][] getMap() {
        return mapData;
    }
    public int getWidth() {
        return mapData[0].length * tileSize;
    }
    public int getHeight() {
        return mapData.length * tileSize;
    }
    public int getTileSize() {
        return tileSize;
    }
}

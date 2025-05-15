package Map;

import Main.MapLoader;

import javax.swing.*;
import java.awt.Dimension;

public class Map extends JPanel {
    private char[][] mapData;
    private int tileSize = 50;
    private MapLoader mapLoader = new MapLoader();

    public Map(String mapFilePath, int tileSize) {
        this.tileSize = tileSize;
        mapData = mapLoader.loadMapFromFile(mapFilePath);
        mapLoader.setupMap(this, mapData, tileSize);
    }

    public java.awt.Point findTopLeftOpenPosition() {
        for (int y = 0; y < mapData.length; y++) {
            for (int x = 0; x < mapData[0].length; x++) {
                if (mapData[y][x] == 'o' || mapData[y][x] == 'O') {
                    return new java.awt.Point(x * tileSize, y * tileSize);
                }
            }
        }
        return new java.awt.Point(tileSize, tileSize); // Default fallback
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

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(getWidth(), getHeight());
    }
}
package Main;

import Map.TileImages;


import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class MapLoader {

    private int rows;
    private int cols;

    public char[][] loadMapFromFile(String fileName) {
        char[][] mapData = null;

        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            rows = 0;
            cols = 0;
            while ((line = br.readLine()) != null) {
                rows++;
                cols = line.split(" ").length;
            }
            mapData = new char[rows][cols];
            try (BufferedReader br2 = new BufferedReader(new FileReader(fileName))) {
                int row = 0;
                while ((line = br2.readLine()) != null) {
                    String[] charsLine = line.split(" ");
                    for (int col = 0; col < cols; col++) {
                        mapData[row][col] = charsLine[col].charAt(0);
                    }
                    row++;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return mapData;
    }

    public void setupMap(JPanel mapPanel, char[][] mapData, int tileSize) {
        mapPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(0, 0, 0, 0);
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;

        mapPanel.setBackground(Color.BLACK);
        mapPanel.setPreferredSize(new Dimension(cols * tileSize, rows * tileSize));
        mapPanel.setBounds(0, 0, mapPanel.getPreferredSize().width, mapPanel.getPreferredSize().height);


        for (int i = 0; i < mapData.length; i++) {
            for (int j = 0; j < mapData[i].length; j++) {
                JPanel tile = new JPanel();
                tile.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));
                tile.setOpaque(false);
                ImageIcon tileImage = TileImages.getImageForTile(mapData[i][j], tileSize);
                tile.add(tileImage != null ? new JLabel(tileImage) : new JLabel());
                gbc.gridx = j;
                gbc.gridy = i;
                mapPanel.add(tile, gbc);
            }
        }
    }


}

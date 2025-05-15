package Map.Second;

import javax.swing.*;
import java.awt.*;

public abstract class Edible extends JLabel {
    protected int x, y;
    protected boolean eaten;

    public Edible(int x, int y, int tileSize, String imagePath) {
        this.x = x;
        this.y = y;
        this.eaten = false;
        setBounds(x * tileSize, y * tileSize, tileSize, tileSize);
        setIcon(getImageForTile(imagePath, tileSize));
    }

    public abstract void onEaten();

    public int getXCoordinate() {
        return x;
    }

    public int getYCoordinate() {
        return y;
    }

    public boolean checkCollisionWithMap(int playerX, int playerY, int tileSize) {
        int tileX = x;
        int tileY = y;
        int playerTileX = playerX / tileSize;
        int playerTileY = playerY / tileSize;

        return tileX == playerTileX && tileY == playerTileY;
    }

    public static ImageIcon getImageForTile(String imagePath, int tileSize) {
        ImageIcon imageIcon = new ImageIcon(imagePath);
        Image scaledImage = imageIcon.getImage().getScaledInstance(tileSize, tileSize, Image.SCALE_SMOOTH);
        return new ImageIcon(scaledImage);
    }


    public boolean isEaten() {
        return eaten;
    }

    public void setEaten(boolean eaten) {
        this.eaten = eaten;
    }
}

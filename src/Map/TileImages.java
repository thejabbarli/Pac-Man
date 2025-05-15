package Map;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class TileImages {

    private static BufferedImage wallEnd;
    private static BufferedImage wallMid;
    private static BufferedImage wallJoin;
    private static BufferedImage wallSideJoin;
    private static BufferedImage wallEdge;
    private static BufferedImage wallSingle;
    static {
        loadImages();
    }

    private static void loadImages() {
        try {
            wallEnd = ImageIO.read(new File("res/tiles/wallEnd.png"));
            wallMid = ImageIO.read(new File("res/tiles/wallMid.png"));
            wallJoin = ImageIO.read(new File("res/tiles/wallJoin.png"));
            wallEdge = ImageIO.read(new File("res/tiles/wallEdge.png"));
            wallSideJoin = ImageIO.read(new File("res/tiles/wallSideJoin.png"));
            wallSingle = ImageIO.read(new File("res/tiles/wallSingle.png"));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static ImageIcon getImageForTile(char tileType, int tileSize) {
        BufferedImage originalImage = getImage(tileType);
        Image resizedImage = null;
        if (originalImage != null) resizedImage = originalImage.getScaledInstance(tileSize, tileSize, Image.SCALE_SMOOTH);
        if (originalImage != null) {
            return new ImageIcon(resizedImage);
        } else {
            return null;
        }
    }

    private static BufferedImage getImage(char tileType) {
        BufferedImage image = null;
        int rotation = 0;

        switch (tileType) {
            case 'o':
                break;
            case '<':
                rotation = 270;
                image = wallEnd;
                break;
            case '>':
                rotation = 90;
                image = wallEnd;
                break;
            case '^':
                rotation = 0;
                image = wallEnd;
                break;
            case 'v':
                rotation = 180;
                image = wallEnd;
                break;
            case '=':
                rotation = 90;
                image = wallMid;
                break;
            case '!':
                rotation = 0;
                image = wallMid;
                break;
            case '+':
                rotation = 0;
                image = wallJoin;
                break;
            case 'L':
                rotation = 0;
                image = wallEdge;
                break;
            case '%':
                rotation = 90;
                image = wallEdge;
                break;
            case '#':
                rotation = 270;
                image = wallEdge;
                break;
            case '&':
                rotation = 180;
                image = wallEdge;
                break;
            case 'u':
                rotation = 90;
                image = wallSideJoin;
                break;
            case 'r':
                rotation = 180;
                image = wallSideJoin;
                break;
            case 'l':
                rotation = 0;
                image = wallSideJoin;
                break;
            case 'd':
                rotation = 270;
                image = wallSideJoin;
                break;
            case '.':
                rotation = 0;
                image = wallSingle;
        }
        return rotateImage(image, rotation);
    }

    private static BufferedImage rotateImage(BufferedImage originalImage, int degrees) {
        if (originalImage == null) return null;
        int width = originalImage.getWidth();
        int height = originalImage.getHeight();

        BufferedImage rotatedImage = new BufferedImage(width, height, originalImage.getType());
        Graphics2D g2d = rotatedImage.createGraphics();
        g2d.rotate(Math.toRadians(degrees), width / 2, height / 2);
        g2d.drawImage(originalImage, 0, 0, null);
        g2d.dispose();

        return rotatedImage;
    }
}

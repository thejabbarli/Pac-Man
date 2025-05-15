package Entity.Boost;

import Entity.Ghost;
import Entity.Player;
import Map.Map; // Ensure this import points to your custom Map class
import javax.swing.*;
import java.awt.*;

public class BoostHealth extends Boost {
    private static final int MAX_PART_LIFE = 4;
    private int partLife;

    public BoostHealth(int x, int y, int size, Map map, String imagePath) {
        super(x, y, size, map, imagePath);
        this.partLife = 0;
        initializeIcon(imagePath, size);
    }

    private void initializeIcon(String imagePath, int size) {
        ImageIcon image = new ImageIcon(imagePath);
        Image resizedImage = image.getImage().getScaledInstance(size, size, Image.SCALE_SMOOTH);
        setIcon(new ImageIcon(resizedImage));
        setBounds(getX(), getY(), size, size);
    }

    @Override
    public void boostTaken(Player player, Ghost ghost) {
        System.out.println("Heal Boost Taken");
        partLife++;
        if (partLife <= MAX_PART_LIFE) {
            player.setLives(player.getLives() + 1);
        }
    }
}

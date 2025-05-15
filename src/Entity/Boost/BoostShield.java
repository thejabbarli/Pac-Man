package Entity.Boost;

import Entity.ghosts.Ghost;
import Entity.Player;
import Map.Map; // Ensure this import points to your custom Map class

import javax.swing.*;
import java.awt.*;

public class BoostShield extends Boost {
    private static final long BOOST_DURATION_MS = 5000;

    public BoostShield(int x, int y, int size, Map map, String imagePath) {
        super(x, y, size, map, imagePath);
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
        System.out.println("Shield Boost Taken");
        player.setImmortal(true);

        Thread boostTimerThread = new Thread(() -> {
            try {
                Thread.sleep(BOOST_DURATION_MS);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.err.println("Boost timer interrupted: " + e.getMessage());
            }
            player.setImmortal(false);
            System.out.println("Shield Boost Finished");
            System.out.println("Player immortal status: " + player.getImmortal());
        });

        boostTimerThread.start();
    }
}

package Entity.Boost;

import Entity.ghosts.Ghost;
import Entity.Player;
import Map.Map;  // Ensure this import points to your custom Map class

import javax.swing.*;
import java.awt.*;

public class BoostIce extends Boost {
    private static final int BOOST_DURATION_MS = 5000;
    public BoostIce(int x, int y, int size, Map map, String imagePath) {
        super(x, y, size, map, imagePath);
        ImageIcon image = new ImageIcon(imagePath);
        Image resizedImage = image.getImage().getScaledInstance(size, size, Image.SCALE_SMOOTH);
        setIcon(new ImageIcon(resizedImage));
        setBounds(x, y, size, size);
    }


    @Override
    public void boostTaken(Player player, Ghost ghost) {
        System.out.println("ICE Boost Taken");
        ghost.setGhostSpeed(0);
        System.out.println("                        " + ghost.getGhostSpeed());

        // Start a thread to revert the speed boost after BOOST_DURATION_MS milliseconds
        Thread boostTimerThread = new Thread(() -> {
            try {
                Thread.sleep(BOOST_DURATION_MS);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            // After sleep, revert the speed back to normal
            ghost.setGhostSpeed(5);
            System.out.println("ICE BOOST FINISHED");
        });

        boostTimerThread.start(); // Start the thread

    }
}

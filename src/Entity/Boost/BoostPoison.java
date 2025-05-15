package Entity.Boost;

import Entity.Ghost;
import Entity.Player;
import Map.Map;

import javax.swing.*;
import java.awt.*;

public class BoostPoison extends Boost {
    public BoostPoison(int x, int y, int size, Map map, String imagePath) {
        super(x, y, size, map, imagePath);
        ImageIcon image = new ImageIcon(imagePath);
        Image resizedImage = image.getImage().getScaledInstance(size, size, Image.SCALE_SMOOTH);
        setIcon(new ImageIcon(resizedImage));
        setBounds(x, y, size, size);
    }

    @Override
    public void boostTaken(Player player, Ghost ghost) {
        System.out.println("Poison Boost Taken");

        ghost.setPoisonated(true);
        Thread boostTimerThread = new Thread(() -> {
            try {
                Thread.sleep(5000);  // Poison effect duration
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            ghost.setPoisonated(false);
        });

        boostTimerThread.start();
    }
}

package Entity.Boost;

import Entity.Ghost;
import Entity.Player;
import Map.Map;

import javax.swing.ImageIcon;
import java.awt.Image;

public class BoostThunder extends Boost {

    private static final int BOOST_DURATION_MS = 500; // 5 seconds in milliseconds

    public BoostThunder(int x, int y, int size, Map map, String imagePath) {
        super(x, y, size, map, imagePath);
        ImageIcon image = new ImageIcon(imagePath);
        Image resizedImage = image.getImage().getScaledInstance(size, size, Image.SCALE_SMOOTH);
        setIcon(new ImageIcon(resizedImage));
        setBounds(x, y, size, size);
    }


    @Override
    public void boostTaken(Player player, Ghost ghost) {
        System.out.println("Thunder Boost Taken");
        player.setPlayerSpeed(player.getPlayerSpeed() + 30); // Increase speed

        // Start a thread to revert the speed boost after BOOST_DURATION_MS milliseconds
        Thread boostTimerThread = new Thread(() -> {
            try {
                Thread.sleep(BOOST_DURATION_MS);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            // After sleep, revert the speed back to normal
            player.setPlayerSpeed(player.getPlayerSpeed() - 30);
            System.out.println("THUNDER BOOST FINISHED");
        });

        boostTimerThread.start(); // Start the thread
    }
}

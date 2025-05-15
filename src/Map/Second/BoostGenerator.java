package Map.Second;

import Entity.Boost.*;
import Main.GamePanel;
import Map.Map;
import Map.Third.GameSettings;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static Entity.Ghost.ghostX;
import static Entity.Ghost.ghostY;
public class BoostGenerator {

    private List<Boost> boosts;
    private Map map;
    private Random random;
    private GamePanel gamePanel;

    public BoostGenerator(Map map, GamePanel gamePanel) {
        this.map = map;
        this.boosts = new ArrayList<>();
        this.random = new Random();
        this.gamePanel = gamePanel;

        Thread boostCreationThread = new Thread(this::boostCreationTask);
        boostCreationThread.start();
    }

    private void boostCreationTask() {
        while (true) {
            try {
                Thread.sleep(5000); // Wait for 5 seconds
                System.out.println("WAITED FOR 5 SEC");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            if (random.nextDouble() < 0.25) { // 25% chance to create boost
                System.out.println("GENERATING new BOOST");
                createRandomBoost();
            }
        }
    }

    private void createRandomBoost() {
        List<java.awt.Point> ghostPositions = GameSettings.getInstance().getGhostStarts();

        if (ghostPositions == null || ghostPositions.isEmpty()) {
            return; // No ghosts available, exit without adding boost
        }

        int randomGhostIndex = random.nextInt(ghostPositions.size());
        Point ghostPosition = ghostPositions.get(randomGhostIndex);

        Boost boost = null;
        switch (random.nextInt(5)) {
            case 0:
                boost = new BoostHealth(ghostX, ghostY, map.getTileSize(), map, "res/boosts/boostHealth.png");
                break;
            case 1:
                boost = new BoostThunder(ghostX, ghostY, map.getTileSize(), map, "res/boosts/boostThunder.png");
                break;
            case 2:
                boost = new BoostShield(ghostX, ghostY, map.getTileSize(), map, "res/boosts/boostShield.png");
                break;
            case 3:
                boost = new BoostPoison(ghostX, ghostY, map.getTileSize(), map, "res/boosts/boostPoison.png");
                break;
            case 4:
                boost = new BoostIce(ghostX, ghostY, map.getTileSize(), map, "res/boosts/boostIce.png");
                break;
        }

        if (boost != null) {
            boosts.add(boost);
            gamePanel.addBoost(boost); // Add boost to the game panel
        }
    }

    public List<Boost> getBoosts() {
        return boosts;
    }
}

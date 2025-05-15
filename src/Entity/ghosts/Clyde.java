package Entity.ghosts;

import Entity.Player;
import Map.Map;

public class Clyde extends Ghost {
    private static final String IMAGE_PATH = "res/ghosts/ghostAmogusOrange.png";
    private static final int CHASE_DISTANCE = 8 * 50; // 8 tiles (assuming tile size is 50)

    public Clyde(int ghostX, int ghostY, int ghostSpeed, Map map, String imagePath, Player player) {
        super(ghostX, ghostY, ghostSpeed, map, imagePath, player);
    }

    @Override
    protected void updateTarget() {
        if (currentMode == GhostMode.SCATTER) {
            // Lower-left corner
            setTargetX(0);
            setTargetY(map.getHeight() - map.getTileSize());
        } else if (currentMode == GhostMode.CHASE) {
            // If distance to Pacman is greater than 8 tiles, chase directly
            // Otherwise, go to scatter corner
            int pacmanX = player.getPlayerX();
            int pacmanY = player.getPlayerY();

            double distance = Math.sqrt(
                    Math.pow(ghostX - pacmanX, 2) + Math.pow(ghostY - pacmanY, 2)
            );

            if (distance > 8 * map.getTileSize()) {
                // Chase Pacman directly
                setTargetX(pacmanX);
                setTargetY(pacmanY);
            } else {
                // Go to scatter corner (lower-left)
                setTargetX(0);
                setTargetY(map.getHeight() - map.getTileSize());
            }
        } else if (currentMode == GhostMode.FRIGHTENED) {
            // When frightened, move away from Pacman
            setTargetX(player.getPlayerX());
            setTargetY(player.getPlayerY());
        }
    }

    @Override
    protected void updateGhostAppearance() {
        if (currentMode == GhostMode.FRIGHTENED) {
            loadImage("res/ghosts/ghostAmogusPoisoned.png");
        } else {
            loadImage(IMAGE_PATH);
        }
    }

    @Override
    public void run() {

    }
}
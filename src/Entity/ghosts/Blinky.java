package Entity.ghosts;

import Entity.Player;
import Map.Map;

public class Blinky extends Ghost {
    private static final String IMAGE_PATH = "res/ghosts/ghostAmogusRed.png";

    public Blinky(int ghostX, int ghostY, int ghostSpeed, Map map, String imagePath, Player player) {
        super(ghostX, ghostY, ghostSpeed, map, imagePath, player);
    }

    @Override
    protected void updateTarget() {
        if (currentMode == GhostMode.SCATTER) {
            // Upper-right corner
            setTargetX(map.getWidth() - map.getTileSize());
            setTargetY(0);
        } else if (currentMode == GhostMode.CHASE) {
            // Direct chase - target Pacman directly
            setTargetX(player.getPlayerX()); // Use player getter
            setTargetY(player.getPlayerY()); // Use player getter
        } else if (currentMode == GhostMode.FRIGHTENED) {
            // When frightened, move away from Pacman
            setTargetX(player.getPlayerX()); // Use player getter
            setTargetY(player.getPlayerY()); // Use player getter
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
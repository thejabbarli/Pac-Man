package Entity.ghosts;

import Entity.AllDirections;
import Entity.Player;
import Map.Map;

public class Pinky extends Ghost {
    private static final String IMAGE_PATH = "res/ghosts/ghostAmogusPink.png";

    public Pinky(int ghostX, int ghostY, int ghostSpeed, Map map, String imagePath, Player player) {
        super(ghostX, ghostY, ghostSpeed, map, imagePath, player);
    }

    @Override
    protected void updateTarget() {
        if (currentMode == GhostMode.SCATTER) {
            // Upper-left corner
            setTargetX(0);
            setTargetY(0);
        } else if (currentMode == GhostMode.CHASE) {
            // Target is 4 tiles ahead of Pacman
            int pacmanX = player.getPlayerX();
            int pacmanY = player.getPlayerY();
            int tileSize = map.getTileSize();

            // Get Pacman's direction
            AllDirections pacmanDirection = player.getCurrentDirection();

            // Calculate target 4 tiles ahead of Pacman
            switch (pacmanDirection) {
                case UP:
                    // Special bug from the original game: when Pacman faces up,
                    // target is 4 tiles up and 4 tiles to the left
                    setTargetX(pacmanX - (4 * tileSize));
                    setTargetY(pacmanY - (4 * tileSize));
                    break;
                case DOWN:
                    setTargetX(pacmanX);
                    setTargetY(pacmanY + (4 * tileSize));
                    break;
                case LEFT:
                    setTargetX(pacmanX - (4 * tileSize));
                    setTargetY(pacmanY);
                    break;
                case RIGHT:
                    setTargetX(pacmanX + (4 * tileSize));
                    setTargetY(pacmanY);
                    break;
                default:
                    setTargetX(pacmanX);
                    setTargetY(pacmanY);
                    break;
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
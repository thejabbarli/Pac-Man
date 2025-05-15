package Entity.ghosts;

import Entity.AllDirections;
import Entity.Player;
import Map.Map;

public class Inky extends Ghost {
    private static final String IMAGE_PATH = "res/ghosts/ghostAmogusCyan.png";
    private Ghost partnerGhost;

    public Inky(int ghostX, int ghostY, int ghostSpeed, Map map, String imagePath, Player player, Ghost partnerGhost) {
        super(ghostX, ghostY, ghostSpeed, map, imagePath, player);
        this.partnerGhost = partnerGhost;
    }

    protected Ghost getPartnerGhost() {
        return partnerGhost;
    }

    @Override
    protected void updateTarget() {
        if (currentMode == GhostMode.SCATTER) {
            setTargetX(map.getWidth() - map.getTileSize());
            setTargetY(map.getHeight() - map.getTileSize());
        } else if (currentMode == GhostMode.CHASE) {
            int pacmanX = player.getPlayerX();
            int pacmanY = player.getPlayerY();
            int tileSize = map.getTileSize();
            int aheadX = pacmanX;
            int aheadY = pacmanY;

            AllDirections pacmanDirection = player.getCurrentDirection();

            switch (pacmanDirection) {
                case UP:
                    aheadY = pacmanY - (2 * tileSize);
                    break;
                case DOWN:
                    aheadY = pacmanY + (2 * tileSize);
                    break;
                case LEFT:
                    aheadX = pacmanX - (2 * tileSize);
                    break;
                case RIGHT:
                    aheadX = pacmanX + (2 * tileSize);
                    break;
            }

            int blinkyX = partnerGhost.getGhostX();
            int blinkyY = partnerGhost.getGhostY();

            int vectorX = aheadX - blinkyX;
            int vectorY = aheadY - blinkyY;

            vectorX *= 2;
            vectorY *= 2;

            setTargetX(blinkyX + vectorX);
            setTargetY(blinkyY + vectorY);
        } else if (currentMode == GhostMode.FRIGHTENED) {
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
        // Animation logic if needed
    }
}
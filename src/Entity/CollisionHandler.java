package Entity;

import Entity.Player;
import Map.Second.Edible;

import java.util.List;

public class CollisionHandler {

    public static void handleCollisions(Player player, List<Edible> edibles) {
        int playerX = player.getPlayerX();
        int playerY = player.getPlayerY();
        int playerSize = player.getWidth(); // Assuming player is square with equal width and height

        for (Edible edible : edibles) {
            int edibleX = edible.getXCoordinate();
            int edibleY = edible.getYCoordinate();
            int edibleSize = edible.getWidth(); // Assuming edible is square with equal width and height

            if (isCollision(playerX, playerY, playerSize, edibleX, edibleY, edibleSize)) {
                edible.onEaten();
                edibles.remove(edible);
                break; // Exit loop after handling one collision per frame
            }
        }
    }

    private static boolean isCollision(int x1, int y1, int size1, int x2, int y2, int size2) {
        // Check for overlap between two squares
        return x1 < x2 + size2 &&
                x1 + size1 > x2 &&
                y1 < y2 + size2 &&
                y1 + size1 > y2;
    }
}

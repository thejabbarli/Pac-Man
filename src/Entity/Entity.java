package Entity;

import javax.swing.*;
import java.util.List;
import Map.Map;


public abstract class Entity extends JLabel {

    protected int entityX;
    protected int entityY;
    protected int entitySpeed;
    protected int tileSize;

    protected Map map;
    protected AllDirections currentDirection;
    protected AllDirections queuedDirection;

    private static final int ANIMATION_DELAY_MS = 100;
    private List<ImageIcon> animationFrames;
    private Thread animationThread;

    public Entity(int x, int y, int speed, String myImage) {
        this.entityX = x;
        this.entityY = y;
        this.entitySpeed = speed;
        this.tileSize = tileSize;

        setBounds(x, y, tileSize, tileSize); // Set bounds for JLabel

    }

    public void setPosition(int x, int y) {
        this.entityX = x;
        this.entityY = y;
        setLocation(x, y);
    }

    public int getXCoordinate() {
        return entityX;
    }

    public int getYCoordinate() {
        return entityY;
    }

    public int getGhostSpeed() {
        return entitySpeed;
    }



    public boolean checkCollisionWithMap(char[][] map, int x, int y) {
        int tileX = x / tileSize;
        int tileY = y / tileSize;

        // Check if the entity collides with walls on the map
        if (tileX < 0 || tileX >= map[0].length || tileY < 0 || tileY >= map.length) {
            return true; // Out of bounds
        }

        char tile = map[tileY][tileX];
        return !isWalkable(tile);
    }

    private boolean isWalkable(char tile) {
        return tile == 'o' || tile == 'O';
    }

    public void move() {
        int nextX = entityX;
        int nextY = entityY;

        // Calculate the next position based on the current direction
        switch (currentDirection) {
            case UP:
                nextY = entityY - entitySpeed;
                break;
            case LEFT:
                nextX = entityX - entitySpeed;
                break;
            case DOWN:
                nextY = entityY + entitySpeed;
                break;
            case RIGHT:
                nextX = entityX + entityY;
                break;
            default:
                break;
        }

        // Update the position if the movement in the current direction is possible
        if (!checkCollisionWithMap(getMap(), nextX, nextY)) {
            entityX = nextX;
            entityY = nextY;
        }

        setLocation(entityX, entityY);
    }

    public abstract void run();

    private void startAnimation() {
        animationThread = new Thread(() -> {
            int currentFrameIndex = 0;
            while (true) {
                setIcon(animationFrames.get(currentFrameIndex));
                currentFrameIndex = (currentFrameIndex + 1) % animationFrames.size();

                try {
                    Thread.sleep(ANIMATION_DELAY_MS);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        animationThread.start();
    }

    public abstract char[][] getMap();

}

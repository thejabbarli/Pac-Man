package Entity.ghosts;

import Entity.Entity;
import Entity.AllDirections;

import Entity.Player;
import Map.Map;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;
import java.util.List;

public abstract class Ghost extends Entity {
    protected int ghostX;
    protected int ghostY;
    protected int ghostSpeed;
    protected boolean poisonated;
    protected Player player;

    protected GhostMode currentMode;
    protected int scatterTimer;
    protected int chaseTimer;

    protected AllDirections currentDirectionG = AllDirections.NULL;
    protected AllDirections queuedDirectionG = AllDirections.NULL;

    protected int targetX;
    protected int targetY;
    protected String originalImagePath;
    private String currentImagePath;


    // Add enum for ghost mode
    public enum GhostMode {
        CHASE,
        SCATTER,
        FRIGHTENED
    }

    public Ghost(int ghostX, int ghostY, int ghostSpeed, Map map, String myImage, Player player) {
        super(ghostX, ghostY, ghostSpeed, myImage);
        this.originalImagePath = myImage;
        this.currentImagePath = myImage;
        this.map = map;
        this.ghostX = ghostX;
        this.ghostY = ghostY;
        this.ghostSpeed = ghostSpeed;
        this.player = player;
        this.currentMode = GhostMode.SCATTER;
        this.scatterTimer = 700;
        this.chaseTimer = 2000;

        setBounds(ghostX, ghostY, map.getTileSize(), map.getTileSize());
        loadImage(myImage);
        poisonated = false;
    }

    protected void loadImage(String imagePath) {
        ImageIcon image = new ImageIcon(imagePath);
        Image resizedImage = image.getImage().getScaledInstance(map.getTileSize(), map.getTileSize(), Image.SCALE_SMOOTH);
        setIcon(new ImageIcon(resizedImage));
        setBounds(ghostX, ghostY, map.getTileSize(), map.getTileSize());
    }

    public void restoreOriginalImage() {
        loadImage(originalImagePath);
        setVisible(true);
    }
    public void setGhostX(int x) {
        this.ghostX = x;
    }

    public void setGhostY(int y) {
        this.ghostY = y;
    }

    public void setGhostImage(String imagePath) {
        this.currentImagePath = imagePath;

        // Load and set the new image
        ImageIcon image = new ImageIcon(imagePath);
        Image resizedImage = image.getImage().getScaledInstance(map.getTileSize(), map.getTileSize(), Image.SCALE_SMOOTH);
        setIcon(new ImageIcon(resizedImage));
        setVisible(true);
    }
    // In the Ghost class, update setPoisonated method
    public void setPoisonated(boolean poisonated) {
        this.poisonated = poisonated;
        if (poisonated) {
            // Set to poisoned image
            setGhostImage("res/ghosts/ghostAmogusPoisoned.png");
            currentMode = GhostMode.FRIGHTENED;
        } else {
            // Reset to original image based on ghost type
            if (this instanceof Blinky) {
                setGhostImage("res/ghosts/ghostAmogusRed.png");
            } else if (this instanceof Inky) {
                setGhostImage("res/ghosts/ghostAmogusCyan.png");
            } else if (this instanceof Pinky || this instanceof Clyde) {
                setGhostImage("res/ghosts/danczakSlawomir.png");
            }
            currentMode = GhostMode.CHASE;
        }
        setVisible(true); // Ensure visibility
    }

    public boolean getPoisonated() {
        return poisonated;
    }

    public double getDistanceLine(double x, double y) {
        return Math.abs(x - y);
    }

    public double getDistance(double x, double y) {
        return Math.hypot(x, y);
    }

    // Update timer and switch modes
    public void updateMode() {
        if (!poisonated) { // Only update mode if not frightened due to poison
            if (currentMode == GhostMode.SCATTER) {
                scatterTimer--;
                if (scatterTimer <= 0) {
                    currentMode = GhostMode.CHASE;
                    chaseTimer = 2000;
                    updateGhostAppearance();
                }
            } else if (currentMode == GhostMode.CHASE) {
                chaseTimer--;
                if (chaseTimer <= 0) {
                    currentMode = GhostMode.SCATTER;
                    scatterTimer = 700;
                    updateGhostAppearance();
                }
            }
        }
    }

    // Abstract methods to be implemented by specific ghosts
    protected abstract void updateTarget();
    protected abstract void updateGhostAppearance();

    public AllDirections recommendedQueuedDirection() {
        // Update the target based on the ghost's specific behavior
        updateTarget();

        int rightPlace = (ghostX + ghostSpeed);
        int leftPlace = (ghostX - ghostSpeed);
        int upPlace = (ghostY - ghostSpeed);
        int downPlace = (ghostY + ghostSpeed);

        double ifRight = 0;
        double ifLeft = 0;
        double ifUp = 0;
        double ifDown = 0;

        boolean checkRight = false;
        boolean checkDown = false;
        boolean checkLeft = false;
        boolean checkUp = false;

        if (!collidesWithWalls(rightPlace, ghostY, AllDirections.RIGHT) && currentDirectionG != AllDirections.LEFT) {
            checkRight = true;
            ifRight = getDistance(getDistanceLine(rightPlace, getTargetX()), getDistanceLine(ghostY, getTargetY()));
        }

        if (!collidesWithWalls(ghostX, downPlace, AllDirections.DOWN) && currentDirectionG != AllDirections.UP) {
            checkDown = true;
            ifDown = getDistance(getDistanceLine(ghostX, getTargetX()), getDistanceLine(downPlace, getTargetY()));
        }

        if (!collidesWithWalls(leftPlace, ghostY, AllDirections.LEFT) && currentDirectionG != AllDirections.RIGHT) {
            checkLeft = true;
            ifLeft = getDistance(getDistanceLine(leftPlace, getTargetX()), getDistanceLine(ghostY, getTargetY()));
        }

        if (!collidesWithWalls(ghostX, upPlace, AllDirections.UP) && currentDirectionG != AllDirections.DOWN) {
            checkUp = true;
            ifUp = getDistance(getDistanceLine(ghostX, getTargetX()), getDistanceLine(upPlace, getTargetY()));
        }

        // Special logic for frightened mode - reverse the distances to move away from target
        if (currentMode == GhostMode.FRIGHTENED) {
            if (checkRight) ifRight = 1000 - ifRight;
            if (checkDown) ifDown = 1000 - ifDown;
            if (checkLeft) ifLeft = 1000 - ifLeft;
            if (checkUp) ifUp = 1000 - ifUp;
        }

        List<Distance> distances = Arrays.asList(
                new Distance(ifRight, checkRight),
                new Distance(ifDown, checkDown),
                new Distance(ifLeft, checkLeft),
                new Distance(ifUp, checkUp)
        );

        double minDistance = distances.stream()
                .filter(Distance::isEnabled)
                .mapToDouble(Distance::getValue)
                .min()
                .orElse(Double.MAX_VALUE);

        if (minDistance == ifRight) {
            return AllDirections.RIGHT;
        } else if (minDistance == ifDown) {
            return AllDirections.DOWN;
        } else if (minDistance == ifLeft) {
            return AllDirections.LEFT;
        } else if (minDistance == ifUp) {
            return AllDirections.UP;
        }

        return AllDirections.RIGHT;
    }

    static class Distance {
        private final double value;
        private final boolean enabled;

        public Distance(double value, boolean enabled) {
            this.value = value;
            this.enabled = enabled;
        }

        public double getValue() {
            return value;
        }

        public boolean isEnabled() {
            return enabled;
        }
    }

    public int getTargetX() {
        return targetX;
    }

    public void setTargetX(int targetX) {
        this.targetX = targetX;
    }

    public int getTargetY() {
        return targetY;
    }

    public void setTargetY(int targetY) {
        this.targetY = targetY;
    }

    @Override
    public void move() {
        // Update mode first
        updateMode();

        int nextGhostX = ghostX;
        int nextGhostY = ghostY;

        queuedDirectionG = recommendedQueuedDirection();

        switch (queuedDirectionG) {
            case AllDirections.UP:
                nextGhostY = nextGhostY - ghostSpeed;
                break;
            case AllDirections.LEFT:
                nextGhostX = nextGhostX - ghostSpeed;
                break;
            case AllDirections.DOWN:
                nextGhostY = nextGhostY + ghostSpeed;
                break;
            case AllDirections.RIGHT:
                nextGhostX = nextGhostX + ghostSpeed;
                break;
            default:
                break;
        }

        if (queuedDirectionG != AllDirections.NULL && !collidesWithWalls(nextGhostX, nextGhostY, queuedDirectionG)) {
            currentDirectionG = queuedDirectionG;
            queuedDirectionG = AllDirections.NULL;
        }

        nextGhostX = ghostX;
        nextGhostY = ghostY;

        switch (currentDirectionG) {
            case AllDirections.UP:
                nextGhostY = ghostY - ghostSpeed;
                break;
            case AllDirections.LEFT:
                nextGhostX = ghostX - ghostSpeed;
                break;
            case AllDirections.DOWN:
                nextGhostY = ghostY + ghostSpeed;
                break;
            case AllDirections.RIGHT:
                nextGhostX = ghostX + ghostSpeed;
                break;
            default:
                break;
        }

        if (!collidesWithWalls(nextGhostX, nextGhostY, currentDirectionG)) {
            ghostX = nextGhostX;
            ghostY = nextGhostY;
        }

        setLocation(ghostX, ghostY);
    }

    protected boolean collidesWithWalls(int x, int y, AllDirections direction) {
        int tileSize = map.getTileSize();

        // Calculate the indices of the tiles around the player
        int leftTileX = x / tileSize;
        int rightTileX = (x + tileSize - 1) / tileSize;
        int upTileY = y / tileSize;
        int downTileY = (y + tileSize - 1) / tileSize;

        // Ensure indices are within bounds
        if (leftTileX < 0 || rightTileX >= map.getMap()[0].length || upTileY < 0 || downTileY >= map.getMap().length) {
            return true;
        }

        // Check for collisions based on the direction
        switch (direction) {
            case AllDirections.UP:
                return !isWalkable(map.getMap()[upTileY][leftTileX]) || !isWalkable(map.getMap()[upTileY][rightTileX]);
            case AllDirections.DOWN:
                return !isWalkable(map.getMap()[downTileY][leftTileX]) || !isWalkable(map.getMap()[downTileY][rightTileX]);
            case AllDirections.LEFT:
                return !isWalkable(map.getMap()[upTileY][leftTileX]) || !isWalkable(map.getMap()[downTileY][leftTileX]);
            case AllDirections.RIGHT:
                return !isWalkable(map.getMap()[upTileY][rightTileX]) || !isWalkable(map.getMap()[downTileY][rightTileX]);
            default:
                return false;
        }
    }

    protected boolean isWalkable(char tile) {
        return tile == 'o' || tile == 'O';
    }

    @Override
    public char[][] getMap() {
        return null;
    }

    @Override
    public int getGhostSpeed() {
        return ghostSpeed;
    }

    public void setGhostSpeed(int x) {
        ghostSpeed = x;
    }

    // In Ghost.java
    public int getGhostX() {
        return this.ghostX;
    }

    public int getGhostY() {
        return this.ghostY;
    }
}
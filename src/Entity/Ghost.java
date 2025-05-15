package Entity;

import Map.Map;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;
import java.util.List;
import java.util.OptionalDouble;
import java.util.OptionalInt;

import static Entity.Player.playerX;
import static Entity.Player.playerY;

public class Ghost extends Entity {

    public static int ghostX;
    public static int ghostY;
    private int ghostSpeed;

    private boolean poisonated;

    private AllDirections currentDirectionG = AllDirections.NULL;
    private AllDirections queuedDirectionG = AllDirections.NULL;

    private Thread animationThread;
    private List<ImageIcon> animationFrames;
    private static final int NUM_FRAMES = 3;
    private static final int MS = 100;

    private int targetX;
    private int targetY;


    public Ghost(int ghostX, int ghostY, int ghostSpeed, Map map, String myImage) {
        super(ghostX, ghostY, ghostSpeed, myImage);
        this.map = map;
        this.ghostX = ghostX;
        this.ghostY = ghostY;
        this.ghostSpeed = ghostSpeed;

        setBounds(ghostX, ghostY, tileSize, tileSize);
        ImageIcon image = new ImageIcon(myImage);
        Image resizedImage = image.getImage().getScaledInstance(map.getTileSize(), map.getTileSize(), Image.SCALE_SMOOTH);
        setIcon(new ImageIcon(resizedImage));
        setBounds(ghostX, ghostY, map.getTileSize(), map.getTileSize());

        poisonated = false;

        loadAnimationFrames();

        // animationThread = new Thread(this::animateGhost);
        // animationThread.start();


    }
     /*public double getDistanceY(int x, int y){
        int distanceY =  Math.abs(x - y);
        return distanceY;
    }*/

    public void setGhostX(int x) {
        this.ghostX = x;
    }
    public void setGhostY(int x) {
        this.ghostX = x;
    }

    public void setImage(String imagePath){

    }
    public void setPoisonated(boolean poisonated) {
        this.poisonated = poisonated;
    }
    public boolean getPoisonated(){
        return poisonated;
    }

    public double getDistanceLine(double x, double y){
        double distanceX =  Math.abs(x - y);
        return distanceX;
    }


    public double getDistance(double x, double y){
        double distance = Math.hypot(x, y);
        return distance;
    }


    public AllDirections recommendedQueuedDirection(){

        if(!poisonated){
            setTargetX(playerX);
            setTargetY(playerY);
            ImageIcon image = new ImageIcon("res/ghosts/ghostAmogusCyan.png");
            Image resizedImage = image.getImage().getScaledInstance(map.getTileSize(), map.getTileSize(), Image.SCALE_SMOOTH);
            setIcon(new ImageIcon(resizedImage));
            setBounds(ghostX, ghostY, map.getTileSize(), map.getTileSize());
        }
        else {
            ImageIcon image = new ImageIcon("res/ghosts/ghostAmogusPoisoned.png");
            Image resizedImage = image.getImage().getScaledInstance(map.getTileSize(), map.getTileSize(), Image.SCALE_SMOOTH);
            setIcon(new ImageIcon(resizedImage));
            setBounds(ghostX, ghostY, map.getTileSize(), map.getTileSize());
            setTargetY(0);
            setTargetX(0);
        }



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



        if (!collidesWithWalls(rightPlace, ghostY,AllDirections.RIGHT) && currentDirectionG != AllDirections.LEFT){
            checkRight = true;
            ifRight = getDistance(getDistanceLine(rightPlace, getTargetX()), getDistanceLine(ghostY, getTargetY()));
        }
        if (!collidesWithWalls(ghostX, downPlace,AllDirections.DOWN) && currentDirectionG != AllDirections.UP){
            checkDown = true;
            ifDown = getDistance(getDistanceLine(downPlace, getTargetY()), getDistanceLine(ghostX, getTargetX()));
        }
        if (!collidesWithWalls(leftPlace, ghostY,AllDirections.LEFT) && currentDirectionG != AllDirections.RIGHT){
            checkLeft = true;
            ifLeft = getDistance(getDistanceLine(leftPlace, getTargetX()), getDistanceLine(ghostY, getTargetY()));
        }
        if (!collidesWithWalls(ghostX, upPlace,AllDirections.UP) && currentDirectionG != AllDirections.DOWN){
            checkUp = true;
            ifUp = getDistance(getDistanceLine(upPlace, getTargetY()), getDistanceLine(ghostX, getTargetX()));
        }

        List<Distance> distances = Arrays.asList(
                new Distance(ifRight, checkRight),
                new Distance(ifDown, checkDown),
                new Distance(ifLeft, checkLeft),
                new Distance(ifUp, checkUp)
        );

        OptionalDouble minValue = distances.stream()
                .filter(Distance::isEnabled) // Keep only enabled pairs
                .mapToDouble(Distance::getValue) // Extract values
                .min(); // Find the minimum value

        double minDistance = minValue.orElse(Double.MAX_VALUE);

        if (minDistance == ifRight){
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


    /*public void setQueuedDirectionGhost(){
        if(getDistanceX() > getDistanceY()){

            if (playerX > ghostX){
                queuedDirectionG = AllDirections.RIGHT;
            }
            else {
                queuedDirectionG = AllDirections.LEFT;
            }
        }
        else {
            if (playerY > ghostY) {
                queuedDirectionG = AllDirections.DOWN;
            } else {
                queuedDirectionG = AllDirections.UP;
            }
        }
    }*/


    @Override
    public void move() {
        int nextGhostX = ghostX;
        int nextGhostY = ghostY;


        queuedDirectionG = recommendedQueuedDirection();


        switch (queuedDirectionG) {
            case UP:
                nextGhostY = nextGhostY - ghostSpeed;
                break;
            case LEFT:
                nextGhostX = nextGhostX - ghostSpeed;
                break;
            case DOWN:
                nextGhostY = nextGhostY + ghostSpeed;
                break;
            case RIGHT:
                nextGhostX = nextGhostX + ghostSpeed;
                break;
            default:
                break;
        }

        System.out.println(targetX+"    "+targetY);

        if (queuedDirectionG != AllDirections.NULL && !collidesWithWalls(nextGhostX, nextGhostY, queuedDirectionG)) {

            queuedDirectionG = recommendedQueuedDirection();


            currentDirectionG = queuedDirectionG;

            queuedDirectionG = AllDirections.NULL;
        }



        nextGhostX = ghostX;
        nextGhostY = ghostY;

        switch (currentDirectionG) {
            case UP:
                nextGhostY = ghostY - ghostSpeed;
                break;
            case LEFT:
                nextGhostX = ghostX - ghostSpeed;
                break;
            case DOWN:
                nextGhostY = ghostY + ghostSpeed;
                break;
            case RIGHT:
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

    @Override
    public void run() {

    }


    private boolean collidesWithWalls(int x, int y, AllDirections direction) {
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
            case UP:
                return !isWalkable(map.getMap()[upTileY][leftTileX]) || !isWalkable(map.getMap()[upTileY][rightTileX]);
            case DOWN:
                return !isWalkable(map.getMap()[downTileY][leftTileX]) || !isWalkable(map.getMap()[downTileY][rightTileX]);
            case LEFT:
                return !isWalkable(map.getMap()[upTileY][leftTileX]) || !isWalkable(map.getMap()[downTileY][leftTileX]);
            case RIGHT:
                return !isWalkable(map.getMap()[upTileY][rightTileX]) || !isWalkable(map.getMap()[downTileY][rightTileX]);
            default:
                return false;
        }
    }
    private boolean isWalkable(char tile) {
        return tile == 'o' || tile == 'O';
    }



    private void loadAnimationFrames() {
        // Load animation frames for the ghost (example frames)
        // Example implementation
        // animationFrames = new ArrayList<>();
        // String[] imageFilePaths = {"res/ghost/ghost1.png", "res/ghost/ghost2.png", "res/ghost/ghost3.png"};
        // for (String imagePath : imageFilePaths) {
        //     ImageIcon frame = new ImageIcon(imagePath);
        //     animationFrames.add(frame);
        // }

    }
    @Override
    public char[][] getMap() {

        return null;
    }

    @Override
    public int getGhostSpeed() {
        return ghostSpeed;
    }
    public void setGhostSpeed(int x){
        ghostSpeed = x;
    }

    // Implement additional methods as needed for common entity behaviors

}

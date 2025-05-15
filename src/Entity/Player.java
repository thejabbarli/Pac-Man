package Entity;

import Main.Keys;
import Map.Map;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Player extends Entity implements Runnable {
    private static final int NUM_FRAMES = 3;
    private static final int MS = 100;

    static int playerX;
    static int playerY;
    private int playerSpeed;
    private Keys keyManager;
    private Thread animationThread;
    private List<ImageIcon> animationFrames;
    private ImageIcon myFrame;
    private ImageIcon[] pacmanFrames;

    private boolean isFlippedHorizontally;
    private boolean isAnimating;
    private AllDirections currentDirectionP = AllDirections.NULL;
    private AllDirections queuedDirectionP = AllDirections.NULL;

    private boolean immortal;
    private int lives;

    private Map map;
    private JLabel playerLabel;

    public Player(int playerX, int playerY, int playerSpeed, Map map, String myImage, Keys keyManager) {
        super(playerX, playerY, playerSpeed, myImage);

        this.playerX = playerX;
        this.playerY = playerY;
        this.playerSpeed = playerSpeed;
        this.keyManager = keyManager;
        this.map = map;
        this.lives = 3;
        this.immortal = false;

        playerLabel = new JLabel();
        loadPacmanFrames();

        animationThread = new Thread(this);
        animationThread.start();
    }

    private void loadPacmanFrames() {
        pacmanFrames = new ImageIcon[NUM_FRAMES];
        pacmanFrames[0] = new ImageIcon("res/pacman/pacman1.png");
        pacmanFrames[1] = new ImageIcon("res/pacman/pacman2.png");
        pacmanFrames[2] = new ImageIcon("res/pacman/pacman3.png");
    }



    private void loadAnimationFrames() {

        for (ImageIcon image : pacmanFrames) {
            animationFrames.add(image);
        }
    }


    @Override
    public void run() {


            myFrame = pacmanFrames[pacmanFrames.length % NUM_FRAMES];

            setImageIcon(myFrame);

            // Direction-based image flipping
            if (currentDirection == AllDirections.LEFT && !isFlippedHorizontally) {
                flipImageHorizontally();
            } else if (currentDirection != AllDirections.LEFT && isFlippedHorizontally) {
                flipImageHorizontally();
            }
            loadAnimationFrames();

            try {
                Thread.sleep(200); // Adjust delay as needed for animation speed
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

    }

    public ImageIcon getImageIcon() {
        return myFrame;
    }

    // Setter method for setting the ImageIcon
    public void setImageIcon(ImageIcon icon) {
        this.myFrame = icon;
        setIcon(icon); // Update the JLabel's icon
        repaint(); // Refresh the component
    }

    private void flipImageHorizontally() {
        ImageIcon currentImageIcon = myFrame;
        Image currentImage = currentImageIcon.getImage();
        Image flippedImage = currentImage.getScaledInstance(currentImage.getWidth(null) * -1, currentImage.getHeight(null), Image.SCALE_DEFAULT);
        setImageIcon(new ImageIcon(flippedImage));
        isFlippedHorizontally = !isFlippedHorizontally;
    }

    public void startAnimation() {
        isAnimating = true; // Set isAnimating flag to true to start animation
    }

    public void stopAnimation() {
        isAnimating = false; // Set isAnimating flag to false to stop animation
    }

    public void setKeyManager(Keys keyManager) {
        this.keyManager = keyManager;
    }

    public void loseLife() {
        lives--;
        System.out.println("Lives remaining: " + lives);
        if (lives <= 0) {
            // Handle game over (e.g., stop the game, show game over screen, etc.)
            System.out.println("Game Over!");
        } else {
            // Reset player position or take other actions
            setPlayerPosition(50, 50);
            // Optionally, start a boost timer thread here
        }
    }

    public int getLives() {
        return lives;
    }

    public void setLives(int x) {
        lives = x;
    }

    public boolean getImmortal() {
        return immortal;
    }

    public void setImmortal(boolean isImmortal) {
        immortal = isImmortal;
    }

    public void movePlayer() {
        int nextPlayerX = playerX;
        int nextPlayerY = playerY;

        // Calculate the next position based on the queued direction
        queuedDirectionP = keyManager.getQueuedDirection();

        switch (queuedDirectionP) {
            case UP:
                nextPlayerY = playerY - playerSpeed;
                break;
            case LEFT:
                nextPlayerX = playerX - playerSpeed;
                break;
            case DOWN:
                nextPlayerY = playerY + playerSpeed;
                break;
            case RIGHT:
                nextPlayerX = playerX + playerSpeed;
                break;
            default:
                break;
        }

        // Check if the next position in the queued direction is walkable
        if (queuedDirectionP != AllDirections.NULL && !collidesWithWalls(nextPlayerX, nextPlayerY, queuedDirectionP)) {
            currentDirectionP = queuedDirectionP;
            queuedDirectionP = AllDirections.NULL;
        }

        // Calculate the next position based on the current direction
        nextPlayerX = playerX;
        nextPlayerY = playerY;

        switch (currentDirectionP) {
            case UP:
                nextPlayerY = playerY - playerSpeed;
                break;
            case LEFT:
                nextPlayerX = playerX - playerSpeed;
                break;
            case DOWN:
                nextPlayerY = playerY + playerSpeed;
                break;
            case RIGHT:
                nextPlayerX = playerX + playerSpeed;
                break;
            default:
                break;
        }

        // Update the position if the movement in the current direction is possible
        if (!collidesWithWalls(nextPlayerX, nextPlayerY, currentDirectionP)) {
            playerX = nextPlayerX;
            playerY = nextPlayerY;
        }

        setLocation(playerX, playerY);
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

    // Helper method to determine if a tile is walkable
    private boolean isWalkable(char tile) {
        return tile == 'o' || tile == 'O';
    }

    @Override
    public char[][] getMap() {
        return new char[0][];
    }

    public JLabel getPlayerLabel() {
        return playerLabel;
    }

    public void setPlayerPosition(int x, int y) {
        playerX = x;
        playerY = y;
    }

    public int getPlayerX() {
        return playerX;
    }

    public int getPlayerY() {
        return playerY;
    }

    public int getPlayerSpeed() {
        return playerSpeed;
    }

    public void setPlayerSpeed(int playerSpeed) {
        this.playerSpeed = playerSpeed;
    }

    public void setPlayerX(int x, int i1) {
        playerX = 50;
        playerY = 50;
    }
}

package Main;

import Entity.Boost.Boost;
import Entity.ghosts.*;
import Entity.Player;
import Map.Map;
import Map.Second.BoostGenerator;
import Map.Second.Edible;
import Map.Second.EdibleGenerator;
import Map.Third.GameSettings;
import Map.Third.Situation;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GamePanel extends JLayeredPane implements Runnable {
    private List<Edible> edibles;
    private JPanel charactersPanel;
    private JPanel yemPanel;
    private JPanel boostPanel;
    private Keys keyManager;
    private Thread gameThread;
    private volatile boolean gameRunning = true;
    private Player player;
    private Ghost ghostRed;
    private Ghost ghostBlue;
    private Ghost ghostPink;
    private Ghost ghostOrange;
    private List<Ghost> allGhosts;
    private Map map;
    private JLabel situationLabel;
    private Situation situation;
    private BoostGenerator boostGenerator;
    private final int TILE_SIZE = 50;
    private javax.swing.Timer visibilityTimer;

    private int timeSeconds = 0;
    private JLabel timeLabel;

    public GamePanel(String mapFilePath, Keys keyManager) {
        System.out.println("Initializing Game Panel with map file: " + mapFilePath);
        setBackground(Color.BLACK);
        setLayout(null);

        this.keyManager = keyManager;
        this.situation = new Situation();
        this.allGhosts = new ArrayList<>();

        map = new Map(mapFilePath, TILE_SIZE);
        GameSettings.getInstance().setMapConfig(mapFilePath);

        yemPanel = new JPanel();
        yemPanel.setBounds(map.getBounds());
        yemPanel.setOpaque(false);
        yemPanel.setLayout(null);

        charactersPanel = new JPanel();
        charactersPanel.setBounds(map.getBounds());
        charactersPanel.setOpaque(false);
        charactersPanel.setLayout(null);

        boostPanel = new JPanel();
        boostPanel.setBounds(map.getBounds());
        boostPanel.setOpaque(false);
        boostPanel.setLayout(null);

        // Place player at top-left corner
        java.awt.Point playerStartPoint = map.findTopLeftOpenPosition();
        player = new Player(playerStartPoint.x, playerStartPoint.y, 10, map, "res/pacman/pacman1.png", keyManager);
        charactersPanel.add(player);

        // Find bottom-right corner for ghosts
        java.awt.Point ghostStartPoint = findBottomRightOpenPosition();

        // Create ghosts at the bottom-right corner (with offsets)
        ghostRed = new Blinky(ghostStartPoint.x, ghostStartPoint.y, 5, map, "res/ghosts/ghostAmogusRed.png", player);
        ghostBlue = new Inky(ghostStartPoint.x - TILE_SIZE, ghostStartPoint.y, 5, map, "res/ghosts/ghostAmogusCyan.png", player, ghostRed);
        ghostPink = new Pinky(ghostStartPoint.x, ghostStartPoint.y - TILE_SIZE, 5, map, "res/ghosts/danczakSlawomir.png", player);
        ghostOrange = new Clyde(ghostStartPoint.x - TILE_SIZE, ghostStartPoint.y - TILE_SIZE, 5, map, "res/ghosts/danczakSlawomir.png", player);

        // Add ghosts to the list and panel
        allGhosts.add(ghostRed);
        allGhosts.add(ghostBlue);
        allGhosts.add(ghostPink);
        allGhosts.add(ghostOrange);

        charactersPanel.add(ghostRed);
        charactersPanel.add(ghostBlue);
        charactersPanel.add(ghostPink);
        charactersPanel.add(ghostOrange);

        // Generate edibles
        edibles = EdibleGenerator.generateEdibles(map, yemPanel, situation);

        // Create UI labels
        situationLabel = new JLabel("Points: 0");
        situationLabel.setBounds(map.getX(), 10, 150, 30);
        situationLabel.setForeground(Color.WHITE);

        timeLabel = new JLabel("Time: 0");
        timeLabel.setBounds(map.getX() + map.getWidth() - 150, 10, 150, 30);
        timeLabel.setForeground(Color.WHITE);

        // Add components to layers
        add(map, Integer.valueOf(1));
        add(yemPanel, Integer.valueOf(4));
        add(charactersPanel, Integer.valueOf(5));
        add(boostPanel, Integer.valueOf(6));
        add(situationLabel, Integer.valueOf(7));
        add(timeLabel, Integer.valueOf(8));

        setPreferredSize(map.getPreferredSize());

        // Create boost generator
        boostGenerator = new BoostGenerator(map, this, ghostRed);
        boostGenerator = new BoostGenerator(map, this, ghostPink);
        boostGenerator = new BoostGenerator(map, this, ghostBlue);
        boostGenerator = new BoostGenerator(map, this, ghostOrange);


        // Start game thread
        gameThread = new Thread(this);
        gameThread.start();

        visibilityTimer = new javax.swing.Timer(2000, e -> ensureGhostVisibility());
        visibilityTimer.start();
    }

    // Helper method to find bottom-right open position
    private java.awt.Point findBottomRightOpenPosition() {
        char[][] mapData = map.getMap();

        // Start from bottom-right and scan towards top-left
        for (int y = mapData.length - 1; y >= 0; y--) {
            for (int x = mapData[0].length - 1; x >= 0; x--) {
                if (isWalkable(mapData[y][x])) {
                    return new java.awt.Point(x * TILE_SIZE, y * TILE_SIZE);
                }
            }
        }

        // Fallback to a default position if no walkable tile found
        return new java.awt.Point(map.getWidth() - TILE_SIZE, map.getHeight() - TILE_SIZE);
    }

    private boolean isWalkable(char tile) {
        return tile == 'o' || tile == 'O';
    }

    public void someMethod() {
        // Use situation variable here
        int currentTotalPoints = situation.getTotalPoints();
        // Perform operations using situation
    }

    public void startGame() {
        System.out.println("Game started!");
        gameRunning = true;
        if (visibilityTimer != null && !visibilityTimer.isRunning()) {
            visibilityTimer.start();
        }
        gameThread = new Thread(this);
        gameThread.start();
    }

    public void addBoost(Boost boost) {
        boostPanel.add(boost);
        boostPanel.revalidate();
        boostPanel.repaint();
    }

    @Override
    public void run() {
        while (gameRunning) {
            updateGame();
            try {
                Thread.sleep(32); // Adjust as needed for desired frame rate
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void updateGame() {
        updateEdibles();
        checkBoosts();
        handlePlayerGhostCollisions();

        player.movePlayer();

        // Move all ghosts
        for (Ghost ghost : allGhosts) {
            ghost.move();
        }

        // Make sure ghosts are visible and in the panel
        ensureGhostVisibility();

        updateTimeLabel();
        updatePointsLabel();
        checkGameOver();
    }

    private void updateEdibles() {
        List<Edible> toRemove = new ArrayList<>();
        for (Edible edible : edibles) {
            if (edible.checkCollisionWithMap(player.getPlayerX(), player.getPlayerY(), TILE_SIZE)) {
                edible.onEaten();
                situation.increaseTotalPoints();
                yemPanel.remove(edible);
                toRemove.add(edible);
                break;
            }
        }
        edibles.removeAll(toRemove);
    }

    private void checkBoosts() {
        List<Boost> boostsToRemove = new ArrayList<>();
        for (Boost boost : boostGenerator.getBoosts()) {
            if (checkCollisionWithPlayer(boost)) {
                // Apply boost to all ghosts
                for (Ghost ghost : allGhosts) {
                    boost.boostTaken(player, ghost);
                }

                boostPanel.remove(boost);
                boostsToRemove.add(boost);
                break;
            }
        }
        boostGenerator.getBoosts().removeAll(boostsToRemove);
    }

    private boolean checkCollisionWithPlayer(Boost boost) {
        Rectangle playerBounds = new Rectangle(player.getPlayerX(), player.getPlayerY(), TILE_SIZE, TILE_SIZE);
        Rectangle boostBounds = new Rectangle(boost.getX(), boost.getY(), boost.getWidth(), boost.getHeight());
        return playerBounds.intersects(boostBounds);
    }

    private void handlePlayerGhostCollisions() {
        if (!player.getImmortal()) {
            for (Ghost ghost : allGhosts) {
                if (ghost.isVisible() && checkCollisionWithGhost(player, ghost)) {
                    handlePlayerDeath();
                    break;
                }
            }
        }
    }

    private void handlePlayerDeath() {
        player.loseLife();
        try {
            Thread.sleep(1500); // Delay for dramatic effect
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Reset positions
        resetPositions();

        if (player.getLives() <= 0) {
            gameRunning = false;
            System.out.println("Game Over!");
        }
    }

    private void resetPositions() {
        System.out.println("Resetting positions after collision...");

        // Reset player to top-left
        java.awt.Point playerPos = map.findTopLeftOpenPosition();
        player.setPlayerPosition(playerPos.x, playerPos.y);

        // Reset ghosts to bottom-right with slight offset from each other
        java.awt.Point ghostPos = findBottomRightOpenPosition();

        // Reset each ghost position and ensure visibility
        ghostRed.setGhostX(ghostPos.x);
        ghostRed.setGhostY(ghostPos.y);
        ghostRed.setLocation(ghostPos.x, ghostPos.y);
        ghostRed.setVisible(true);

        ghostBlue.setGhostX(ghostPos.x - TILE_SIZE);
        ghostBlue.setGhostY(ghostPos.y);
        ghostBlue.setLocation(ghostPos.x - TILE_SIZE, ghostPos.y);
        ghostBlue.setVisible(true);

        ghostPink.setGhostX(ghostPos.x);
        ghostPink.setGhostY(ghostPos.y - TILE_SIZE);
        ghostPink.setLocation(ghostPos.x, ghostPos.y - TILE_SIZE);
        ghostPink.setVisible(true);

        ghostOrange.setGhostX(ghostPos.x - TILE_SIZE);
        ghostOrange.setGhostY(ghostPos.y - TILE_SIZE);
        ghostOrange.setLocation(ghostPos.x - TILE_SIZE, ghostPos.y - TILE_SIZE);
        ghostOrange.setVisible(true);

        // Force refresh
        charactersPanel.revalidate();
        charactersPanel.repaint();

        System.out.println("Position reset complete. Ghosts: " + allGhosts.size());
    }

    private boolean checkCollisionWithGhost(Player player, Ghost ghost) {
        Rectangle playerBounds = new Rectangle(player.getPlayerX(), player.getPlayerY(), TILE_SIZE, TILE_SIZE);
        Rectangle ghostBounds = new Rectangle(ghost.getGhostX(), ghost.getGhostY(), TILE_SIZE, TILE_SIZE);
        return playerBounds.intersects(ghostBounds);
    }

    private void updateTimeLabel() {
        timeSeconds++;
        timeLabel.setText("Time: " + timeSeconds);
    }

    public void updatePoints() {
        int currentTotalPoints = situation.getTotalPoints();
        System.out.println("Current total points: " + currentTotalPoints);
    }

    private void updatePointsLabel() {
        situationLabel.setText("Points: " + situation.getTotalPoints() + " Points Left: " + getMaxPoints());
    }

    private void checkGameOver() {
        if (edibles.isEmpty()) {
            gameRunning = false;
            System.out.println("Game Over - You Won!");

            // Display win message (optional)
            JOptionPane.showMessageDialog(this,
                    "Congratulations! You won with " + situation.getTotalPoints() + " points!",
                    "Victory", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    public void stopGame() {
        gameRunning = false;
        if (visibilityTimer != null) {
            visibilityTimer.stop();
        }
    }

    public void resumeGame() {
        gameRunning = true;
    }

    public void restartGame() {
        stopGame();
        initializeGame();
        startGame();
    }

    private void initializeGame() {
        situation.reset();
        timeSeconds = 0;

        // Clear existing edibles
        edibles.clear();
        yemPanel.removeAll();

        // Generate new edibles
        edibles = EdibleGenerator.generateEdibles(map, yemPanel, situation);

        // Reset player and ghost positions
        resetPositions();

        // Ensure all components are visible
        yemPanel.revalidate();
        yemPanel.repaint();
    }

    private void ensureGhostVisibility() {
        // Check if ghosts are in the charactersPanel and visible
        for (Ghost ghost : allGhosts) {
            boolean isInPanel = false;
            Component[] components = charactersPanel.getComponents();
            for (Component c : components) {
                if (c == ghost) {
                    isInPanel = true;
                    break;
                }
            }

            // If ghost is not in panel, add it back
            if (!isInPanel) {
                System.out.println("Ghost missing from panel! Re-adding: " + ghost.getClass().getSimpleName());
                charactersPanel.add(ghost);
            }

            // Make sure ghost is visible and properly positioned
            ghost.setVisible(true);
            ghost.setLocation(ghost.getGhostX(), ghost.getGhostY());

            // If ghost was poisoned and is not anymore, make sure it has its original appearance
            if (!ghost.getPoisonated()) {
                if (ghost instanceof Blinky) {
                    ghost.setGhostImage("res/ghosts/ghostAmogusRed.png");
                } else if (ghost instanceof Inky) {
                    ghost.setGhostImage("res/ghosts/ghostAmogusCyan.png");
                } else if (ghost instanceof Pinky || ghost instanceof Clyde) {
                    ghost.setGhostImage("res/ghosts/danczakSlawomir.png");
                }
            }
        }

        // Force update UI
        charactersPanel.revalidate();
        charactersPanel.repaint();
    }

    public int getMaxPoints() {
        return edibles.size();
    }

    public void setKeyManager(Keys keyManager) {
        this.keyManager = keyManager;
        player.setKeyManager(keyManager);
    }

    public Player getPlayer() {
        return player;
    }

    public Ghost getGhostBlue() {
        return ghostBlue;
    }

    public Ghost getGhostRed() {
        return ghostRed;
    }

    public Ghost getGhostPink() {
        return ghostPink;
    }

    public Ghost getGhostOrange() {
        return ghostOrange;
    }

    public List<Ghost> getAllGhosts() {
        return allGhosts;
    }

    public Map getMap() {
        return map;
    }
}
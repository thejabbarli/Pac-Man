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
import java.util.List;

public class GamePanel extends JLayeredPane implements Runnable {
    private List<Edible> edibles;
    private JPanel charactersPanel;
    private JPanel yemPanel;
    private JPanel boostPanel;
    private Keys keyManager;
    private Thread gameThread;
    private volatile boolean gameRunning = true;
    private Player player;
    private Ghost ghostBlue;
    private Ghost ghostRed;
    private Map map;
    private JLabel situationLabel;
    private Situation situation;
    private BoostGenerator boostGenerator;

    private int timeSeconds = 0;
    private JLabel timeLabel;

    public GamePanel(String mapFilePath, Keys keyManager) {
        System.out.println("Initializing Game Panel with map file: " + mapFilePath);
        setBackground(Color.BLACK);
        setLayout(null);

        this.keyManager = keyManager;
        this.situation = new Situation();

        map = new Map(mapFilePath, 50); // Assuming TILESIZE is 50

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

        // Get the top-left open position from the map
        java.awt.Point startPoint = map.findTopLeftOpenPosition();

        // Create player at the top-left open position
        player = new Player(startPoint.x, startPoint.y, 10, map, "res/pacman/pacman1", keyManager);
        charactersPanel.add(player);

        // Create ghosts - Blinky first since Inky depends on it
        ghostRed = new Blinky(900, 900, 5, map, "res/ghosts/ghostAmogusRed.png", player);

        // Create Inky with reference to Blinky
        ghostBlue = new Inky(150, 50, 5, map, "res/ghosts/ghostAmogusCyan.png", player, ghostRed);

        // Create other ghosts
        Ghost ghostPink = new Pinky(50, 150, 5, map, "res/ghosts/ghostAmogusPink.png", player);
        Ghost ghostOrange = new Clyde(150, 150, 5, map, "res/ghosts/ghostAmogusOrange.png", player);

        // Add all ghosts to the characters panel
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
        boostGenerator = new BoostGenerator(map, this, ghostBlue);

        // Start game thread
        gameThread = new Thread(this);
        gameThread.start();
    }

    public void someMethod() {
        // Use situation variable here
        int currentTotalPoints = situation.getTotalPoints();
        // Perform operations using situation
    }
    public void startGame() {
        System.out.println("Game started!");
        // Additional logic to initialize game components or start the game loop
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
        checkPlayerGhostCollision();

        player.movePlayer();
        ghostBlue.move();
        ghostRed.move();

        updateTimeLabel();
        updatePointsLabel();
        checkGameOver();
    }

    private void updateEdibles() {
        for (Edible edible : edibles) {
            if (edible.checkCollisionWithMap(player.getPlayerX(), player.getPlayerY(), 50)) { // Assuming TILESIZE is 50
                edible.onEaten();
                situation.increaseTotalPoints();
                yemPanel.remove(edible);
                edibles.remove(edible);
                break;
            }
        }
    }

    private void checkBoosts() {
        for (Boost boost : boostGenerator.getBoosts()) {
            if (checkCollisionWithPlayer(boost)) {
                boost.boostTaken(player, ghostBlue);
                boostPanel.remove(boost);
                boostGenerator.getBoosts().remove(boost);
                break;
            }
        }
    }

    private boolean checkCollisionWithPlayer(Boost boost) {
        Rectangle playerBounds = new Rectangle(player.getPlayerX(), player.getPlayerY(), 50, 50); // Assuming TILESIZE is 50
        Rectangle boostBounds = new Rectangle(boost.getX(), boost.getY(), boost.getWidth(), boost.getHeight());
        return playerBounds.intersects(boostBounds);
    }

    private void checkPlayerGhostCollision() {
        if (!player.getImmortal()) {
            if (checkCollisionWithGhost(player, ghostBlue) || checkCollisionWithGhost(player, ghostRed)) {
                player.loseLife();
                try {
                    Thread.sleep(1500); // Delay for dramatic effect
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                player.setPlayerX(50, 50); // Reset player position

                if (player.getLives() <= 0) {
                    gameRunning = false;
                    System.out.println("Game Over!");
                }
            }
        } else {
            System.out.println("SHIELD IS ACTIVE");
        }
    }

    private boolean checkCollisionWithGhost(Player player, Ghost ghost) {
        Rectangle playerBounds = new Rectangle(player.getPlayerX(), player.getPlayerY(), 50, 50); // Assuming TILESIZE is 50
        Rectangle ghostBounds = new Rectangle(ghost.getX(), ghost.getY(), 50, 50); // Assuming ghost has same tile size
        return playerBounds.intersects(ghostBounds);
    }

    private void updateTimeLabel() {
        timeSeconds++;
        timeLabel.setText("Time: " + timeSeconds);
    }

    public void updatePoints() {
        int currentTotalPoints = situation.getTotalPoints(); // Access getTotalPoints method
        System.out.println("Current total points: " + currentTotalPoints);
    }
    private void updatePointsLabel() {
        situationLabel.setText("Points: " + situation.getTotalPoints() + " Points Left: " + getMaxPoints());
    }

    private void checkGameOver() {
        if (edibles.isEmpty()) {
            gameRunning = false;
            System.out.println("Game Over!");
        }
    }

    public void stopGame() {
        gameRunning = false;
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
        edibles = EdibleGenerator.generateEdibles(map, yemPanel, situation);
        player.setPlayerX(GameSettings.getInstance().getPlayerStart().x, GameSettings.getInstance().getPlayerStart().y);
        ghostBlue.setGhostX(150);
        ghostBlue.setGhostY(50);
        ghostRed.setGhostX(900);
        ghostRed.setGhostY(900);
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

    public Map getMap() {
        return map;
    }
}

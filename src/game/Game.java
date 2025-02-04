package game;

import java.util.PriorityQueue;

import entities.server.Entity;
import entities.server.Player;
import entities.server.ServerEntity;
import graphics.GamePanel;
import graphics.TileMap;
import inputs.LocalKeyInputHandler;
import utils.ConcurrentSortedList;

public class Game extends Thread {

    public static final long NANOSECONDS_PER_SECOND = 1000000000; // 1s = 10^9 ns
    public static final long UPDATES_PER_SECOND = 100;
    public static final long NANOSECONDS_PER_UPDATE = NANOSECONDS_PER_SECOND / UPDATES_PER_SECOND;
    public static final double TIME_STEP = 0.04;

    public static final int GROUND = 400;

    public GamePanel gamePanel;
    private ConcurrentSortedList<ServerEntity> entities;
    PriorityQueue<Entity> b;
    public Player player;
    private LocalKeyInputHandler keyInputHandler;
    private final TileMap TILE_MAP = TileMap.MAP_4;
    private boolean gameInProgress;

    public Game() {

        entities = new ConcurrentSortedList<>();
        player = new Player(TILE_MAP.getSpawnPoint().x, TILE_MAP.getSpawnPoint().y, "");
        entities.add(player);
        entities.addAll(TILE_MAP.getTiles());

        gamePanel = new GamePanel(entities, player);
        
        keyInputHandler = new LocalKeyInputHandler(player);
        gamePanel.addKeyListener(keyInputHandler);

        gameInProgress = true;
        
    }

    @Override
    public void run() {
        
        long currentTime = 0;
        long previousTime = System.nanoTime();
        long deltaTime = 0;
        long accumulatedTime = 0;

        while (gameInProgress) {

            currentTime = System.nanoTime();
            deltaTime = currentTime - previousTime;
            previousTime += deltaTime;
            accumulatedTime += deltaTime;

            // update at a fixed rate
            while (accumulatedTime > NANOSECONDS_PER_UPDATE) {
                update(TIME_STEP);
                accumulatedTime -= NANOSECONDS_PER_UPDATE;
            }

            // render graphics
            draw();

        }

        System.exit(0);

    }

    public void update(double deltaTime) {

        gamePanel.updateCamera();
        keyInputHandler.handleKeys();

        for (ServerEntity entity : entities) {
            entity.update(deltaTime);
        }

        for (ServerEntity entity : entities) {
            for (ServerEntity otherEntity : entities) {
                if (entity != otherEntity && entity.isTouching(otherEntity)) {
                    // add these to their queue
                    entity.addCollision(otherEntity);
                    // entity.handleCollision(otherEntity);
                }
            }

            // handle the collisions in the correct order
            entity.handleQueuedCollisions();
        }

        // game over condition

    }

    private void draw() {
        gamePanel.repaint();
    }

    public GamePanel getGamePanel() {
        return gamePanel;
    }

    public void addPlayer(Player player) {
        entities.add(player);
    }

    public void removePlayer(Player player) {
        entities.remove(player);
    }

}

package com.warofplebs.zaki.server.manager;

import com.warofplebs.zaki.server.engine.GameEngine;
import com.warofplebs.zaki.server.user.User;
import com.warofplebs.zaki.server.exception.UserAlreadyExistsException;

import java.util.Timer;
import java.util.TimerTask;

public class GameManager extends Thread {

    private class MapSpawnerTimer extends TimerTask {

        public MapSpawnerTimer() {
        }

        public static final long MAP_SPAWN_DELAY = 15_000;

        @Override
        public void run() {
            gameEngine.getMap().spawnItems();
            gameEngine.getMap().spawnNpc();
        }
    }

    private class NpcMoveTimer extends TimerTask {

        public NpcMoveTimer() {
        }

        public static final long NPC_MOVE_DELAY = 5_000;

        @Override
        public void run() {
            gameEngine.getMap().moveNpc();
        }
    }

    private boolean running;

    private GameEngine gameEngine;

    public GameManager(GameEngine gameEngine) {
        this.gameEngine = gameEngine;
        this.running = true;
    }

    @Override
    public void run() {

        if (running) {
            Timer npcUpdateTimer = new Timer();
            npcUpdateTimer.schedule(new MapSpawnerTimer(), 0, MapSpawnerTimer.MAP_SPAWN_DELAY);

            Timer npcMoveTimer = new Timer();
            npcMoveTimer.schedule(new NpcMoveTimer(), 0, NpcMoveTimer.NPC_MOVE_DELAY);
        }

        while (running) {
            try {
                Thread.sleep(300);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            clearMap();
            displayMap();
        }
    }

    private void displayMap() {
        System.out.print("\033[H\033[2J");
        String[][] map = gameEngine.getMap().getAsString();
        for (String row[] : map) {
            for (String s : row) {
                System.out.print(s + " ");
            }
            System.out.println();
        }
    }

    private void clearMap() {
        gameEngine.getMap().clearJunk();
    }

    public void stopListening() {
        this.running = false;
    }

    public void validateNewUserName(String username) throws UserAlreadyExistsException {

        for (User u : gameEngine.getUserList()) {
            if (u.getPlayer().getUsername().equals(username)) {
                throw new UserAlreadyExistsException();
            }
        }
    }

    public GameEngine getGameEngine() {
        return gameEngine;
    }
}

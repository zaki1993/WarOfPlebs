package com.bloodplebs.zaki.server.manager;

import com.bloodplebs.zaki.engine.GameEngine;
import com.bloodplebs.zaki.server.User;
import com.bloodplebs.zaki.server.exception.UserAlreadyExistsException;

import java.util.Timer;
import java.util.TimerTask;

public class GameManager extends Thread {

    private class NpcUpdateTimer extends TimerTask {

        public NpcUpdateTimer() {
        }

        public static final long NPC_SPAWN_DELAY = 15_000;

        @Override
        public void run() {
            gameEngine.getMap().spawnItems();
            gameEngine.getMap().spawnNpc();
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
            Timer t = new Timer();
            t.schedule(new NpcUpdateTimer(), 0, NpcUpdateTimer.NPC_SPAWN_DELAY);
        }

        while (running) {
            try {
                Thread.sleep(1_000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            clearMap();
            displayMap();
        }
    }

    private void clearMap() {
        gameEngine.getMap().clearJunk();
    }

    private void displayMap() {
        //System.out.print("\033[H\033[2J");
        //gameEngine.getMap().print();
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

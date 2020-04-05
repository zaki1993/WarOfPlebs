package com.warofplebs.zaki.server.listener.impl;

import com.warofplebs.zaki.server.engine.GameEngine;
import com.warofplebs.zaki.server.engine.event.Event;
import com.warofplebs.zaki.server.engine.event.EventFactory;
import com.warofplebs.zaki.server.user.User;
import com.warofplebs.zaki.server.exception.InvalidEventDataException;
import com.warofplebs.zaki.server.listener.Listener;
import com.warofplebs.zaki.server.log.ClientListenerLogger;
import com.warofplebs.zaki.util.MsgUtils;
import org.json.JSONObject;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

public class ClientListener extends Thread implements Listener {

    //private ExecutorService eventHandler = Executors.newFixedThreadPool(3);

    private static final int NOTIFY_INTERVAL_MS = 1_000;

    private boolean gameRunning;

    private GameEngine engine;

    public ClientListener(GameEngine engine) {
        ClientListenerLogger.LOGGER.info("Initializing client listener..!");
        this.gameRunning = false;
        this.engine = engine;
    }

    @Override
    public synchronized void notify(User user) {
        if (engine != null && user != null) {
            engine.connectUser(user);
            ClientListenerLogger.LOGGER.info("User has connected: " + user);
        }
    }

    @Override
    public void listen() {
        ClientListenerLogger.LOGGER.info("Client listener is listening..!");
        this.gameRunning = true;
        start();
    }

    @Override
    public void interupt() {
        this.gameRunning = false;
        ClientListenerLogger.LOGGER.info("Client listener is stopping..!");
    }

    @Override
    public void run() {
        while(gameRunning) {
            try {
                Thread.sleep(NOTIFY_INTERVAL_MS);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            try {
                handleClientEvents();
                notifyClients();
            } catch (IOException e) {
                ClientListenerLogger.LOGGER.severe(e);
            }
        }
    }

    private void handleClientEvents() throws IOException {
        synchronized (engine) {
            ListIterator<User> clientsIterator = engine.getUserList().listIterator();
            List<User> toDisconnect = new ArrayList<>();
            while (clientsIterator.hasNext()) {
                User u = clientsIterator.next();
                Socket client = u.getConnection();
                try {
                    if (client != null) {
                        JSONObject request = new JSONObject();
                        request.put("dummy", true);

                        MsgUtils.sendMessage(client, request);
                        JSONObject response = MsgUtils.receiveMessage(client);

                        // handle disconnect
                        boolean connected = response.getBoolean("connected");
                        if (!connected) {
                            toDisconnect.add(u);
                        }

                        // handle client events
                        boolean hasEvent = response.getBoolean("hasEvent");
                        if (hasEvent) {
                            Event clientEvent = getEvent(response);
                            try {
                                engine.handleClientEvent(clientEvent);
                            } catch (Exception e) {
                                // Event exception
                                ClientListenerLogger.LOGGER.warning(e);
                            }
                        }
                    }
                } catch (InvalidEventDataException iede) {
                    MsgUtils.sendMessage(client, iede);
                } catch (IOException e) {
                    toDisconnect.add(u);
                }
            }
            for (User u : toDisconnect) {
                engine.disconnectUser(u);
                ClientListenerLogger.LOGGER.info("User " + u + " has disconnected..!");
            }
        }
    }

    private Event getEvent(JSONObject response) throws InvalidEventDataException {

        String eventData = response.getString("event");
        JSONObject eventJson = new JSONObject(eventData);
        return EventFactory.buildEvent(eventJson);
    }

    private void notifyClients() throws IOException {
        synchronized (engine) {
            List<User> users = engine.getUserList();
            JSONObject gameInfo = getGameInfo();
            for (User user : users) {
                //ClientListenerLogger.LOGGER.info("Notifying user " + user);
                Socket client = user.getConnection();
                if (client != null) {
                    notifyClient(client, gameInfo);
                }
            }
        }
    }

    private void notifyClient(Socket client, JSONObject gameInfo) throws IOException {
        MsgUtils.sendMessage(client, gameInfo);
    }

    private JSONObject getGameInfo() {

        JSONObject json = new JSONObject();
        JSONObject map = new JSONObject();
        map.put("tiles", engine.getMap().getAsString());
        json.put("map", map);

        return json;
    }
}

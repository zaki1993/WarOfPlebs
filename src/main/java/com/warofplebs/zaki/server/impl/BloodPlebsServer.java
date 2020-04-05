package com.warofplebs.zaki.server.impl;

import com.warofplebs.zaki.server.user.User;
import com.warofplebs.zaki.server.engine.GameEngine;
import com.warofplebs.zaki.server.engine.event.EventFactory;
import com.warofplebs.zaki.server.engine.impl.BloodPlebsGameEngine;
import com.warofplebs.zaki.server.engine.map.object.unit.race.Race;
import com.warofplebs.zaki.server.exception.InvalidRaceException;
import com.warofplebs.zaki.server.exception.InvalidRequestException;
import com.warofplebs.zaki.server.exception.LoginDeniedException;
import com.warofplebs.zaki.server.exception.UserAlreadyExistsException;
import com.warofplebs.zaki.server.listener.Listener;
import com.warofplebs.zaki.server.listener.impl.ClientListener;
import com.warofplebs.zaki.server.log.ServerLogger;
import com.warofplebs.zaki.server.manager.GameManager;
import com.warofplebs.zaki.util.MsgUtils;
import com.warofplebs.zaki.util.Utils;
import org.json.JSONObject;

import java.io.IOException;
import java.net.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class BloodPlebsServer {

    private ExecutorService threadPool = Executors.newFixedThreadPool(5);

    private static final int GAME_PORT = 6060;
    private static final int MAX_PLAYER_QUEUE = 10;

    private ServerSocket serverSocket;
    private Listener clientListener;
    private GameManager gameManager;
    private boolean running;

    public BloodPlebsServer() throws IOException {
        init();
    }

    private void init() throws IOException {
        initServer();
        initListeners();
    }

    private void initListeners() {
        ServerLogger.LOGGER.info("Initializing listeners..!");
        GameEngine engine = new BloodPlebsGameEngine();
        this.gameManager = new GameManager(engine);
        this.clientListener = new ClientListener(engine);

        // This is not a listener, but we will think later for that one
        // TODO
        EventFactory.init(engine);
    }

    private void initServer() throws IOException {
        ServerLogger.LOGGER.info("Initializing server..!");
        this.serverSocket = new ServerSocket(GAME_PORT, MAX_PLAYER_QUEUE, InetAddress.getByName("192.168.0.101"));
        this.running = false;
    }

    public void start() {
        if (!running) {
            running = true;
            ServerLogger.LOGGER.info("Starting server..!");
            startListeners();
            run();
        }
    }

    public void stop() {
        if (running) {
            running = false;
            ServerLogger.LOGGER.info("Stopping server..!");
            stopListeners();
        }
    }

    private void stopListeners() {
        ServerLogger.LOGGER.info("Stopping listeners..!");
        clientListener.interupt();
        gameManager.stopListening();
    }

    private void startListeners() {
        ServerLogger.LOGGER.info("Starting listeners..!");
        clientListener.listen();
        gameManager.start();
    }

    private void run() {

        ServerLogger.LOGGER.info("Server is started running on port " + GAME_PORT);
        while (running) {
            try {
                Socket client = serverSocket.accept();
                threadPool.execute(() -> processClient(client));
            } catch (Exception e) {
                ServerLogger.LOGGER.warning(e);
            }
        }
    }

    private void processClient(Socket client) {
        ServerLogger.LOGGER.info("Connection established with " + client);
        try {
            User u = connectToServer(client);
            if (u != null) {
                notifyListeners(u);
            }
        } catch (Exception e) {
            ServerLogger.LOGGER.severe(e);
            JSONObject response = new JSONObject();
            response.put("error", e.getMessage());
            MsgUtils.sendMessageIgnoreException(client, response);
        }
    }

    private void notifyListeners(User user) {
        clientListener.notify(user);
    }

    public User connectToServer(Socket client) throws IOException, LoginDeniedException, InvalidRequestException, UserAlreadyExistsException, InvalidRaceException {

        JSONObject request = MsgUtils.receiveMessage(client);
        boolean isLogin = request.getBoolean("login");

        User user;

        if (isLogin) {
            String username = request.getString("username");
            validateUsername(username);
            String race = request.getString("race");
            Race r = validateRace(race);
            user = new User(client, username, r);
        } else {
            throw new LoginDeniedException();
        }

        if (user != null) {
            JSONObject response = new JSONObject();
            response.put("loginResult", true);

            // Send some more preloaded information to load client application
            response.put("mapSize", gameManager.getGameEngine().getMap().getSize());

            MsgUtils.sendMessage(client, response);
        }

        return user;
    }

    private Race validateRace(String race) throws InvalidRaceException {
        return Utils.getRaceFromName(race);
    }

    private void validateUsername(String username) throws UserAlreadyExistsException {
        gameManager.validateNewUserName(username);
    }
}

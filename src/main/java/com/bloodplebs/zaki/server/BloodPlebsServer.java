package com.bloodplebs.zaki.server;

import com.bloodplebs.zaki.engine.GameEngine;
import com.bloodplebs.zaki.engine.event.EventFactory;
import com.bloodplebs.zaki.engine.impl.BloodPlebsGameEngine;
import com.bloodplebs.zaki.engine.map.object.unit.race.Race;
import com.bloodplebs.zaki.server.exception.InvalidRaceException;
import com.bloodplebs.zaki.server.exception.InvalidRequestException;
import com.bloodplebs.zaki.server.exception.LoginDeniedException;
import com.bloodplebs.zaki.server.exception.UserAlreadyExistsException;
import com.bloodplebs.zaki.server.listener.Listener;
import com.bloodplebs.zaki.server.listener.impl.ClientListener;
import com.bloodplebs.zaki.server.log.ServerLogger;
import com.bloodplebs.zaki.server.manager.GameManager;
import com.bloodplebs.zaki.util.MsgUtils;
import com.bloodplebs.zaki.util.Utils;
import org.json.JSONObject;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class BloodPlebsServer {

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
        this.serverSocket = new ServerSocket(GAME_PORT, MAX_PLAYER_QUEUE);
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
                processClient(client);
            } catch (Exception e) {
                ServerLogger.LOGGER.warning(e);
            }
        }
    }

    private void processClient(Socket client) throws IOException {
        ServerLogger.LOGGER.info("Connection established with " + client);
        try {
            User u = connectToServer(client);
            if (u != null) {
                notifyListeners(u);
            }
        } catch (IOException e) {
            throw e;
        } catch (Exception e) {
            JSONObject response = new JSONObject();
            response.put("error", e.getMessage());
            MsgUtils.sendMessage(client, response);
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

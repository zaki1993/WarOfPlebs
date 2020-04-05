package com.warofplebs.zaki.client;

import com.warofplebs.zaki.client.exception.InvalidLoginDataException;
import com.warofplebs.zaki.client.gui.ClientCaller;
import com.warofplebs.zaki.client.gui.event.EventType;
import com.warofplebs.zaki.server.engine.map.object.unit.core.Direction;
import com.warofplebs.zaki.json.LoginParser;
import com.warofplebs.zaki.util.MsgUtils;
import javafx.scene.input.KeyCode;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class WarOfPlebsClient extends Thread {

    private static final int GAME_PORT = 6060;

    private Socket client;

    private boolean connected;

    private boolean disconnect;

    private boolean hasEvent;

    private KeyCode code;

    private ClientCaller caller;

    private String username;

    private String race;

    public WarOfPlebsClient(String host, ClientCaller caller) throws IOException {
        this.client = new Socket(host, GAME_PORT);
        this.connected = false;
        this.hasEvent = false;
        this.caller = caller;
    }

    public int login() throws IOException, InvalidLoginDataException {

        this.username = "pesho";
        this.race = "HUMAN";

        // Send login data to server
        JSONObject json = LoginParser.constructLoginData(username, race);
        MsgUtils.sendMessage(client, json);

        // Wait for server response
        JSONObject response = MsgUtils.receiveMessage(client);
        // check whether the login was successful
        connected = response.getBoolean("loginResult");
        if (!connected) {
            throw new InvalidLoginDataException();
        }

        System.out.println("Connected to the server");

        // load map info to prepare application
        return response.getInt("mapSize");
    }

    private void connectToServer() {
        try {
            JSONObject connectivityCheck = MsgUtils.receiveMessage(client);
            if (connectivityCheck.getBoolean("dummy")) {
                JSONObject request = new JSONObject();
                request.put("connected", !disconnect);
                request.put("hasEvent", hasEvent);
                if (hasEvent) {
                    request.put("event", getEventInfo().toString());
                }
                MsgUtils.sendMessage(client, request);
            }
            if (!disconnect) {
                JSONObject serverState = MsgUtils.receiveMessage(client);
                displayMap(serverState.getJSONObject("map"));

                //displayData(serverState);

                // Add listener for key events
                // TODO play
                // request data from server
            } else {
                connected = false;
            }
        } catch (Exception e) {
            // TODO
            e.printStackTrace();
        }
    }

    private void displayMap(JSONObject map) {
        JSONArray tiles = map.getJSONArray("tiles");
        caller.displayMap(tiles);
    }

    public void disconnect() throws IOException {
        disconnect = true;

    }

    public void receiveEvent(KeyCode code) {
        this.hasEvent = true;
        this.code = code;
    }

    private JSONObject getEventInfo() {
        JSONObject eventInfo = new JSONObject();

        EventType eventType = getEventType();
        if (eventType == EventType.MOVE) {
            eventInfo = generateMoveEvent();
        } else {
            eventInfo = generateAttackEvent();
        }

        eventInfo.put("playerName", username);

        return eventInfo;
    }

    private JSONObject generateAttackEvent() {

        JSONObject eventInfo = new JSONObject();

        eventInfo.put("attack", true);

        return eventInfo;
    }

    private JSONObject generateMoveEvent() {

        JSONObject eventInfo = new JSONObject();

        eventInfo.put("move", true);
        Direction direction;
        if (code == KeyCode.LEFT) {
            direction = Direction.LEFT;
        } else if (code == KeyCode.RIGHT) {
            direction = Direction.RIGHT;
        } else if (code == KeyCode.UP) {
            direction = Direction.UP;
        } else {
            direction = Direction.DOWN;
        }

        eventInfo.put("direction", direction);
        eventInfo.put("move", true);

        return eventInfo;
    }

    private EventType getEventType() {

        EventType result = null;

        if (code == KeyCode.LEFT || code == KeyCode.RIGHT || code == KeyCode.UP || code == KeyCode.DOWN) {
            result = EventType.MOVE;
        } else {
            result = EventType.ATTACK;
        }

        return result;
    }

    @Override
    public void run() {
        try {
            while (connected) {
                clearData();
                connectToServer();
            }
        } catch (Exception e) {
            // TODO
        }
    }

    private void clearData() {
        if (hasEvent) {
            hasEvent = false;
        }
    }
}

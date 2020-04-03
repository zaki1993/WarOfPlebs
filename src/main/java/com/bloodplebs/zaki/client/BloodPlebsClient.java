package com.bloodplebs.zaki.client;

import com.bloodplebs.zaki.client.exception.InvalidLoginDataException;
import com.bloodplebs.zaki.json.LoginParser;
import com.bloodplebs.zaki.server.exception.InvalidRequestException;
import com.bloodplebs.zaki.util.MsgUtils;
import org.json.JSONObject;

import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class BloodPlebsClient {

    private static final int GAME_PORT = 6060;

    private Socket client;

    private boolean connected;

    private boolean disconnect;

    public BloodPlebsClient() throws IOException {
        this.client = new Socket("localhost", GAME_PORT);
        this.connected = false;
    }

    public void login() throws IOException, InvalidLoginDataException, InvalidRequestException {

        Scanner sc = new Scanner(System.in);
        System.out.println("Enter username: ");
        String username = sc.nextLine();
        System.out.println("Enter race: ");
        String race = sc.nextLine();

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
    }

    public void join() {
        System.out.println("Connected to server..!");
        // TODO create listener to notify it
        while (connected) {
            try {
                JSONObject connectivityCheck = MsgUtils.receiveMessage(client);
                if (connectivityCheck.getBoolean("dummy")) {
                    JSONObject connectJson = new JSONObject();
                    connectJson.put("connected", !disconnect);
                    MsgUtils.sendMessage(client, connectJson);
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
            }
        }
    }

    private void displayMap(JSONObject map) {
        String tiles = map.getString("tiles");
        System.out.print("\033[H\033[2J");
        System.out.println(tiles);
    }

    public void disconnect() throws IOException {
        disconnect = true;
    }
}

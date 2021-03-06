package com.warofplebs.zaki.server.user;

import com.warofplebs.zaki.server.engine.map.object.unit.impl.Player;
import com.warofplebs.zaki.server.engine.map.object.unit.race.Race;

import java.net.Socket;

public class User {

    private Player player;

    private Socket clientData;

    public User(Socket clientData, String username, Race race) {
        this.clientData = clientData;
        this.player = new Player(username, race);
    }

    public Socket getConnection() {
        return clientData;
    }

    public Player getPlayer() {
        return player;
    }
}

package com.bloodplebs.zaki.engine.impl;

import com.bloodplebs.zaki.engine.GameEngine;
import com.bloodplebs.zaki.engine.event.Event;
import com.bloodplebs.zaki.engine.event.impl.MovePlayerEvent;
import com.bloodplebs.zaki.engine.map.BloorPlebsMap;
import com.bloodplebs.zaki.server.User;

public class BloodPlebsGameEngine extends GameEngine {

    private BloorPlebsMap map;

    public BloodPlebsGameEngine() {
        this.map = new BloorPlebsMap(30);
    }

    public synchronized BloorPlebsMap getMap() {
        return map;
    }

    @Override
    public void handleClientEvent(Event clientEvent) {
        if (clientEvent instanceof MovePlayerEvent) {
            MovePlayerEvent moveEvent = (MovePlayerEvent) clientEvent;
            getMap().movePlayer(moveEvent.getDirection(), moveEvent.getPlayer());
        }
    }

    @Override
    protected void init() {}

    @Override
    public void connectUser(User u) {
        getUserList().add(u);
        getMap().spawnPlayer(u.getPlayer());
    }

    @Override
    public void disconnectUser(User u) {
        getUserList().remove(u);
        getMap().removePlayer(u.getPlayer());
    }
}

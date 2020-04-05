package com.warofplebs.zaki.server.engine.impl;

import com.warofplebs.zaki.client.gui.event.EventType;
import com.warofplebs.zaki.server.engine.GameEngine;
import com.warofplebs.zaki.server.engine.event.Event;
import com.warofplebs.zaki.server.engine.event.impl.AttackPlayerEvent;
import com.warofplebs.zaki.server.engine.event.impl.MovePlayerEvent;
import com.warofplebs.zaki.server.engine.map.BloorPlebsMap;
import com.warofplebs.zaki.server.user.User;
import com.warofplebs.zaki.server.log.ServerLogger;

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
        ServerLogger.LOGGER.info("Received event: " + clientEvent);
        if (clientEvent.getEventType() == EventType.MOVE) {
            MovePlayerEvent moveEvent = (MovePlayerEvent) clientEvent;
            getMap().moveUnit(moveEvent.getDirection(), moveEvent.getPlayer());
        } else if (clientEvent.getEventType() == EventType.ATTACK) {
            AttackPlayerEvent attackPlayerEvent = (AttackPlayerEvent) clientEvent;
            getMap().launchAttack(attackPlayerEvent.getPlayer());
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

package com.bloodplebs.zaki.engine;

import com.bloodplebs.zaki.engine.event.Event;
import com.bloodplebs.zaki.engine.map.BloorPlebsMap;
import com.bloodplebs.zaki.server.User;

import java.util.ArrayList;
import java.util.List;

public abstract class GameEngine {

    private List<User> users;

    public GameEngine() {
        init();
        this.users = new ArrayList<>();
    }

    public List<User> getUserList() {
        return users;
    }

    protected abstract void init();

    public abstract void connectUser(User u);

    public abstract void disconnectUser(User u);

    public abstract BloorPlebsMap getMap();

    public abstract void handleClientEvent(Event clientEvent);
}

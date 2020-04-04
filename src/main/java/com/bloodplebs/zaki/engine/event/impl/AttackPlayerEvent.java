package com.bloodplebs.zaki.engine.event.impl;

import com.bloodplebs.zaki.client.gui.event.EventType;
import com.bloodplebs.zaki.engine.map.object.unit.impl.Player;
import org.json.JSONObject;

public class AttackPlayerEvent extends ClientEvent {

    private Player player;

    public AttackPlayerEvent(Player player, JSONObject eventData) {
        super(eventData);

        this.player = player;
    }

    public Player getPlayer() {
        return player;
    }

    @Override
    public EventType getEventType() {
        return EventType.ATTACK;
    }
}

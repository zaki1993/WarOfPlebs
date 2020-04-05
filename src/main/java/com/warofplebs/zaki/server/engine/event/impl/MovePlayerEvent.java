package com.warofplebs.zaki.server.engine.event.impl;

import com.warofplebs.zaki.client.gui.event.EventType;
import com.warofplebs.zaki.server.engine.map.object.unit.core.Direction;
import com.warofplebs.zaki.server.engine.map.object.unit.impl.Player;
import org.json.JSONObject;

public class MovePlayerEvent extends ClientEvent {

    private Player player;
    private Direction direction;

    public MovePlayerEvent(Player player, Direction direction, JSONObject eventInfo) {
        super(eventInfo);
        this.player = player;
        this.direction = direction;
    }

    public Player getPlayer() {
        return player;
    }

    public Direction getDirection() {
        return direction;
    }

    @Override
    public EventType getEventType() {
        return EventType.MOVE;
    }
}

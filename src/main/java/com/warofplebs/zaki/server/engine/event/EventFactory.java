package com.warofplebs.zaki.server.engine.event;

import com.warofplebs.zaki.server.engine.GameEngine;
import com.warofplebs.zaki.server.engine.event.impl.AttackPlayerEvent;
import com.warofplebs.zaki.server.engine.event.impl.MovePlayerEvent;
import com.warofplebs.zaki.server.engine.map.object.unit.core.Direction;
import com.warofplebs.zaki.server.engine.map.object.unit.impl.Player;
import com.warofplebs.zaki.server.exception.InvalidEventDataException;
import com.warofplebs.zaki.util.Utils;
import org.json.JSONObject;

import java.util.List;
import java.util.stream.Collectors;

public final class EventFactory {

    public static void init(GameEngine engine) {
        EventFactory.engine = engine;
    }

    private static GameEngine engine;

    public static Event buildEvent(JSONObject eventData) throws InvalidEventDataException {

        Event result = null;

        if (eventData.has("move")) {
            if (eventData.getBoolean("move")) {
                Direction direction = Utils.getDirectionFromName(eventData.getString("direction"));
                Player player = Utils.getPlayerByName(getPlayersFromUsers(), eventData.getString("playerName"));
                result = new MovePlayerEvent(player, direction, eventData);
            }
        } else if (eventData.has("attack")) {
            if (eventData.getBoolean("attack")) {
                Player player = Utils.getPlayerByName(getPlayersFromUsers(), eventData.getString("playerName"));
                result = new AttackPlayerEvent(player, eventData);
            }
        }

        return result;
    }

    private static List<Player> getPlayersFromUsers() {
        return engine.getUserList().stream().map(user -> user.getPlayer()).collect(Collectors.toList());
    }
}

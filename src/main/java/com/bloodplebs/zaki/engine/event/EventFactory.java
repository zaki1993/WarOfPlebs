package com.bloodplebs.zaki.engine.event;

import com.bloodplebs.zaki.engine.event.impl.MovePlayerEvent;
import com.bloodplebs.zaki.engine.map.object.unit.core.Direction;
import com.bloodplebs.zaki.engine.map.object.unit.impl.Player;
import com.bloodplebs.zaki.server.exception.InvalidEventDataException;
import com.bloodplebs.zaki.util.Utils;
import org.json.JSONObject;

public final class EventFactory {

    public static Event buildEvent(JSONObject eventData) throws InvalidEventDataException {

        Event result = null;

        if (eventData.getBoolean("move")) {
            Direction direction = Utils.getDirectionFromName(eventData.getString("direction"));
            Player player = Utils.getPlayerByName(eventData.getString("playerName"));
            result = new MovePlayerEvent(player, direction, eventData);
        }

        return result;
    }
}

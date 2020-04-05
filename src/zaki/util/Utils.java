package com.warofplebs.zaki.util;

import com.warofplebs.zaki.server.engine.map.object.unit.core.Direction;
import com.warofplebs.zaki.server.engine.map.object.unit.impl.Player;
import com.warofplebs.zaki.server.engine.map.object.unit.race.Race;
import com.warofplebs.zaki.server.exception.InvalidDirectionException;
import com.warofplebs.zaki.server.exception.InvalidRaceException;

import java.util.List;
import java.util.stream.Collectors;

public final class Utils {

    public static final String USER_DIR = System.getProperty("user.dir");

    public static Race getRaceFromName(String race) throws InvalidRaceException {

        Race result;

        try {
            result = Race.valueOf(race);
        } catch (IllegalArgumentException iae) {
            throw  new InvalidRaceException();
        }

        return result;
    }

    public static Direction getDirectionFromName(String direction) throws InvalidDirectionException{
        Direction result;

        try {
            result = Direction.valueOf(direction);
        } catch (IllegalArgumentException iae) {
            throw new InvalidDirectionException();
        }

        return result;
    }

    public static Player getPlayerByName(List<Player> players, String playerName) {

        List<Player> matches = players.stream().filter(player -> player.getUsername().equals(playerName)).collect(Collectors.toList());

        return matches != null && !matches.isEmpty() ? matches.get(0) : null;
    }

    public static void noop() {
        // Method which does nothing
    }
}

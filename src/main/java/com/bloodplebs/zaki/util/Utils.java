package com.bloodplebs.zaki.util;

import com.bloodplebs.zaki.engine.map.object.unit.core.Direction;
import com.bloodplebs.zaki.engine.map.object.unit.impl.Player;
import com.bloodplebs.zaki.engine.map.object.unit.race.Race;
import com.bloodplebs.zaki.server.User;
import com.bloodplebs.zaki.server.exception.InvalidDirectionException;
import com.bloodplebs.zaki.server.exception.InvalidRaceException;

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
}

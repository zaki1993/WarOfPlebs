package com.warofplebs.zaki.json;

import org.json.JSONObject;

public final class LoginParser {
    public static JSONObject constructLoginData(String username, String race) {
        JSONObject json = new JSONObject();
        json.put("login", true);
        json.put("username", username);
        json.put("race", race);

        return json;
    }
}

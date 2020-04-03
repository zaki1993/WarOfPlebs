package com.bloodplebs.zaki.json;

import org.json.JSONObject;

import java.io.Serializable;

public class Json implements Serializable {

    private String content;

    public Json(JSONObject jsonObject) {
        this.content = jsonObject.toString();
    }

    public JSONObject toJsonObject() {
        return new JSONObject(content);
    }
}

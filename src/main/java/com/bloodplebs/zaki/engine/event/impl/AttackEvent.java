package com.bloodplebs.zaki.engine.event.impl;

import org.json.JSONObject;

public class AttackEvent extends ClientEvent {
    public AttackEvent(JSONObject eventInfo) {
        super(eventInfo);
    }
}

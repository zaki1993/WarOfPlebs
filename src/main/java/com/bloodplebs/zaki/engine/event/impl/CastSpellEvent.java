package com.bloodplebs.zaki.engine.event.impl;

import org.json.JSONObject;

public class CastSpellEvent extends ClientEvent {
    public CastSpellEvent(JSONObject eventInfo) {
        super(eventInfo);
    }
}

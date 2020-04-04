package com.bloodplebs.zaki.engine.event.impl;

import com.bloodplebs.zaki.client.gui.event.EventType;
import org.json.JSONObject;

public class CastSpellEvent extends ClientEvent {
    public CastSpellEvent(JSONObject eventInfo) {
        super(eventInfo);
    }

    @Override
    public EventType getEventType() {
        return EventType.ATTACK;
    }
}

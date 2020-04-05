package com.warofplebs.zaki.server.engine.event.impl;

import com.warofplebs.zaki.client.gui.event.EventType;
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

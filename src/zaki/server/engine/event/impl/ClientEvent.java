package com.warofplebs.zaki.server.engine.event.impl;

import com.warofplebs.zaki.server.engine.event.Event;
import org.json.JSONObject;

public abstract class ClientEvent implements Event {

    private JSONObject eventInfo;

    public ClientEvent(JSONObject eventInfo) {
        this.eventInfo = eventInfo;
    }

    @Override
    public JSONObject getEventInfo() {
        return eventInfo;
    }
}

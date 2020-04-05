package com.warofplebs.zaki.server.engine.event;

import com.warofplebs.zaki.client.gui.event.EventType;
import org.json.JSONObject;

public interface Event {
    JSONObject getEventInfo();
    EventType getEventType();
}

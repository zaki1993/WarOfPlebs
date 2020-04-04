package com.bloodplebs.zaki.engine.event;

import com.bloodplebs.zaki.client.gui.event.EventType;
import org.json.JSONObject;

public interface Event {
    JSONObject getEventInfo();
    EventType getEventType();
}

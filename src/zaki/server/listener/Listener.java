package com.warofplebs.zaki.server.listener;

import com.warofplebs.zaki.server.user.User;

public interface Listener {
    void interupt();
    void listen();
    void notify(User user);
}

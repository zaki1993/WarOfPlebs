package com.bloodplebs.zaki.server.listener;

import com.bloodplebs.zaki.server.User;

import java.net.Socket;

public interface Listener {
    void interupt();
    void listen();
    void notify(User user);
}

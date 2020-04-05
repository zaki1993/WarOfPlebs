package com.warofplebs.zaki.server;

import com.warofplebs.zaki.server.impl.BloodPlebsServer;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {

        BloodPlebsServer server = new BloodPlebsServer();
        server.start();
    }
}

package com.bloodplebs.zaki;

import com.bloodplebs.zaki.server.BloodPlebsServer;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {

        BloodPlebsServer server = new BloodPlebsServer();
        server.start();
    }
}

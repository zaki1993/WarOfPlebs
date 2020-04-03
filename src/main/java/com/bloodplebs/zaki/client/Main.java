package com.bloodplebs.zaki.client;

import com.bloodplebs.zaki.client.exception.InvalidLoginDataException;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException, InvalidLoginDataException {

        BloodPlebsClient client = new BloodPlebsClient();
        client.login();
        try {
            client.join();
        } finally {
            client.disconnect();
        }
    }
}

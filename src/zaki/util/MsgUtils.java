package com.warofplebs.zaki.util;

import com.warofplebs.zaki.json.Json;
import com.warofplebs.zaki.server.exception.InvalidRequestException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public final class MsgUtils {

    public static void sendMessage(Socket client, Throwable err) throws IOException {

        JSONObject message = new JSONObject();
        message.put("error", err);

        sendMessage(client, message);
    }

    public static void sendMessage(Socket client, JSONObject message) throws IOException {

        ObjectOutputStream out = new ObjectOutputStream(client.getOutputStream());

        out.writeObject(new Json(message));
        out.flush();
    }

    public static JSONObject receiveMessage(Socket client) throws IOException {

        ObjectInputStream in = new ObjectInputStream(client.getInputStream());

        JSONObject request;
        try {
            request = ((Json) in.readObject()).toJsonObject();
        } catch (ClassNotFoundException e) {
            throw new InvalidRequestException();
        }

        return request;
    }

    public static void sendMessageIgnoreException(Socket client, JSONObject response) {
        try {
            sendMessage(client, response);
        } catch (Exception e) {
            Utils.noop();
        }
    }
}

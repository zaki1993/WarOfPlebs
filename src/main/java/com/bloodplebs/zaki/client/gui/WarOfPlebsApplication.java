package com.bloodplebs.zaki.client.gui;

import com.bloodplebs.zaki.client.WarOfPlebsClient;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class WarOfPlebsApplication extends Application implements ClientCaller {

    private static final int TILE_WIDTH = 24;

    private static final int TILE_HEIGHT = 24;

    private static final int STATUS_BAR_HEIGHT = 1;

    private static Map<Character, String> tilesMap = new HashMap<>();

    static {
        // Add wall
        tilesMap.put('#', "resources/wall.png");
        // Add path
        tilesMap.put('_', "resources/path.png");
        // Add npc
        tilesMap.put('N', "resources/npc.png");
        // Add items
        tilesMap.put('I', "resources/item.png");
        // Add player
        tilesMap.put('P', "resources/player.png");
        // Add status bar
        tilesMap.put('S', "resources/statusbar.png");
    }

    private Scene sc;

    private GridPane playground;

    private WarOfPlebsClient client;

    @Override
    public void start(Stage stage) throws Exception {

        Scanner scanner = new Scanner(System.in);
        String host = "localhost";//scanner.nextLine();

        this.client = new WarOfPlebsClient(host, this);
        int mapSize = client.login();
        client.start();

        stage.setTitle("War of plebs");

        // create a stack pane
        playground = new GridPane();

        // create a scene with default width and height
        this.sc = new Scene(playground, mapSize * TILE_WIDTH, mapSize * (TILE_HEIGHT + STATUS_BAR_HEIGHT) - 6);

        stage.setResizable(false);

        sc.setOnKeyPressed(event -> {
            switch (event.getCode()) {
                case UP:
                case LEFT:
                case RIGHT:
                case DOWN:
                case SPACE:
                    client.receiveEvent(event.getCode());
                    break;
            }
        });

        // set the scene
        stage.setScene(sc);
        stage.show();
    }

    public static void main(String[] args) {

        Application.launch(args);
    }

    @Override
    public void displayMap(String tiles) {
        Platform.runLater(() -> {
            try {
                clearMap();
                paintMap(tiles);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        });

    }

    private void clearMap() {
        playground.getChildren().removeAll(playground.getChildren());
    }

    private void paintMap(String tiles) throws FileNotFoundException {


        String[] tileRows = tiles.split("\n");

        // status bar
        for (int i = 0; i < tileRows.length; i++) {
            ImageView img = getImageFromTile('S');
            if (img != null) {
                playground.add(img, i, 0);
            }
        }

        for (int x = 0; x < tileRows.length; x++) {
            for (int y = 0; y < tileRows[x].length(); y++) {
                char tile = tileRows[x].charAt(y);
                ImageView img = getImageFromTile(tile);
                if (img != null) {
                    playground.add(img, x, y + STATUS_BAR_HEIGHT);
                }
            }
        }
    }

    private ImageView getImageFromTile(char tile) throws FileNotFoundException {

        String resource = tilesMap.get(tile);
        if (resource != null) {
            InputStream fileStream = new FileInputStream(resource);
            if (fileStream != null) {
                Image img = new Image(fileStream);
                if (img != null) {
                    ImageView tileImg = new ImageView(img);
                    if (tileImg != null) {
                        tileImg.setPreserveRatio(true);
                        return tileImg;
                    }
                }
            }
        }

        return null;
    }
}

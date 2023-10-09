package team.firestorm.converterhandhistory;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import team.firestorm.converterhandhistory.ggpokerok.FileManager;
import team.firestorm.converterhandhistory.googlesheets.GSheetsService;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FXMain extends Application {
    static Map<String, String> nicknameMap = new HashMap<>();

    public static Map<String, String> getMap() {
        return nicknameMap;
    }

    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage stage) throws GeneralSecurityException, IOException {
        GSheetsService sheetsService = new GSheetsService();
        List<List<Object>> objectList = sheetsService.getData();
        nicknameMap = sheetsService.createMap(objectList);

        FileManager fileManager = new FileManager();
        fileManager.getNicknameConference(nicknameMap);

        FXMLLoader fxmlLoader = new FXMLLoader(FXMain.class.getResource("FXMLConfig.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("FireStorm Team GGPokerOK Hand History Converter");
        stage.getIcons().add(new Image("team/firestorm/converterhandhistory/FS.png"));
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }
}
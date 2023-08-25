package team.firestorm.converterhandhistory;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import team.firestorm.converterhandhistory.googlesheets.GSheetsService;

import java.io.IOException;
import java.security.GeneralSecurityException;

public class FXMain extends Application {
    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage stage) throws IOException, GeneralSecurityException {
        GSheetsService.getNicknames();

        FXMLLoader fxmlLoader = new FXMLLoader(FXMain.class.getResource("FXMLConfig.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("FireStorm Team GGPokerOK Hand History Converter");
        stage.getIcons().add(new Image("team/firestorm/converterhandhistory/FS.png"));
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }
}
package team.firestorm.converterhandhistory;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import team.firestorm.converterhandhistory.ggpokerok.FileManager;
import team.firestorm.converterhandhistory.ggpokerok.TextOperator;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class Controller implements Initializable {
    @FXML
    private Button btnOpenFile;
    @FXML
    private Button btnConvert;
    @FXML
    private TextField textPath = new TextField();
    @FXML
    private TextField textSetNickname = new TextField();

    private List<File> fileList = new ArrayList<>();

    private FileManager fileManager = new FileManager();

    private File selectedDirectory;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        textSetNickname.textProperty().addListener((observable, oldValue, newValue) -> {
        });

        btnOpenFile.setOnAction(event -> {
            DirectoryChooser directoryChooser = new DirectoryChooser();
            directoryChooser.setTitle("FireStorm Team Hand History Converter");
            selectedDirectory = directoryChooser.showDialog(new Stage());
            if (selectedDirectory != null) {
                textPath.setText(selectedDirectory.getPath());
                fileList = fileManager.getFilesFromDirectory(selectedDirectory);
            }
        });

        btnConvert.setOnAction(event -> {
            FileManager fileManager = new FileManager();
            TextOperator textOperator = new TextOperator();
            fileList.clear();
            fileList = fileManager.getFilesFromDirectory(selectedDirectory);
            for (File file : fileList) {
                List<String> list = fileManager.read(file);
                textOperator.replaceNickname(list, textSetNickname.getText());
                textOperator.replaceWordWon(list);
                textOperator.deleteStringDealt(list);
                fileManager.write(list, file);
            }
        });
    }
}
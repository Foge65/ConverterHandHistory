package team.firestorm.converterhandhistory;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import team.firestorm.converterhandhistory.ggpokerok.FileManager;
import team.firestorm.converterhandhistory.ggpokerok.Roman;
import team.firestorm.converterhandhistory.ggpokerok.TextOperator;

import java.io.File;
import java.net.URL;
import java.util.*;
import java.util.prefs.Preferences;

public class Controller implements Initializable {
    @FXML
    private Button btnOpenFile;
    @FXML
    private Button btnConvert;
    @FXML
    private TextField textPath = new TextField();

    private ObservableList<String> observableListNickname = FXCollections.observableArrayList(FileManager.getConferenceName());

    @FXML
    private ComboBox<String> boxListNickname = new ComboBox<>(observableListNickname);

    private List<File> fileList = new ArrayList<>();

    private File selectedDirectory;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        boxListNickname.setItems(observableListNickname);

        boxListNickname.getEditor().addEventFilter(KeyEvent.KEY_RELEASED, event -> {
            String filter = boxListNickname.getEditor().getText();
            filterComboBoxItems(boxListNickname, filter);

            if (event.getCode() == KeyCode.BACK_SPACE) {
                boxListNickname.getSelectionModel().clearSelection();
            }
        });

        boxListNickname.setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.PRIMARY) {
                if (!boxListNickname.isShowing()) {
                    boxListNickname.getEditor().clear();
                    boxListNickname.getSelectionModel().clearSelection();
                    boxListNickname.show();
                }
            }
        });

        btnOpenFile.setOnAction(event -> {
            DirectoryChooser directoryChooser = new DirectoryChooser();
            directoryChooser.setTitle("Please, select a folder");
            Preferences preferences = Preferences.userNodeForPackage(getClass());
            String lastDirectory = preferences.get("lastDirectory", System.getProperty("user.home"));
            File initialDirectory = new File(lastDirectory);
            if (!initialDirectory.exists() || !initialDirectory.isDirectory()) {
                initialDirectory = new File(System.getProperty("user.home"));
            }
            directoryChooser.setInitialDirectory(initialDirectory);
            selectedDirectory = directoryChooser.showDialog(new Stage());
            if (selectedDirectory != null) {
                textPath.setText(selectedDirectory.getPath());
                preferences.put("lastDirectory", selectedDirectory.getAbsolutePath());
            }
        });

        btnConvert.setOnAction(event -> {
            FileManager fileManager = new FileManager();
            TextOperator textOperator = new TextOperator();
            if (boxListNickname.getEditor().getText() == "") {
                Alert setNickname = new Alert(Alert.AlertType.INFORMATION);
                setNickname.setTitle("Invalid nickname");
                setNickname.setHeaderText(null);
                setNickname.setContentText("Please, enter nickname for replacement!");
                setNickname.showAndWait();
            } else {
                fileList.clear();
                fileList = fileManager.getFilesFromDirectory(selectedDirectory);
                Map<String, String> mapNicknames = FXMain.getMap();
                String selectedConference = boxListNickname.getEditor().getText();
                String nicknameGGPokerOK = null;
                for (Map.Entry<String, String> entry : mapNicknames.entrySet()) {
                    String k = entry.getKey();
                    String v = entry.getValue();
                    if (selectedConference.equals(k)) {
                        nicknameGGPokerOK = v;
                        break;
                    }
                }
                for (File file : fileList) {
                    List<String> stringList = fileManager.readFileHandHistory(file);
                    textOperator.replaceWordWon(stringList);
                    Roman.replaceNumber(stringList);
                    textOperator.deleteStringDealt(stringList);
                    textOperator.replaceNickname(stringList, nicknameGGPokerOK);
                    fileManager.write(stringList, file, selectedDirectory);
                }
                Alert completeOperation = new Alert(Alert.AlertType.INFORMATION);
                completeOperation.setTitle("Complete");
                completeOperation.setHeaderText(null);
                completeOperation.setContentText("Complete");
                completeOperation.showAndWait();
            }
        });

    }

    private void filterComboBoxItems(ComboBox<String> comboBox, String filter) {
        ObservableList<String> filteredItems = FXCollections.observableArrayList();

        for (String item : observableListNickname) {
            if (item.toLowerCase().contains(filter.toLowerCase())) {
                filteredItems.add(item);
            }
        }
        comboBox.setItems(filteredItems);
        if (!comboBox.isShowing()) {
            comboBox.show();
        }
    }

}

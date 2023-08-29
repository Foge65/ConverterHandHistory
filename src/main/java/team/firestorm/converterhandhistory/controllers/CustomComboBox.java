package team.firestorm.converterhandhistory.controllers;

import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public class CustomComboBox<T> extends ComboBox<T> {
    ObservableList<String> observableListNickname;
    public CustomComboBox(ObservableList<String> observableListNickname) {
        super();
        setEditable(true);
        setupEventHandlers();
        this.observableListNickname = observableListNickname;
    }

    private void setupEventHandlers() {
        getEditor().addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode() == KeyCode.BACK_SPACE && getEditor().getCaretPosition() == 0) {
                event.consume();
            }
        });
    }
}

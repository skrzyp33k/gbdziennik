package com.gbsdevelopers.gbdziennik.admin;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class RemoveTeacherController {

    @FXML
    private TextField teacherIDTextField;

    @FXML
    private Button removeButton;

    @FXML
    void removeButtonClicked(ActionEvent event) {

        ((Stage)(((Node) event.getSource()).getScene().getWindow())).close();
    }

}
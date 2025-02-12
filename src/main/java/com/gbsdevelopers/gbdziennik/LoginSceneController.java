package com.gbsdevelopers.gbdziennik;

import com.gbsdevelopers.gbssocket.GbsMessage;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.Vector;

/**
 * Controller for LoginScene
 */
public class LoginSceneController implements Initializable {

    /**
     * Password field.
     */
    @FXML
    private PasswordField passwordPasswordField;

    /**
     * Background image.
     */
    @FXML
    private ImageView backgroundImage;

    /**
     * Login info.
     */
    @FXML
    private Label loginInfo;

    /**
     * Login button.
     */
    @FXML
    private Button loginButton;

    /**
     * Login field.
     */
    @FXML
    private TextField loginTextField;

    /**
     * Logger for log4j2
     */
    private static final Logger logger = LogManager.getLogger(LoginSceneController.class);

    /**
     * LoginButton action handler.
     *
     * @param event Event that invoked action.
     * @throws IOException Throws when can not find or load resources.
     */
    @FXML
    private void loginButtonAction(ActionEvent event) throws IOException {
        logger.info("Clicked loginButton");

        loginInfo.setText("");
        Program.loggedUser = "";
        Program.loggedID = "";

        Vector<String> args = new Vector<>();

        args.add(loginTextField.getText());
        args.add(GbsMessage.MD5(passwordPasswordField.getText()));

        GbsMessage message = new GbsMessage("_loginUser", args);

        GbsMessage reply = Program.socket.executeRequest(message);

        if (reply.header.equals("0")) {
            Program.loggedUser = reply.arguments.get(0);
            Program.loggedPerms = reply.arguments.get(1);
            Program.loggedID = reply.arguments.get(2);

            Stage newStage = new Stage();

            if (reply.arguments.get(1).equals("a")) {
                FXMLLoader fxmlLoader = new FXMLLoader(Program.class.getResource("fxml/admin/adminMainScene.fxml"));
                Scene scene = new Scene(fxmlLoader.load(), 1280, 720);
                newStage.setTitle("GBDziennik - Panel administratora. Zalogowany jako: " + reply.arguments.get(0));
                newStage.setScene(scene);
            } else {
                FXMLLoader fxmlLoader = new FXMLLoader(Program.class.getResource("fxml/user/userMainScene.fxml"));
                Scene scene = new Scene(fxmlLoader.load(), 1280, 720);
                switch (reply.arguments.get(1)) {
                    case "u" -> //uczen
                            newStage.setTitle("GBDziennik - Panel ucznia. Zalogowany jako: " + reply.arguments.get(0));
                    case "r" -> //rodzic
                            newStage.setTitle("GBDziennik - Panel rodzica. Zalogowany jako: " + reply.arguments.get(0));
                    case "n" -> //nauczyciel
                            newStage.setTitle("GBDziennik - Panel nauczyciela. Zalogowany jako: " + reply.arguments.get(0));
                }
                newStage.setScene(scene);
            }

            newStage.getIcons().add(new Image(Objects.requireNonNull(Program.class.getResourceAsStream("img/icon.png"))));
            newStage.setResizable(false);
            newStage.show();

            loginTextField.clear();
            passwordPasswordField.clear();

            logger.info("Logged as: " + Program.loggedUser);

            ((Stage) (((Node) event.getSource()).getScene().getWindow())).close();
        } else {
            loginInfo.setText("Nieprawidłowe dane logowania!");
            logger.warn("Wrong login credentials!");
        }
    }

    /**
     * Initialize window.
     *
     * @param location  URL location.
     * @param resources Resource bundle.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        logger.info("Initialize window");

        loginTextField.setOnKeyPressed(keyEvent -> {
            if (keyEvent.getCode().equals(KeyCode.ENTER)) {
                loginButton.fire();
            }
        });

        passwordPasswordField.setOnKeyPressed(keyEvent -> {
            if (keyEvent.getCode().equals(KeyCode.ENTER)) {
                loginButton.fire();
            }
        });

        backgroundImage.setImage(new Image(Objects.requireNonNull(Program.class.getResourceAsStream("img/background.png"))));
    }
}

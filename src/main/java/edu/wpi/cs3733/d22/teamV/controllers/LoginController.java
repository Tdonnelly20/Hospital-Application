package edu.wpi.cs3733.d22.teamV.controllers;

import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class LoginController extends Controller {

  public LoginController() {}

  @Override
  public void start(Stage primaryStage) throws Exception {}

  @FXML private Button button;
  @FXML private Label wrongLogIn;
  @FXML private TextField username;
  @FXML private PasswordField password;

  public void userLogIn(ActionEvent event) throws IOException {
    checkLogin(event);
  }

  private void checkLogin(ActionEvent event) throws IOException {
    if (username.getText().toString().equals("admin")
        && password.getText().toString().equals("admin")) {
      wrongLogIn.setText("Success!");
      switchToHome(event);
    } else if (username.getText().isEmpty() && password.getText().isEmpty()) {
      wrongLogIn.setText("Please enter your data.");
    } else {
      wrongLogIn.setText("Wrong username or password!");
    }
  }
}

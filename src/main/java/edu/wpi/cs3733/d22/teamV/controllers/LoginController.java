package edu.wpi.cs3733.d22.teamV.controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
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

  private Map<String, String> UserTable = Map.of("admin", "admin");

  private void checkLogin(ActionEvent event) throws IOException {


    for (Map.Entry<String, String> set : UserTable.entrySet()) {
      if (username.getText().toString().equals(set.getKey())
          && password.getText().toString().equals(set.getValue())) {
        wrongLogIn.setText("Success!");
        switchToHome(event);
      } else if (username.getText().isEmpty() && password.getText().isEmpty()) {
        wrongLogIn.setText("Please enter your data.");
      } else {
        wrongLogIn.setText("Wrong username or password!");
      }
    }
  }
}

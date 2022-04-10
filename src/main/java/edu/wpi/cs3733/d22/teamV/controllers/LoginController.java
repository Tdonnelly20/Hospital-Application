package edu.wpi.cs3733.d22.teamV.controllers;

import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class LoginController extends Controller {

  public LoginController() {}

  @Override
  public void start(Stage primaryStage) throws Exception {
    iv.fitWidthProperty().bind(pane.widthProperty());
    iv.fitHeightProperty().bind(pane.heightProperty());
  }

  @FXML private Button button;
  @FXML private Label wrongLogIn;
  @FXML private TextField username;
  @FXML private PasswordField password;
  @FXML private ImageView iv;
  @FXML private Pane pane;

  @FXML
  public void userLogin(ActionEvent event) throws IOException {
    checkLogin(event);
  }

  // private Map<String, String> UserTable = Map.of("admin", "admin", "staff", "staff");

  private void checkLogin(ActionEvent event) throws IOException {

    if (username.getText().toString().equals("admin")
        && password.getText().toString().equals("admin")) {
      // wrongLogIn.setText("Success!");
      System.out.println("Success!");
      switchToHome(event);
    } else if (username.getText().isEmpty() && password.getText().isEmpty()) {
      wrongLogIn.setText("Please enter your data.");
    } else {
      wrongLogIn.setText("Wrong username or password!");
    }
  }
}

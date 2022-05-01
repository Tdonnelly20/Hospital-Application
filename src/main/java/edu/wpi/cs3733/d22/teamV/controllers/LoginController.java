package edu.wpi.cs3733.d22.teamV.controllers;

import com.jfoenix.controls.JFXButton;
import edu.wpi.cs3733.d22.teamV.face.Camera;
import edu.wpi.cs3733.d22.teamV.main.Vdb;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class LoginController extends Controller {
  @FXML private Button button;
  @FXML private TextField connectionFail;
  @FXML private Label wrongLogin;
  @FXML private TextField username;
  @FXML private PasswordField password;
  @FXML private ImageView iv;
  @FXML private Pane pane;
  @FXML private ComboBox<String> dBMenu;
  @FXML private TextField dbIP;
  @FXML private TextField dbPath;
  @FXML private Button dbButton;
  @FXML private Group group;
  @FXML private JFXButton jfxButton;

  public LoginController() {}

  @FXML private ImageView faceImage;
  private Camera camera;

  @Override
  public void init() {
    Camera.stopAcquisition();
    // System.out.println("Login init");

    ChangeListener<Number> listener =
        new ChangeListener<Number>() {
          @Override
          public void changed(
              ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
            double w = pane.getWidth();
            double h = pane.getHeight();
            if (w / 3 * 2 > h) {
              iv.setFitWidth(w);
              iv.setFitHeight(w / 3 * 2);
            }
            if (h / 2 * 3 > w) {
              iv.setFitWidth(h / 2 * 3);
              iv.setFitHeight(h);
            }
            group.setLayoutX(w / 2 - 249);
            group.setLayoutY(h / 2 - 189);
            faceImage.setLayoutX(w / 2 - 112);
          }
        };
    pane.widthProperty().addListener(listener);
    pane.heightProperty().addListener(listener);
  }

  public void toggleCamera() {
    if (!Camera.isCameraActive()) {
      faceImage.setVisible(true);
      camera = new Camera(null, faceImage, jfxButton, true);
      camera.setLoginPageController(this);
      camera.toggleCamera();
    } else {
      Camera.stopAcquisition();
      faceImage.setVisible(false);
    }
  }

  @Override
  public void start(Stage primaryStage) throws Exception {
    iv.fitWidthProperty().bind(pane.widthProperty());
    iv.fitHeightProperty().bind(pane.heightProperty());
  }

  @FXML
  public void setDB() {
    System.out.println(dBMenu.getValue().toString());
    if (dBMenu.getValue().toString().equals("Server DB")) {
      Vdb.setIsClient(true);
      dbButton.setOpacity(1);
      dbPath.setOpacity(1);
      dbIP.setOpacity(1);
    } else if (dBMenu.getValue().toString().equals("Embedded DB")) {
      Vdb.setIsClient(false);
      dbButton.setOpacity(0);
      dbPath.setOpacity(0);
      dbIP.setOpacity(0);
      Vdb.setUpConnection();
    } else {
      System.out.println("No database was selected");
      Vdb.setIsClient(false);
      Vdb.setUpConnection();
    }
  }

  @FXML
  public void getDBInfo() throws Exception {
    Vdb.setIP(dbIP.getText());
    Vdb.setServerPath(dbPath.getText());
    System.out.println("IP :" + dbIP.getText());
    System.out.println(dbPath.getText());
    if (Vdb.setUpConnection() == 0) {
      connectionFail.setOpacity(1);
    } else {
      connectionFail.setText("Database found!");
      connectionFail.setOpacity(1);
      dbButton.setOpacity(0);
      dbPath.setOpacity(0);
      dbIP.setOpacity(0);
    }
    dbIP.setText("");
    dbPath.setText("");
  }

  @FXML
  public void keyLogin(KeyEvent event) throws Exception {
    // System.out.println(KeyCode.ENTER);

    if (event.getCode().equals(KeyCode.ENTER)) {
      try {
        Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
        Class.forName("org.apache.derby.jdbc.ClientDriver");
      } catch (ClassNotFoundException e) {
        e.printStackTrace();
      }
      Vdb vdb = new Vdb();
      vdb.setUpConnection();
      vdb.createAllDB();
      checkLogin(event, username.getText());
      // System.out.println("hello");
    } else {
      // System.out.println("else");
    }
  }

  @FXML
  public void userLogin(ActionEvent event) throws Exception {
    try {
      Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
      Class.forName("org.apache.derby.jdbc.ClientDriver");
    } catch (ClassNotFoundException e) {
      e.printStackTrace();
    }
    Vdb vdb = new Vdb();
    Vdb.setUpConnection();
    vdb.createAllDB();
    checkLogin(event, username.getText());
    try {
      camera.grabFrame();
      if (camera.detect()) checkLogin(event, "Jason");
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void checkLogin(Event event, String string) {
    try {
      if (string.equals("admin") && password.getText().equals("admin")) {
        switchToHome(event);
        Camera.stopAcquisition();
      } else if (string.equals("staff") && password.getText().toString().equals("staff")) {
        switchToHome(event);
        Camera.stopAcquisition();
      } else if (string.equals("Jason") && Camera.isCameraActive()) {
        switchToHome(event);
        Camera.stopAcquisition();
      } else if (string.isEmpty() && password.getText().isEmpty()) {
        wrongLogin.setText("Please enter your data.");
      } else {
        wrongLogin.setText("Wrong username or password!");
      }

    } catch (Exception e) {
    }
  }
}

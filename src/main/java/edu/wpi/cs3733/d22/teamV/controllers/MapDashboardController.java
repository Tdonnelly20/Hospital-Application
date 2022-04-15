package edu.wpi.cs3733.d22.teamV.controllers;

import java.io.IOException;
import java.util.Objects;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TreeTableView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javax.swing.text.html.ImageView;

public class MapDashboardController extends Controller {

  private @FXML HBox hBox;
  private @FXML VBox leftVBox;
  private @FXML Pane sideViewPane;
  private @FXML ImageView sideViewImage;
  private @FXML VBox centerVBox;
  private @FXML TreeTableView infoTable;
  private @FXML TreeTableView countTable;
  private @FXML VBox rightVBox;
  private @FXML Pane mapPane;
  private @FXML ImageView mapImage;
  private @FXML TreeTableView alertTable;

  public MapDashboardController() throws IOException {}

  @Override
  public void start(Stage primaryStage) throws Exception {}

  public void goBack(MouseEvent event) throws IOException {
    Parent root =
        FXMLLoader.load(
            Objects.requireNonNull(getClass().getClassLoader().getResource("FXML/home.fxml")));
    PopupController.getController().closePopUp();
    Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
    Scene scene = new Scene(root);
    stage.setScene(scene);
    stage.show();
  }

  public void goExit(MouseEvent mouseEvent) {
    stop();
  }

  public void goHome(MouseEvent event) throws IOException {
    Parent root =
        FXMLLoader.load(
            Objects.requireNonNull(getClass().getClassLoader().getResource("FXML/home.fxml")));
    PopupController.getController().closePopUp();
    Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
    Scene scene = new Scene(root);
    stage.setScene(scene);
    stage.show();
  }
}

package edu.wpi.cs3733.d22.teamV.controllers;

import javafx.fxml.FXML;
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

  @Override
  public void start(Stage primaryStage) throws Exception {}

  public void goBack(MouseEvent mouseEvent) {}

  public void goExit(MouseEvent mouseEvent) {}

  public void goHome(MouseEvent mouseEvent) {}
  // still need other HBOX / buttons

}

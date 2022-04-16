package edu.wpi.cs3733.d22.teamV.controllers;

import edu.wpi.cs3733.d22.teamV.manager.MapManager;
import edu.wpi.cs3733.d22.teamV.map.Floor;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TitledPane;
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

  private @FXML Button ll2;
  private @FXML Button ll1;
  private @FXML Button f1;
  private @FXML Button f2;
  private @FXML Button f3;
  private @FXML Button f4;
  private @FXML Button f5;

  private @FXML TitledPane sideViewTPane;
  private @FXML TitledPane infoTPane;
  @FXML TitledPane countsTPane;
  private @FXML TitledPane mapTPane;
  private @FXML TitledPane alertsTPane;

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

  /// STUFF FOR OBSERVER LISTENER PATTERN TO UPDATE ALL DASHBOARD COMPONENTS BY FLOOR BUTTONS

  private Floor curFloor = MapManager.getManager().getFloor("1");

  public class ButtonSubject {
    Button button;
    Floor floor;

    ButtonSubject(Button button, Floor floor) {
      this.button = button;
      this.floor = floor;
    }

    public void setFloor(Floor floor) {
      this.floor = floor;
      updateListeners();
    }
  }

  public class DashboardListener {
    Floor floor;
    TitledPane tPane;

    DashboardListener(Floor floor, TitledPane tPane) {
      this.floor = floor;
      this.tPane = tPane;
    }

    public void setFloor(Floor floor) {
      this.floor = floor;
      sideViewTPane.setText("Floor: " + floor.getFloorName());
    }
  }

  @FXML private ArrayList<ButtonSubject> buttonSubjects = new ArrayList<ButtonSubject>();

  public ArrayList<ButtonSubject> getButtonSubjects() {
    return buttonSubjects;
  }

  @FXML private ArrayList<DashboardListener> listeners = new ArrayList<DashboardListener>();

  public ArrayList getListeners() {
    return listeners;
  }

  public void setUpButtonSubjects() {
    ButtonSubject ll2sub = new ButtonSubject(ll2, MapManager.getManager().getFloor("L2"));
    ButtonSubject ll1sub = new ButtonSubject(ll1, MapManager.getManager().getFloor("L1"));
    ButtonSubject f1sub = new ButtonSubject(f1, MapManager.getManager().getFloor("1"));
    ButtonSubject f2sub = new ButtonSubject(f2, MapManager.getManager().getFloor("2"));
    ButtonSubject f3sub = new ButtonSubject(f3, MapManager.getManager().getFloor("3"));
    ButtonSubject f4sub = new ButtonSubject(f4, MapManager.getManager().getFloor("4"));
    ButtonSubject f5sub = new ButtonSubject(f5, MapManager.getManager().getFloor("5"));

    buttonSubjects.add(ll2sub);
    buttonSubjects.add(ll1sub);
    buttonSubjects.add(f1sub);
    buttonSubjects.add(f2sub);
    buttonSubjects.add(f3sub);
    buttonSubjects.add(f4sub);
    buttonSubjects.add(f5sub);
  }

  public void setUpDashboardListeners() {
    DashboardListener sideViewTPanelistener =
        new DashboardListener(MapManager.getManager().getFloor("1"), sideViewTPane);
    DashboardListener infoTPanelistener =
        new DashboardListener(MapManager.getManager().getFloor("1"), infoTPane);
    DashboardListener countsTPanelistener =
        new DashboardListener(MapManager.getManager().getFloor("1"), countsTPane);
    DashboardListener mapTPanelistener =
        new DashboardListener(MapManager.getManager().getFloor("1"), mapTPane);
    DashboardListener alertsTPanelistener =
        new DashboardListener(MapManager.getManager().getFloor("1"), alertsTPane);
    listeners.add(sideViewTPanelistener);
    listeners.add(infoTPanelistener);
    listeners.add(countsTPanelistener);
    listeners.add(mapTPanelistener);
    listeners.add(alertsTPanelistener);
  }

  public void swtichToLL2() {}

  public void updateListeners() {
    for (DashboardListener l : listeners) {
      // l.setFloor(floor);
    }
  }
}

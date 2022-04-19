package edu.wpi.cs3733.d22.teamV.controllers;

import edu.wpi.cs3733.d22.teamV.main.RequestSystem;
import edu.wpi.cs3733.d22.teamV.main.Vdb;
import edu.wpi.cs3733.d22.teamV.manager.MapManager;
import edu.wpi.cs3733.d22.teamV.map.EquipmentIcon;
import edu.wpi.cs3733.d22.teamV.map.Floor;
import edu.wpi.cs3733.d22.teamV.map.Icon;
import edu.wpi.cs3733.d22.teamV.objects.Equipment;
import edu.wpi.cs3733.d22.teamV.objects.Location;
import edu.wpi.cs3733.d22.teamV.objects.Patient;
import edu.wpi.cs3733.d22.teamV.servicerequests.ServiceRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.TreeItemPropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class MapDashboardController extends Controller {

  private @FXML TreeTableView equipmentTable;
  private @FXML TreeTableColumn<Equipment, Integer> equipmentIDCol;
  private @FXML TreeTableColumn<Equipment, String> isDirtyCol;
  private @FXML TreeTableView serviceRequestTable;
  private @FXML TreeTableColumn<ServiceRequest, String> typeCol;
  private @FXML TreeTableColumn<Location, String> locationCol;
  private @FXML TreeTableColumn<ServiceRequest, String> startTimeCol;
  private @FXML TreeTableView patientTable;
  private @FXML TreeTableColumn<Patient, Integer> patientIDCol;
  private @FXML TreeTableColumn<Patient, String> lastCol;
  private @FXML TreeTableColumn<Patient, String> SRCol;
  private @FXML TextArea countsArea = new TextArea();
  private @FXML TextArea alertArea;
  private @FXML Label floorLabel;

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

  private static class SingletonHelper {
    private static final MapDashboardController controller = new MapDashboardController();
  }

  public static MapDashboardController getController() {
    return SingletonHelper.controller;
  }

  @Override
  public void start(Stage primaryStage) throws Exception {}

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
    private Button button;
    private Floor floor;

    public ButtonSubject(Button button, Floor floor) {
      this.button = button;
      this.floor = floor;
    }

    public void setFloor(Floor floor) {
      this.floor = floor;
    }
  }

  public class DashboardListener {
    private Floor floor;
    private TitledPane tPane;

    public DashboardListener(Floor floor, TitledPane tPane) {
      this.floor = floor;
      this.tPane = tPane;
    }

    public void setFloor(Floor floor) {
      this.floor = floor;
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
    DashboardListener sideViewTPaneListener =
        new DashboardListener(MapManager.getManager().getFloor("1"), sideViewTPane);
    DashboardListener infoTPaneListener =
        new DashboardListener(MapManager.getManager().getFloor("1"), infoTPane);
    DashboardListener countsTPaneListener =
        new DashboardListener(MapManager.getManager().getFloor("1"), countsTPane);
    DashboardListener mapTPaneListener =
        new DashboardListener(MapManager.getManager().getFloor("1"), mapTPane);
    DashboardListener alertsTPaneListener =
        new DashboardListener(MapManager.getManager().getFloor("1"), alertsTPane);
    listeners.add(sideViewTPaneListener);
    listeners.add(infoTPaneListener);
    listeners.add(countsTPaneListener);
    listeners.add(mapTPaneListener);
    listeners.add(alertsTPaneListener);
  }

  @FXML
  public void switchToLL2() {
    curFloor = MapManager.getManager().getFloor("L2");
    updateListeners(curFloor);
    updateAll();
    floorLabel.setText("Lower Level 2");
  }

  @FXML
  public void switchToLL1() {
    curFloor = MapManager.getManager().getFloor("L1");
    updateListeners(curFloor);
    updateAll();
    floorLabel.setText("Lower Level 1");
  }

  @FXML
  public void switchToF1() {
    curFloor = MapManager.getManager().getFloor("1");
    updateListeners(curFloor);
    updateAll();
    floorLabel.setText("Floor 1");
  }

  @FXML
  public void switchToF2() {
    curFloor = MapManager.getManager().getFloor("2");
    updateListeners(curFloor);
    updateAll();
    floorLabel.setText("Floor 2");
  }

  @FXML
  public void switchToF3() {
    curFloor = MapManager.getManager().getFloor("3");
    updateListeners(curFloor);
    updateAll();
    floorLabel.setText("Floor 3");
  }

  @FXML
  public void switchToF4() {
    curFloor = MapManager.getManager().getFloor("4");
    updateListeners(curFloor);
    updateAll();
    floorLabel.setText("Floor 4");
  }

  @FXML
  public void switchToF5() {
    curFloor = MapManager.getManager().getFloor("5");
    updateListeners(curFloor);
    updateAll();
    floorLabel.setText("Floor 5");
  }

  @FXML
  public void updateListeners(Floor floor) {
    for (DashboardListener l : listeners) {
      l.setFloor(floor);
    }
  }

  @FXML
  private void updateEquipmentTable() {
    equipmentIDCol.setCellValueFactory(new TreeItemPropertyValueFactory("ID"));
    isDirtyCol.setCellValueFactory(new TreeItemPropertyValueFactory("isDirtyString"));

    ArrayList<Equipment> currEquipment = Vdb.requestSystem.getEquipment();
    ArrayList<TreeItem> treeItems = new ArrayList<>();

    if (!currEquipment.isEmpty()) {

      for (Equipment pos : currEquipment) {
        if (pos.getFloor().equals(curFloor.getFloorName())) {
          TreeItem<Equipment> item = new TreeItem(pos);
          treeItems.add(item);
        }
      }

      equipmentTable.setShowRoot(false);
      TreeItem root = new TreeItem(RequestSystem.getSystem().getEquipment().get(0));
      equipmentTable.setRoot(root);
      root.getChildren().addAll(treeItems);
    }
  }

  @FXML
  private void updateServiceRequestTable() {
    typeCol.setCellValueFactory(new TreeItemPropertyValueFactory("type"));
    locationCol.setCellValueFactory(new TreeItemPropertyValueFactory("shortName"));
    startTimeCol.setCellValueFactory(new TreeItemPropertyValueFactory("timestamp"));

    ArrayList<ServiceRequest> currRequests =
        (ArrayList<ServiceRequest>) Vdb.requestSystem.getEveryServiceRequest();
    ArrayList<TreeItem> treeItems = new ArrayList<>();

    if (!currRequests.isEmpty()) {

      for (ServiceRequest pos : currRequests) {
        if (pos.getLocation() != null) {
          if (pos.getLocation().getFloor() != null) {
            if (pos.getLocation().getFloor().equals(curFloor.getFloorName())) {
              TreeItem<ServiceRequest> item = new TreeItem(pos);
              treeItems.add(item);
            }
          }
        }
      }

      serviceRequestTable.setShowRoot(false);
      TreeItem root = new TreeItem(RequestSystem.getSystem().getEveryServiceRequest().get(0));
      serviceRequestTable.setRoot(root);
      root.getChildren().addAll(treeItems);
    }
  }

  @FXML
  private void updatePatientTable() {
    patientIDCol.setCellValueFactory(new TreeItemPropertyValueFactory("patientID"));
    lastCol.setCellValueFactory(new TreeItemPropertyValueFactory("lastName"));
    SRCol.setCellValueFactory(new TreeItemPropertyValueFactory("serviceIDs"));
    ArrayList<Patient> currPatients = Vdb.requestSystem.getPatients();
    ArrayList<TreeItem> treeItems = new ArrayList<>();

    if (!currPatients.isEmpty()) {

      for (Patient pos : currPatients) {
        for (ServiceRequest s : RequestSystem.getSystem().getEveryServiceRequest()) {
          for (int i : pos.getServiceIDs()) {
            if (i == s.getServiceID()
                && s.getLocation().getFloor().equals(curFloor.getFloorName())) {
              TreeItem<Patient> item = new TreeItem(pos);
              treeItems.add(item);
            }
          }
        }
      }

      patientTable.setShowRoot(false);
      TreeItem root = new TreeItem(RequestSystem.getSystem().getPatients().get(0));
      patientTable.setRoot(root);
      root.getChildren().addAll(treeItems);
    }
  }

  @FXML
  public void updateCounts() {
    curFloor = MapManager.getManager().getFloor(curFloor.getFloorName());
    int srCount = 0;
    int dirty = 0;
    int clean = 0;

    for (Equipment equipment : RequestSystem.getSystem().getEquipment()) {
      if (equipment.getFloor().equals(curFloor.getFloorName())) {
        if (equipment.getIsDirty()) {
          dirty++;
        } else {
          clean++;
        }
      }
    }

    for (ServiceRequest request : RequestSystem.getSystem().getEveryServiceRequest()) {
      if (request.getLocation() != null) {
        if (request.getLocation().getFloor() != null) {
          if (request.getLocation().getFloor().equals(curFloor.getFloorName())) {
            srCount++;
          }
        }
      }
    }
    countsArea.setText(
        "Active Service Requests: "
            + srCount
            + "\n"
            + "Clean Equipment: "
            + clean
            + "\n"
            + "Dirty Equipment: "
            + dirty);
  }

  @FXML
  private void updateAlerts() {
    ArrayList<String> alerts = new ArrayList<>();
    ArrayList<Icon> iconList = curFloor.getIconList();
    ArrayList<EquipmentIcon> i = new ArrayList<>();
    int[] state;
    String alertText = "";
    for (Icon icon : iconList) {
      if (icon.iconType.equals(Icon.IconType.Equipment)) {
        i.add((EquipmentIcon) icon);
      }
    }
    for (EquipmentIcon e : i) {
      state = e.pumpAlert();
      if ((state[0] < 5) && e.hasCleanEquipment()) {
        alerts.add(
            "ALERT there are only "
                + state[0]
                + " clean pumps at location "
                + e.getLocation().getNodeID());
      }
      if (state[1] >= 10) {
        alerts.add(
            "ALERT! there are "
                + state[1]
                + " dirty pumps at location "
                + e.getLocation().getNodeID());
      }
    }

    for (String a : alerts) {
      alertText = alertText + a + "\n";
    }
    System.out.println(alertText);
    alertArea.setText(alertText);
  }

  @FXML
  private void updateAll() {
    updateEquipmentTable();
    updatePatientTable();
    updateServiceRequestTable();
    updateCounts();
    updateAlerts();
  }

  @Override
  public void init() {
    setUpButtonSubjects();
    setUpDashboardListeners();
    // updateAll();
  }
}

package edu.wpi.cs3733.d22.teamV.controllers;

import edu.wpi.cs3733.d22.teamV.main.RequestSystem;
import edu.wpi.cs3733.d22.teamV.main.Vdb;
import edu.wpi.cs3733.d22.teamV.map.EquipmentIcon;
import edu.wpi.cs3733.d22.teamV.map.Floor;
import edu.wpi.cs3733.d22.teamV.map.Icon;
import edu.wpi.cs3733.d22.teamV.map.LocationIcon;
import edu.wpi.cs3733.d22.teamV.map.MapManager;
import edu.wpi.cs3733.d22.teamV.objects.Equipment;
import edu.wpi.cs3733.d22.teamV.objects.Patient;
import edu.wpi.cs3733.d22.teamV.servicerequests.EquipmentDelivery;
import edu.wpi.cs3733.d22.teamV.servicerequests.ServiceRequest;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Objects;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.TreeItemPropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class MapDashboardController extends Controller {

  private @FXML TreeTableView equipmentTable;
  private @FXML TreeTableColumn<Equipment, Integer> equipmentIDCol;
  private @FXML TreeTableColumn<Equipment, String> isDirtyCol;
  private @FXML TreeTableView serviceRequestTable;
  private @FXML TreeTableColumn<ServiceRequest, String> typeCol;
  private @FXML TreeTableColumn<ServiceRequest, String> locationCol;
  private @FXML TreeTableView patientTable;
  private @FXML TreeTableColumn<Patient, Integer> patientIDCol;
  private @FXML TreeTableColumn<Patient, String> lastCol;
  private @FXML TreeTableColumn<Patient, String> SRCol;
  private @FXML TextArea countsArea = new TextArea();
  private @FXML TextArea alertArea;
  private @FXML TextArea alertsArea = new TextArea();
  private @FXML VBox rightVBox;
  private @FXML ImageView mapButton;
  private @FXML ImageView imageView;
  private @FXML ArrayList<String> alertTable;
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

  private @FXML BarChart bedBarChart;

  private Parent root;
  FXMLLoader loader = new FXMLLoader();

  public void goHome(javafx.scene.input.MouseEvent event) throws IOException {
    root =
        FXMLLoader.load(
            Objects.requireNonNull(getClass().getClassLoader().getResource("FXML/home.fxml")));
    PopupController.getController().closePopUp();
    Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
    Scene scene = new Scene(root);
    stage.setScene(scene);
    stage.show();
  }

  public void goExit(javafx.scene.input.MouseEvent event) {
    stop();
  }

  public void openMap(MouseEvent event) throws IOException {
    root =
        FXMLLoader.load(
            Objects.requireNonNull(getClass().getClassLoader().getResource("FXML/Map.fxml")));
    loader.setLocation(getClass().getClassLoader().getResource("FXML/Map.fxml"));
    try {
      root = loader.load();
    } catch (IOException e) {
      e.printStackTrace();
    }

    MapController controller = loader.getController();
    controller.initFloor(curFloor.getFloorName());

    PopupController.getController().closePopUp();
    Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
    Scene scene = new Scene(root);
    stage.setScene(scene);
    stage.show();
  }

  private static class SingletonHelper {
    private static final MapDashboardController controller = new MapDashboardController();
  }

  public static MapDashboardController getController() {
    return SingletonHelper.controller;
  }

  @Override
  public void start(Stage primaryStage) throws Exception {}

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
    floorLabel.setText("LL2");
    updateMap("L2");
  }

  @FXML
  public void switchToLL1() {
    curFloor = MapManager.getManager().getFloor("L1");
    updateListeners(curFloor);
    updateAll();
    floorLabel.setText("LL1");
    updateMap("L1");
  }

  @FXML
  public void switchToF1() {
    curFloor = MapManager.getManager().getFloor("1");
    updateListeners(curFloor);
    updateAll();
    floorLabel.setText("Floor 1");
    updateMap("F1");
  }

  @FXML
  public void switchToF2() {
    curFloor = MapManager.getManager().getFloor("2");
    updateListeners(curFloor);
    updateAll();
    floorLabel.setText("Floor 2");
    updateMap("F2");
  }

  @FXML
  public void switchToF3() {
    curFloor = MapManager.getManager().getFloor("3");
    updateListeners(curFloor);
    updateAll();
    floorLabel.setText("Floor 3");
    updateMap("F3");
  }

  @FXML
  public void switchToF4() {
    curFloor = MapManager.getManager().getFloor("4");
    updateListeners(curFloor);
    updateAll();
    floorLabel.setText("Floor 4");
    updateMap("F4");
  }

  @FXML
  public void switchToF5() {
    curFloor = MapManager.getManager().getFloor("5");
    updateListeners(curFloor);
    updateAll();
    floorLabel.setText("Floor 5");
    updateMap("F5");
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
    locationCol.setCellValueFactory(new TreeItemPropertyValueFactory("nodeID"));

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
            if (i == s.getServiceID()) {
              if (s.getLocation() != null) {
                if (s.getLocation().getFloor() != null) {
                  if (s.getLocation().getFloor().equals(curFloor.getFloorName())) {
                    TreeItem<Patient> item = new TreeItem(pos);
                    treeItems.add(item);
                  }
                }
              }
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

  public int checkAlertSixBeds(String m1, boolean d1, String m2, boolean d2) {
    /*if (m1.equals("bed") && d1 == true && m2.equals("Bed") && d2 == true) {
      return 1;
    } else {
      return 0;
    }*/
    return 0;
  }

  @FXML
  public void addBedAlertToArray(boolean b, ArrayList<String> dirtyBedsFloor) {
    /*
    if (b == true) {
      for (String s : dirtyBedsFloor) {
        alertTable.add(s);
      }
    }

    // adds strings from alerTable to alertsArea
    for (String s : alertTable) {
      alertsArea.setText("There are 6+ dirty beds in floor " + s);
    }*/
  }

  @FXML
  private void updateAlerts() {
    ArrayList<String> alerts = new ArrayList<>();
    ArrayList<Icon> iconList = curFloor.getIconList();
    ArrayList<EquipmentIcon> i = new ArrayList<>();
    ArrayList<LocationIcon> j = new ArrayList<>();

    int[] state;
    String alertText = "";
    for (Icon icon : iconList) {
      if (icon.iconType.equals(Icon.IconType.Equipment)) {
        i.add((EquipmentIcon) icon);
      }
    }
    for (Icon icon : iconList) {
      if (icon.iconType.equals(Icon.IconType.Location)) {
        j.add((LocationIcon) icon);
      }
    }
    int index = 0;
    for (EquipmentIcon e : i) {
      state = e.pumpAlert();
      if ((state[0] < 5) && e.hasCleanEquipment()) {
        alerts.add(
            "ALERT there are only "
                + state[0]
                + " clean pumps at location "
                + e.getXCoord()
                + ", "
                + e.getYCoord());
      }
      if (state[1] > 9) {
        alerts.add(
            "ALERT! there are "
                + state[1]
                + " dirty pumps at location "
                + e.getXCoord()
                + ", "
                + e.getYCoord());

        EquipmentDelivery equipmentDelivery =
            new EquipmentDelivery(
                11, 5, "vDEPT00301", "Infusion Pump", "none", state[1], "Not Completed");
        j.get(index).addToRequests(equipmentDelivery);
      }
      index++;
    }

    for (String a : alerts) {
      alertText = alertText + a + "\n";
    }
    // System.out.println(alertText);
    alertArea.setText(alertText);
  }

  @FXML
  private void updateAll() {
    updateEquipmentTable();
    updatePatientTable();
    updateServiceRequestTable();
    updateCounts();
    updateAlerts();
    updateBeds();
    updatePumps();
  }

  @Override
  public void init() {
    setUpButtonSubjects();
    setUpDashboardListeners();
    setUpBarChart();
    // updateAll();
  }

  @FXML
  private void updateMap(String floor) {
    Image m = null;
    URL _url;
    switch (floor) {
      case "L2":
        _url = this.getClass().getResource("/Lower Level 2.png");

        try {
          m = new Image(_url.toExternalForm());

        } catch (NullPointerException e) {
          e.printStackTrace();
        }

        mapButton.setImage(m);
        break;

      case "L1":
        _url = this.getClass().getResource("/Lower Level 1.png");

        try {
          m = new Image(_url.toExternalForm());

        } catch (NullPointerException e) {
          e.printStackTrace();
        }
        mapButton.setImage(m);
        break;

      case "F1":
        _url = this.getClass().getResource("/1st Floor.png");

        try {
          m = new Image(_url.toExternalForm());

        } catch (NullPointerException e) {
          e.printStackTrace();
        }

        mapButton.setImage(m);
        break;

      case "F2":
        _url = this.getClass().getResource("/2nd Floor.png");

        try {
          m = new Image(_url.toExternalForm());

        } catch (NullPointerException e) {
          e.printStackTrace();
        }

        mapButton.setImage(m);
        break;

      case "F3":
        _url = this.getClass().getResource("/3rd Floor.png");

        try {
          m = new Image(_url.toExternalForm());

        } catch (NullPointerException e) {
          e.printStackTrace();
        }

        mapButton.setImage(m);
        break;

      case "F4":
        _url = this.getClass().getResource("/4th Floor.png");

        try {
          m = new Image(_url.toExternalForm());

        } catch (NullPointerException e) {
          e.printStackTrace();
        }

        mapButton.setImage(m);
        break;

      case "F5":
        _url = this.getClass().getResource("/5th Floor.png");

        try {
          m = new Image(_url.toExternalForm());

        } catch (NullPointerException e) {
          e.printStackTrace();
        }

        mapButton.setImage(m);
        break;
    }
  }

  @FXML
  public void setUpBarChart() {
    // x axis
    CategoryAxis x = new CategoryAxis();
    x.setLabel("Item");
    // y axis
    NumberAxis y = new NumberAxis();
    y.setLabel("Count");
    // add values
    XYChart.Series ds = new XYChart.Series();
    ds.setName("Beds");
    ds.getData().add(new XYChart.Data("Clean", curFloor.getDirtyEquipmentCount() + 1));
    ds.getData()
        .add(
            new XYChart.Data(
                "Dirty", curFloor.getEquipmentIcons().size() - curFloor.getDirtyEquipmentCount()));
    bedBarChart.getData().add(ds);
  }

  @FXML
  public void updateBeds() {
    bedBarChart.getData().clear();
    XYChart.Series c = new XYChart.Series();
    c.setName("Beds");
    int dirt = 0;
    int clean = 0;
    for (int i = 0; i < curFloor.getEquipmentIcons().size(); i++) {
      for (int j = 0; j < curFloor.getEquipmentIcons().get(i).getEquipmentList().size(); j++) {
        if (curFloor
            .getEquipmentIcons()
            .get(i)
            .getEquipmentList()
            .get(j)
            .getName()
            .equalsIgnoreCase("bed")) {
          if (curFloor.getEquipmentIcons().get(i).getEquipmentList().get(j).getIsDirty()) {
            dirt++;
          } else {
            clean++;
          }
        }
      }
    }
    c.getData().add(new XYChart.Data("Clean", clean));
    c.getData().add(new XYChart.Data("Dirty", dirt));
    bedBarChart.getData().add(c);
  }

  @FXML
  public void updatePumps() {
    XYChart.Series c = new XYChart.Series();
    c.setName("Pumps");
    int dirt = 0;
    int clean = 0;
    for (int i = 0; i < curFloor.getEquipmentIcons().size(); i++) {
      for (int j = 0; j < curFloor.getEquipmentIcons().get(i).getEquipmentList().size(); j++) {
        if (curFloor
            .getEquipmentIcons()
            .get(i)
            .getEquipmentList()
            .get(j)
            .getName()
            .equalsIgnoreCase("infusion pump")) {
          if (curFloor.getEquipmentIcons().get(i).getEquipmentList().get(j).getIsDirty()) {
            dirt++;
          } else {
            clean++;
          }
        }
      }
    }
    c.getData().add(new XYChart.Data("Clean", clean));
    c.getData().add(new XYChart.Data("Dirty", dirt));
    bedBarChart.getData().add(c);
  }
}

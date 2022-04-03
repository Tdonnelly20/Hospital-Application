package edu.wpi.veganvampires.controllers;

import edu.wpi.veganvampires.dao.LocationDao;
import edu.wpi.veganvampires.main.Vdb;
import edu.wpi.veganvampires.objects.Location;
import java.awt.*;
import java.util.ArrayList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.TreeItemPropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class LocationController extends Controller {
  private static LocationDao locationDao = Vdb.locationDao;
  @FXML private TreeTableView<Location> table;
  @FXML private TreeTableColumn<Location, String> nodeIDCol;
  @FXML private TreeTableColumn<Location, Integer> xCol;
  @FXML private TreeTableColumn<Location, Integer> yCol;
  @FXML private TreeTableColumn<Location, String> floorCol;
  @FXML private TreeTableColumn<Location, String> buildingCol;
  @FXML private TreeTableColumn<Location, String> nodeTypeCol;
  @FXML private TreeTableColumn<Location, String> shortNameCol;
  @FXML private TreeTableColumn<Location, String> longNameCol;

  // Buttons
  @FXML private VBox vbox;
  @FXML private HBox hbox = new HBox(15);
  @FXML private Button reset = new Button("<- Go Back");
  @FXML private Button addLocation = new Button("Add Location");
  @FXML private Button removeLocation = new Button("Remove Location");
  @FXML private Button updateLocation = new Button("Update Location");
  @FXML private Button submit = new Button("Submit");
  @FXML private Button clear = new Button("Clear");

  // Fields
  @FXML private TextField nodeID = new TextField();
  @FXML private TextField x = new TextField();
  @FXML private TextField y = new TextField();
  @FXML private TextField floor = new TextField();
  @FXML private TextField building = new TextField();
  @FXML private TextField nodeType = new TextField();
  @FXML private TextField shortName = new TextField();
  @FXML private TextField longName = new TextField();

  private static class SingletonHelper {
    private static final LocationController manager = new LocationController();
  }

  public static LocationController getManager() {
    return LocationController.SingletonHelper.manager;
  }

  @FXML
  protected void setElements() {
    reset.setOnAction(
        event -> {
          resetPage();
        });
    addLocation.setOnAction(
        event -> {
          openUpdateLocation();
        });
    removeLocation.setOnAction(
        event -> {
          openRemoveLocation();
        });
    updateLocation.setOnAction(
        event -> {
          openUpdateLocation();
        });
    setTextFieldActions();
    setTextFieldPrompts();
  }

  @FXML
  private void setTextFieldPrompts() {
    nodeID.setPromptText("Node ID");
    x.setPromptText("X-Coordinate");
    y.setPromptText("Y-Coordinate");
    floor.setPromptText("Floor");
    building.setPromptText("Building");
    nodeType.setPromptText("Node Type");
    shortName.setPromptText("Short Name");
    longName.setPromptText("Long Name");
    nodeID.setText("");
    x.setText("");
    y.setText("");
    floor.setText("");
    building.setText("");
    nodeType.setText("");
    shortName.setText("");
    longName.setText("");
    submit.setDisable(true);
  }

  @FXML
  private void setTextFieldActions() {
    nodeID.setOnKeyReleased(
        event -> {
          validateButton();
        });
    x.setOnKeyReleased(
        event -> {
          validateButton();
        });
    y.setOnKeyReleased(
        event -> {
          validateButton();
        });
    floor.setOnKeyReleased(
        event -> {
          validateButton();
        });
    building.setOnKeyReleased(
        event -> {
          validateButton();
        });
    nodeType.setOnKeyReleased(
        event -> {
          validateButton();
        });
    shortName.setOnKeyReleased(
        event -> {
          validateButton();
        });
    longName.setOnKeyReleased(
        event -> {
          validateButton();
        });
  }

  @FXML
  protected void resetPage() {
    vbox.getChildren().clear();
    hbox.getChildren().clear();
    setTextFieldPrompts();
    vbox.getChildren().addAll(addLocation, removeLocation, updateLocation);
  }

  @FXML
  private void setForms() {
    vbox.getChildren().clear();
    hbox.getChildren().clear();
    submit.setDisable(true);
    setTextFieldPrompts();
    hbox.getChildren().addAll(reset, clear, submit);
  }

  @FXML
  private void openRemoveLocation() {
    setForms();
    clear.setOnAction(
        event -> {
          nodeID.setPromptText("Node ID");
        });
    submit.setOnAction(
        event -> {
          locationDao.deleteLocation(nodeID.getText());
        });
    vbox.getChildren().addAll(hbox, nodeID);
    updateTreeTable();
  }

  @FXML
  private void openUpdateLocation() {
    setForms();
    clear.setOnAction(
        event -> {
          setTextFieldPrompts();
        });
    vbox.getChildren().addAll(hbox, nodeID, x, y, floor, building, nodeType, shortName, longName);
    updateTreeTable();
  }

  @Override
  public void start(Stage primaryStage) {}

  @FXML
  public void updateTreeTable() {
    nodeIDCol.setCellValueFactory(new TreeItemPropertyValueFactory("nodeID"));
    xCol.setCellValueFactory(new TreeItemPropertyValueFactory("xCoord"));
    yCol.setCellValueFactory(new TreeItemPropertyValueFactory("yCoord"));
    floorCol.setCellValueFactory(new TreeItemPropertyValueFactory("Floor"));
    buildingCol.setCellValueFactory(new TreeItemPropertyValueFactory("Building"));
    nodeTypeCol.setCellValueFactory(new TreeItemPropertyValueFactory("nodeType"));
    shortNameCol.setCellValueFactory(new TreeItemPropertyValueFactory("shortName"));
    longNameCol.setCellValueFactory(new TreeItemPropertyValueFactory("longName"));

    ArrayList<Location> currLocations = (ArrayList<Location>) locationDao.getAllLocations();
    ArrayList<TreeItem> treeItems = new ArrayList<>();

    if (!currLocations.isEmpty()) {

      for (Location pos : currLocations) {
        TreeItem<Location> item = new TreeItem(pos);
        treeItems.add(item);
      }

      table.setShowRoot(false);
      TreeItem root = new TreeItem(locationDao.getAllLocations().get(0));
      table.setRoot(root);
      root.getChildren().addAll(treeItems);
    }
  }

  @FXML
  private void validateButton() {
    if ((!nodeID.getText().isEmpty())
        && (!x.getText().isEmpty() && isInteger(x.getText()))
        && (!y.getText().isEmpty() && isInteger(y.getText()))
        && (!floor.getText().isEmpty())
        && (!building.getText().isEmpty())
        && (!longName.getText().isEmpty())
        && (!shortName.getText().isEmpty())
        && (!nodeType.getText().isEmpty())) {
      submit.setDisable(false);
    } else {
      submit.setDisable(true);
    }
    if (isDouble(x.getText()) && isDouble(y.getText())) { // Change to work with doubles
    } else {
      if (!isInteger(x.getText()) && !(x.getText().isEmpty())) {
        x.setStyle("-fx-text-fill: red;");
      } else {
        x.setStyle("-fx-text-fill: black;");
      }
      if (!isInteger(y.getText()) && !(y.getText().isEmpty())) {
        y.setStyle("-fx-text-fill: red;");
      } else {
        y.setStyle("-fx-text-fill: black;");
      }
    }
  }
}

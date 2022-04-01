package edu.wpi.veganvampires.controllers;

import edu.wpi.veganvampires.dao.LocationDao;
import edu.wpi.veganvampires.objects.Location;
import java.util.ArrayList;
import javafx.fxml.FXML;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableView;
import javafx.scene.control.cell.TreeItemPropertyValueFactory;
import javafx.stage.Stage;

public class LocationController extends Controller {
  private static LocationDao locationDao = new LocationDao();
  @FXML private TreeTableView<Location> table;
  @FXML private TreeTableColumn<Location, String> nodeIDCol;
  @FXML private TreeTableColumn<Location, Integer> x;
  @FXML private TreeTableColumn<Location, Integer> y;
  @FXML private TreeTableColumn<Location, String> floor;
  @FXML private TreeTableColumn<Location, String> building;
  @FXML private TreeTableColumn<Location, String> nodeType;
  @FXML private TreeTableColumn<Location, String> shortName;
  @FXML private TreeTableColumn<Location, String> longName;

  @Override
  public void start(Stage primaryStage) {}

  @FXML
  public void updateTreeTable() {
    nodeIDCol.setCellValueFactory(new TreeItemPropertyValueFactory("nodeID"));
    x.setCellValueFactory(new TreeItemPropertyValueFactory("xCoord"));
    y.setCellValueFactory(new TreeItemPropertyValueFactory("yCoord"));
    floor.setCellValueFactory(new TreeItemPropertyValueFactory("Floor"));
    building.setCellValueFactory(new TreeItemPropertyValueFactory("Building"));
    nodeType.setCellValueFactory(new TreeItemPropertyValueFactory("nodeType"));
    shortName.setCellValueFactory(new TreeItemPropertyValueFactory("shortName"));
    longName.setCellValueFactory(new TreeItemPropertyValueFactory("longName"));

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
}

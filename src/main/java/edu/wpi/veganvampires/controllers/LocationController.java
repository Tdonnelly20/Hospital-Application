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
  public void refresh() {
    nodeIDCol.setCellValueFactory(new TreeItemPropertyValueFactory("nodeID"));
    x.setCellValueFactory(new TreeItemPropertyValueFactory("xCoord"));
    y.setCellValueFactory(new TreeItemPropertyValueFactory("yCoord"));
    floor.setCellValueFactory(new TreeItemPropertyValueFactory("Floor"));
    building.setCellValueFactory(new TreeItemPropertyValueFactory("Building"));
    nodeType.setCellValueFactory(new TreeItemPropertyValueFactory("nodeType"));
    shortName.setCellValueFactory(new TreeItemPropertyValueFactory("shortName"));
    longName.setCellValueFactory(new TreeItemPropertyValueFactory("longName"));
    // Get the locations the DAO
    ArrayList<Location> currLocations = (ArrayList<Location>) locationDao.getAllLocations();

    // Create a list for our tree items
    ArrayList<TreeItem> treeItems = new ArrayList<>();
    // Need to make sure the list isn't empty
    if (!currLocations.isEmpty()) {
      // for each loop cycling through each location currently entered into the system
      for (Location pos : currLocations) {
        TreeItem<Location> item = new TreeItem(pos);
        treeItems.add(item);
      }

      // VERY IMPORTANT: Because this is a Tree Table, we need to create a root, and then hide it so
      // we get the standard table functionality
      table.setShowRoot(false);
      // Root is just the first entry in our list
      TreeItem root = new TreeItem(locationDao.getAllLocations().get(0));
      // Set the root in the table
      table.setRoot(root);
      // Set the rest of the tree items to the root, including the one we set as the root
      root.getChildren().addAll(treeItems);
    }
  }
}

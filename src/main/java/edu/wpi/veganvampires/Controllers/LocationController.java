package edu.wpi.veganvampires.Controllers;

import edu.wpi.veganvampires.Dao.LocationDao;
import edu.wpi.veganvampires.Features.Location;
import java.util.ArrayList;
import javafx.fxml.FXML;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableView;
import javafx.stage.Stage;

public class LocationController extends Controller {
  private static LocationDao locationDao = new LocationDao();
  @FXML private TreeTableView<Location> table;
  @FXML private TreeTableColumn<Location, String> nodeID;
  @FXML private TreeTableColumn<Location, Integer> xCoord;
  @FXML private TreeTableColumn<Location, Integer> yCoord;
  @FXML private TreeTableColumn<Location, String> floor;
  @FXML private TreeTableColumn<Location, String> building;
  @FXML private TreeTableColumn<Location, String> nodeType;
  @FXML private TreeTableColumn<Location, String> shortName;
  @FXML private TreeTableColumn<Location, String> longName;

  @Override
  public void start(Stage primaryStage) {}

  @FXML
  public void updateTree(Location new_location) {}

  void refresh() {
    // Get the current list of medicine deliveries from the DAO
    ArrayList<Location> currLocations = (ArrayList<Location>) locationDao.getAllLocations();

    // Create a list for our tree items
    ArrayList<TreeItem> treeItems = new ArrayList<>();
    // Need to make sure the list isn't empty
    if (!currLocations.isEmpty()) {

      // for each loop cycling through each location currently entered into the system
      for (Location delivery : currLocations) {
        TreeItem<Location> item = new TreeItem(delivery);
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

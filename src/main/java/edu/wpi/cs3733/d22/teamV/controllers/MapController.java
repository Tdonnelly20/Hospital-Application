package edu.wpi.cs3733.d22.teamV.controllers;

import static edu.wpi.cs3733.d22.teamV.main.Vdb.locationDao;

import com.jfoenix.controls.JFXComboBox;
import edu.wpi.cs3733.d22.teamV.main.Vdb;
import edu.wpi.cs3733.d22.teamV.manager.MapManager;
import edu.wpi.cs3733.d22.teamV.map.Floor;
import edu.wpi.cs3733.d22.teamV.map.Icon;
import edu.wpi.cs3733.d22.teamV.map.LocationIcon;
import edu.wpi.cs3733.d22.teamV.map.MapEvent;
import edu.wpi.cs3733.d22.teamV.map.ZoomPane;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import org.controlsfx.control.CheckComboBox;

public class MapController extends Controller {
  protected Floor currFloor;
  boolean drag = false;

  @FXML
  ObservableList<String> filterItems =
      FXCollections.observableArrayList(
          "Locations",
          "Equipment",
          "Clean Equipment",
          "Service Requests",
          "Active Requests",
          "Lab Requests",
          "Equipment Delivery",
          "Meal Delivery",
          "Laundry Requests",
          "Medicine Delivery",
          "Religious Requests",
          "Sanitation Requests",
          "Internal Patient Transport Requests");

  @FXML protected VBox mapVBox = new VBox(15);
  @FXML protected CheckComboBox<String> filterCheckBox = new CheckComboBox<>();
  @FXML protected HBox mapHBox = new HBox(15);
  @FXML protected Pane mapPane = new Pane();
  protected final DoubleProperty deltaY = new SimpleDoubleProperty(0.0d);
  protected final Group group = new Group();
  @FXML protected ImageView mapImage = new ImageView(new Image("1st Floor.png"));
  @FXML protected StackPane stackPane = new StackPane();
  protected ZoomPane zoomPane = null;
  @FXML protected ScrollPane scrollPane = new ScrollPane(stackPane);
  protected final DoubleProperty zoomProperty = new SimpleDoubleProperty(200);

  @FXML
  protected JFXComboBox floorDropDown =
      new JFXComboBox<>(
          FXCollections.observableArrayList(
              "Lower Level 2",
              "Lower Level 1",
              "1st Floor",
              "2nd Floor",
              "3rd Floor",
              "4th Floor",
              "5th Floor"));


  @Override
  public void start(Stage primaryStage) throws Exception {
    init();
  }

  @FXML
  void zoom() {
    stackPane.getChildren().add(mapImage);
    stackPane.getChildren().add(mapPane);
    mapImage.setPreserveRatio(true);
    scrollPane.setPannable(true);
    scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
    scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
    group.getChildren().add(mapImage);
    group.getChildren().add(stackPane);

    zoomPane = new ZoomPane();
    zoomProperty.bind(zoomPane.scale);
    deltaY.bind(zoomPane.deltaY);
    zoomPane.getChildren().add(group);

    MapEvent mapEvent = new MapEvent(zoomPane);

    scrollPane.setContent(zoomPane);
    zoomPane.toBack();
    mapPane.setOnMouseClicked(
        event -> {
          openIconFormWindow(event);
        });
    scrollPane.addEventFilter(ScrollEvent.ANY, mapEvent.getOnZoomEventHandler());
  }

  void setUpControls() {
    floorDropDown.setValue("1st Floor");
    floorDropDown.setOnAction(
        event -> {
          checkDropDown();
        });
    filterCheckBox = new CheckComboBox<>();
    filterCheckBox.setTitle("Filter Items");
    filterCheckBox.getItems().addAll(filterItems);
    filterCheckBox
        .focusedProperty()
        .addListener(
            (o, ov, nv) -> {
              if (nv) filterCheckBox.show();
              else filterCheckBox.hide();
            });
    filterCheckBox
        .getCheckModel()
        .getCheckedItems()
        .addListener(
            (ListChangeListener<String>)
                change -> {
                  checkDropDown();
                });
  }

  @Override
  public void init() {
    setUpControls();
    zoom();
    currFloor = MapManager.getManager().getFloor("1");
    mapVBox.setFillWidth(true);

    scrollPane.setMaxSize(470, 470);
    mapHBox.getChildren().addAll(floorDropDown, filterCheckBox);
    mapVBox.getChildren().addAll(scrollPane, mapHBox);
    mapVBox.setAlignment(Pos.TOP_CENTER);
    mapVBox.setSpacing(15);
    mapHBox.setAlignment(Pos.CENTER);
    checkDropDown();
  }

  /** Checks the value of the floor drop down and matches it with the corresponding map png */
  @FXML
  void checkDropDown() {
    MapManager.getManager().closePopUp();
    String url = floorDropDown.getValue().toString() + ".png";
    System.out.println(floorDropDown.getValue().toString() + ".png");
    mapImage.setImage(new Image(url));
    mapImage.setFitWidth(600);
    mapImage.setFitHeight(600);
    getFloor();
  }

  @FXML
  private void dragDetected() {
    drag = true;
  }

  @FXML
  private void dragOver() {
    System.out.println("Drag over");
    drag = false;
  }

  // Sets the mapImage to the corresponding floor dropdown and returns the floor string
  private String getFloor() {
    String result = "";
    switch (floorDropDown.getValue().toString()) {
      case "Lower Level 1":
        currFloor = Vdb.mapManager.getFloor("L1");
        result = "L1";
        break;
      case "Lower Level 2":
        currFloor = Vdb.mapManager.getFloor("L2");
        result = "L2";
        break;
      case "1st Floor":
        currFloor = Vdb.mapManager.getFloor("1");
        result = "1";
        break;
      case "2nd Floor":
        currFloor = Vdb.mapManager.getFloor("2");
        result = "2";
        break;
      case "3rd Floor":
        currFloor = Vdb.mapManager.getFloor("3");
        result = "3";
        System.out.println("3");
        break;
      case "4th Floor":
        currFloor = Vdb.mapManager.getFloor("4");
        result = "2";
        break;
      case "5th Floor":
        currFloor = Vdb.mapManager.getFloor("5");
        result = "3";
        break;
    }

    populateFloorIconArr();
    return result;
  }

  // Loads the floor's icons in accordance with filter
  @FXML
  public void populateFloorIconArr() {
    mapPane.getChildren().clear();
    ObservableList<String> filter = filterCheckBox.getCheckModel().getCheckedItems();
    for (Icon icon : currFloor.getIconList()) {
      if (filter.size() > 0) {
        System.out.println(icon.iconType);
        mapPane.getChildren().add(icon.getImage());
        if (filter.contains("Request") && icon.iconType.equals("Request")) {
          System.out.println(icon.getImage());
          mapPane.getChildren().add(icon.getImage());
        }
        if (filter.contains("Equipment") && icon.iconType.equals("Equipment")) {
          mapPane.getChildren().add(icon.getImage());
        }
        if (filter.contains("Location") && icon.iconType.equals("Location")) {
          mapPane.getChildren().add(icon.getImage());
        }
      } else {
        mapPane.getChildren().add(icon.getImage());
      }
    }
  }

  // Opens and manages the location adding form
  @FXML
  public void openIconFormWindow(MouseEvent event) {
    if (!event.getTarget().getClass().getTypeName().equals("javafx.scene.image.ImageView")
        && !drag) {
      MapManager mapManager = MapManager.getManager();
      // X and Y coordinates
      double xPos = event.getX() - 15;
      double yPos = event.getY() - 25;

      mapManager.locationForm(event, false);
      mapManager
          .getSubmitIcon()
          .setOnAction(
              event1 -> {
                if (mapManager.checkFields()) {
                  addIcon(
                      new LocationIcon(mapManager.getLocation(xPos + 25, yPos + 15, getFloor())));
                  System.out.println("Real X: " + event.getX() + " Y: " + event.getY());
                  System.out.println("X: " + xPos + " Y: " + yPos);
                  populateFloorIconArr();
                } else {
                  Text missingFields = new Text("Please fill all fields");
                  missingFields.setFill(Color.RED);
                  missingFields.setTextAlignment(TextAlignment.CENTER);
                  MapManager.getManager().getContent().getChildren().add(missingFields);
                  System.out.println("MISSING FIELD");
                }
              });
      // Place Icon
      MapManager.getManager().getTempIcon().setX(xPos);
      MapManager.getManager().getTempIcon().setY(yPos);
      if (!mapPane.getChildren().contains(MapManager.getManager().getTempIcon())) {
        System.out.println("X:" + xPos + " Y:" + yPos);
        MapManager.getManager().getTempIcon().setFitWidth(30);
        MapManager.getManager().getTempIcon().setFitHeight(30);
        mapPane.getChildren().add(MapManager.getManager().getTempIcon());
      }
    }
  }

  // Adds icon to map
  private void addIcon(Icon icon) {
    switch (icon.iconType) {
      case "Location":
        locationDao.addLocation(icon.getLocation());
    }
    MapManager.getManager().closePopUp();
    mapPane.getChildren().remove(MapManager.getManager().getTempIcon());
    MapManager.getManager().getFloor(getFloor()).addIcon(icon);
    MapManager.getManager().getTempIcon().setVisible(false);
    checkDropDown();
  }
}

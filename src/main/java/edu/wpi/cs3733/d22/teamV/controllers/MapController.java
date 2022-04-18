package edu.wpi.cs3733.d22.teamV.controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import edu.wpi.cs3733.d22.teamV.main.RequestSystem;
import edu.wpi.cs3733.d22.teamV.manager.MapManager;
import edu.wpi.cs3733.d22.teamV.map.*;
import edu.wpi.cs3733.d22.teamV.objects.Location;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import lombok.Getter;
import lombok.Setter;
import org.controlsfx.control.CheckComboBox;

@Setter
@Getter
public class MapController extends Controller {
  protected Floor currFloor = MapManager.getManager().getFloor("1st Floor");
  @FXML protected VBox mapVBox = new VBox(15);
  @FXML protected Button refreshButton = new Button("Refresh");
  @FXML protected CheckComboBox<String> filterCheckBox = new CheckComboBox<>();
  @FXML protected VBox controlsVBox = new VBox(15);
  @FXML protected Pane mapPane = new Pane();
  protected final DoubleProperty deltaY = new SimpleDoubleProperty(0.0d);
  protected final Group group = new Group();
  @FXML protected ImageView mapImage = new ImageView(new Image("1st Floor.png"));
  @FXML protected StackPane stackPane = new StackPane();
  protected ZoomPane zoomPane = null;
  @FXML protected ScrollPane scrollPane = new ScrollPane(stackPane);
  protected final DoubleProperty zoomProperty = new SimpleDoubleProperty(200);
  @FXML JFXButton LL2 = new JFXButton("LL2");
  @FXML JFXButton LL1 = new JFXButton("LL1");
  @FXML JFXButton floor1 = new JFXButton("Floor 1");
  @FXML JFXButton floor2 = new JFXButton("Floor 2");
  @FXML JFXButton floor3 = new JFXButton("Floor 3");
  @FXML JFXButton floor4 = new JFXButton("Floor 4");
  @FXML JFXButton floor5 = new JFXButton("Floor 5");
  @FXML TextField[] fields = new TextField[10];
  @FXML JFXComboBox[] comboBoxes = new JFXComboBox[5];
  @FXML Button submitButton = new Button("Submit");
  private String floorName = "";

  @FXML
  ObservableList<String> filterItems =
      FXCollections.observableArrayList(
          "Locations",
          "Department",
          "Hallway",
          "Service",
          "Elevator",
          "Stairway",
          "Bathroom",
          "Labs",
          "Equipment",
          "Clean Equipment",
          "Service Requests",
          "Active Requests",
          "Lab Requests",
          "Equipment Delivery Requests",
          "Meal Delivery Requests",
          "Laundry Requests",
          "Medicine Delivery Requests",
          "Religious Requests",
          "Sanitation Requests",
          "Internal Patient Transport Requests");

  private static class SingletonHelper {
    private static final MapController controller = new MapController();
  }

  public static MapController getController() {
    return SingletonHelper.controller;
  }

  @Override
  public void start(Stage primaryStage) throws Exception {
    init();
  }

  @Override
  public void init() {
    setFloor("1");
    mapSetUp();
  }

  /** Allows users to zoom in and out of the map without */
  protected void mapSetUp() {
    setUpControls();
    zoom();
    filterCheckBox.setMaxWidth(controlsVBox.getWidth() / 3);
    scrollPane.setPrefSize(550, 550);
    controlsVBox.getChildren().addAll(filterCheckBox, refreshButton);
    mapVBox.getChildren().addAll(scrollPane);
    mapVBox.setAlignment(Pos.CENTER);
    mapVBox.setSpacing(15);
    controlsVBox.setAlignment(Pos.CENTER);
    checkFilter();
  }

  /** Allows users to zoom in and out of the map without */
  @FXML
  void zoom() {
    zoomPane = new ZoomPane();
    zoomPane.getChildren().clear();
    group.getChildren().clear();
    stackPane.getChildren().clear();
    stackPane.getChildren().add(mapImage);
    stackPane.getChildren().add(mapPane);

    scrollPane.setPrefSize(550, 550);
    mapImage.setFitHeight(scrollPane.getPrefHeight() + 50);
    mapImage.setFitWidth(scrollPane.getPrefHeight() + 50);
    mapPane.setPrefSize(mapImage.getFitWidth(), mapImage.getFitHeight());
    // mapPane.autosize();
    mapImage.setPreserveRatio(true);
    scrollPane.setPannable(true);
    scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
    scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
    group.getChildren().add(mapImage);
    group.getChildren().add(mapPane);

    group.getChildren().add(stackPane);
    zoomProperty.bind(zoomPane.scale);
    deltaY.bind(zoomPane.deltaY);
    zoomPane.getChildren().add(group);

    MapEvent mapEvent = new MapEvent(zoomPane);

    scrollPane.setContent(zoomPane);
    zoomPane.toBack();
    scrollPane.addEventFilter(ScrollEvent.ANY, mapEvent.getOnZoomEventHandler());
  }

  void setUpControls() {
    System.out.println("setting up controls");
    LL2.setOnAction(
        event -> {
          setFloor("L2");
        });
    LL1.setOnAction(
        event -> {
          setFloor("L1");
        });
    floor1.setOnAction(
        event -> {
          setFloor("1");
        });
    floor2.setOnAction(
        event -> {
          setFloor("2");
        });
    floor3.setOnAction(
        event -> {
          setFloor("3");
        });
    floor4.setOnAction(
        event -> {
          setFloor("4");
        });
    floor5.setOnAction(
        event -> {
          setFloor("5");
        });
    refreshButton.setOnAction(
        event -> {
          System.out.println("Refresh");
          setFloor(floorName);
        });
    for (int i = 0; i < 10; i++) {
      fields[i] = new TextField();
    }
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
                  checkFilter();
                });

    mapPane.setOnMouseClicked(
        event -> {
          if (event.getClickCount() == 2) {
            openIconFormWindow(event);
          }
        });
  }

  /** Filters out icons based on filterCheckBox */
  @FXML
  public void checkFilter() {
    if (filterCheckBox.getCheckModel().getCheckedItems().contains("Active Requests")) {
      if (!filterCheckBox.getCheckModel().isChecked("Service Requests")) {
        filterCheckBox.getCheckModel().check("Service Requests");
      }
    }
    if (filterCheckBox.getCheckModel().getCheckedItems().contains("Clean Equipment")) {
      if (!filterCheckBox.getCheckModel().isChecked("Equipment")) {
        filterCheckBox.getCheckModel().check("Equipment");
      }
    }
    PopupController.getController().closePopUp();
    populateFloorIconArr();
  }

  // Sets the mapImage to the corresponding floor dropdown and returns the floor string
  public void setFloor(String floor) {
    floorName = floor;
    currFloor = MapManager.getManager().getFloor(floorName);
    mapImage.setImage(currFloor.getMap());
    populateFloorIconArr();
  }

  /** Loads the floor's icons in accordance with filter */
  @FXML
  public void populateFloorIconArr() {
    mapPane.getChildren().clear();
    ObservableList<String> filter = filterCheckBox.getCheckModel().getCheckedItems();
    if (filter.size() > 0) {
      filterRequestsAndLocations();
      filterEquipment();
    } else {
      for (Icon icon : currFloor.getIconList()) {
        if (!mapPane.getChildren().contains(icon.getImage())) {
          mapPane.getChildren().add(icon.getImage());
        }
      }
    }
  }
  /** Filter by Equipment */
  private void filterEquipment() {
    ObservableList<String> filter = filterCheckBox.getCheckModel().getCheckedItems();
    if (filter.contains("Equipment")) { // Filter Equipment
      for (EquipmentIcon icon : currFloor.getEquipmentIcons()) {
        if (filter.contains("Clean Equipment")) {
          if (icon.hasCleanEquipment()) {
            mapPane.getChildren().add(icon.getImage());
          }
        } else {
          mapPane.getChildren().add(icon.getImage());
        }
      }
    }
  }

  /** Filters by service requests and locations */
  private void filterRequestsAndLocations() {
    ObservableList<String> filter = filterCheckBox.getCheckModel().getCheckedItems();
    if (filter.contains("Service Requests") || filter.contains("Locations")) {
      for (LocationIcon icon : currFloor.getLocationIcons()) {
        if (filter.contains("Service Requests")
            && icon.getRequestsArr().size() > 0) { // Filter Service Request
          if (filter.contains("Active Requests") && icon.hasActiveRequests()) {
            filterByActiveRequestType(icon);
          } else {
            filterByRequestType(icon);
          }
        }
        if (filter.contains("Locations")) { // Filter Location
          filterByLocation(icon);
        }
      }
    }
  }

  public boolean filterContainsRequests() {
    ObservableList<String> filter = filterCheckBox.getCheckModel().getCheckedItems();
    return (!filter.contains("Lab Requests")
        && !filter.contains("Equipment Delivery Requests")
        && !filter.contains("Meal Delivery Requests")
        && !filter.contains("Laundry Requests")
        && !filter.contains("Medicine Delivery Requests")
        && !filter.contains("Religious Requests")
        && !filter.contains("Sanitation Requests")
        && !filter.contains("Internal Patient Transport Requests"));
  }
  /** Filters by active request and by request type */
  public void filterByActiveRequestType(LocationIcon icon) {
    ObservableList<String> filter = filterCheckBox.getCheckModel().getCheckedItems();
    if (filterContainsRequests() && icon.hasActiveRequests()) {
      if (!mapPane.getChildren().contains(icon.getImage())) {
        mapPane.getChildren().add(icon.getImage());
      }
    } else if ((filter.contains("Lab Requests") && icon.hasActiveRequestType("Lab Request"))
        || (filter.contains("Equipment Delivery Requests")
            && icon.hasActiveRequestType("Equipment Delivery Request"))
        || (filter.contains("Meal Delivery Requests")
            && icon.hasActiveRequestType("Meal Delivery Request"))
        || (filter.contains("Laundry Requests") && icon.hasActiveRequestType("Laundry Request"))
        || (filter.contains("Medicine Delivery Requests")
            && icon.hasActiveRequestType("Medicine Delivery Request"))
        || (filter.contains("Religious Requests") && icon.hasActiveRequestType("Religious Request"))
        || (filter.contains("Sanitation Requests")
            && icon.hasActiveRequestType("Sanitation Request"))
        || (filter.contains("Internal Patient Transport Requests")
            && icon.hasActiveRequestType("Internal Patient Transport Request"))) {
      mapPane.getChildren().add(icon.getImage());
    }
  }

  /** Filter by request type */
  public void filterByRequestType(LocationIcon icon) {
    ObservableList<String> filter = filterCheckBox.getCheckModel().getCheckedItems();
    if (filterContainsRequests()) {
      if (!mapPane.getChildren().contains(icon.getImage())) {
        mapPane.getChildren().add(icon.getImage());
      }
    } else if ((filter.contains("Lab Requests") && icon.hasRequestType("Lab Request"))
        || (filter.contains("Equipment Delivery Requests")
            && icon.hasRequestType("Equipment Delivery Request"))
        || (filter.contains("Meal Delivery Requests")
            && icon.hasRequestType("Meal Delivery Request"))
        || (filter.contains("Laundry Requests") && icon.hasRequestType("Laundry Request"))
        || (filter.contains("Medicine Delivery Requests")
            && icon.hasRequestType("Medicine Delivery Request"))
        || (filter.contains("Religious Requests") && icon.hasRequestType("Religious Request"))
        || (filter.contains("Sanitation Requests") && icon.hasRequestType("Sanitation Request"))
        || (filter.contains("Internal Patient Transport Requests")
            && icon.hasRequestType("Internal Patient Transport Request"))) {
      if (!mapPane.getChildren().contains(icon.getImage())) {
        mapPane.getChildren().add(icon.getImage());
      }
    }
  }

  /** Filter by location type */
  public void filterByLocation(LocationIcon icon) {
    ObservableList<String> filter = filterCheckBox.getCheckModel().getCheckedItems();
    if (!filter.contains("Department")
        && !filter.contains("Hallway")
        && !filter.contains("Service")
        && !filter.contains("Elevator")
        && !filter.contains("Stairway")
        && !filter.contains("Bathroom")
        && !filter.contains("Labs")) {
      if (!mapPane.getChildren().contains(icon.getImage())) {
        mapPane.getChildren().add(icon.getImage());
      }
    } else if ((filter.contains("Department") && icon.getLocation().getNodeType().equals("DEPT"))
        || (filter.contains("Hallway") && icon.getLocation().getNodeType().equals("HALL"))
        || (filter.contains("Service") && icon.getLocation().getNodeType().equals("SERV"))
        || (filter.contains("Elevator") && icon.getLocation().getNodeType().equals("ELEV"))
        || (filter.contains("Stairway") && icon.getLocation().getNodeType().equals("STAI"))
        || (filter.contains("Bathroom")
            && (icon.getLocation().getNodeType().equals("BATH")
                || icon.getLocation().getNodeType().equals("REST")))
        || (filter.contains("Labs") && icon.getLocation().getNodeType().equals("LABS"))) {
      if (!mapPane.getChildren().contains(icon.getImage())) {
        mapPane.getChildren().add(icon.getImage());
      }
    }
  }

  /** Filter by Equipment */
  /*private void filterEquipment() {
    ObservableList<String> filter = filterCheckBox.getCheckModel().getCheckedItems();
    if (filter.contains("Equipment")) { // Filter Equipment
      for (EquipmentIcon icon : currFloor.getEquipmentIcons()) {
        if (filter.contains("Clean Equipment")) {
          if (icon.hasCleanEquipment()) {
            mapPane.getChildren().add(icon.getImage());
          }
        } else {
          mapPane.getChildren().add(icon.getImage());
        }
      }
    }
  }*/

  /** Filters by service requests and locations */
  /*private void filterRequestsAndLocations() {
    ObservableList<String> filter = filterCheckBox.getCheckModel().getCheckedItems();
    if (filter.contains("Service Requests") || filter.contains("Locations")) {
      for (LocationIcon icon : currFloor.getLocationIcons()) {
        if (filter.contains("Service Requests")) { // Filter Service Request
          if (filter.contains("Active Requests") && (icon).hasActiveRequests()) {
            filterByActiveRequestType(icon);
          } else {
            filterByRequestType(icon);
          }
        } else { // Filter Location
          filterByLocation(icon);
        }
      }
    }
  }*/

  /** Filters by active request and by request type */
  /*public void filterByActiveRequestType(LocationIcon icon) {
    ObservableList<String> filter = filterCheckBox.getCheckModel().getCheckedItems();
    if (!filter.contains("Lab Requests")
        && !filter.contains("Equipment Delivery Requests")
        && !filter.contains("Meal Delivery Requests")
        && !filter.contains("Laundry Requests")
        && !filter.contains("Medicine Delivery Requests")
        && !filter.contains("Religious Requests")
        && !filter.contains("Sanitation Requests")
        && !filter.contains("Internal Patient Transport Requests")) {
      mapPane.getChildren().add(icon.getImage());
    } else if ((filter.contains("Lab Requests") && icon.hasActiveRequestType("Lab Request"))
        || (filter.contains("Equipment Delivery Requests")
            && icon.hasActiveRequestType("Equipment Delivery Request"))
        || (filter.contains("Meal Delivery Requests")
            && icon.hasActiveRequestType("Meal Delivery Request"))
        || (filter.contains("Laundry Requests") && icon.hasActiveRequestType("Laundry Request"))
        || (filter.contains("Medicine Delivery Requests")
            && icon.hasActiveRequestType("Medicine Delivery Request"))
        || (filter.contains("Religious Requests") && icon.hasActiveRequestType("Religious Request"))
        || (filter.contains("Sanitation Requests")
            && icon.hasActiveRequestType("Sanitation Request"))
        || (filter.contains("Internal Patient Transport Requests")
            && icon.hasActiveRequestType("Internal Patient Transport Request"))) {
      mapPane.getChildren().add(icon.getImage());
    }
  }*/

  /** Filter by request type */
  /*public void filterByRequestType(LocationIcon icon) {
    ObservableList<String> filter = filterCheckBox.getCheckModel().getCheckedItems();
    if (!filter.contains("Lab Requests")
        && !filter.contains("Equipment Delivery Requests")
        && !filter.contains("Meal Delivery Requests")
        && !filter.contains("Laundry Requests")
        && !filter.contains("Medicine Delivery Requests")
        && !filter.contains("Religious Requests")
        && !filter.contains("Sanitation Requests")
        && !filter.contains("Internal Patient Transport Requests")) {
      if (!mapPane.getChildren().contains(icon.getImage())) {
        mapPane.getChildren().add(icon.getImage());
      }
    } else if ((filter.contains("Lab Requests") && icon.hasRequestType("Lab Request"))
        || (filter.contains("Equipment Delivery Requests")
            && icon.hasRequestType("Equipment Delivery Request"))
        || (filter.contains("Meal Delivery Requests")
            && icon.hasRequestType("Meal Delivery Request"))
        || (filter.contains("Laundry Requests") && icon.hasRequestType("Laundry Request"))
        || (filter.contains("Medicine Delivery Requests")
            && icon.hasRequestType("Medicine Delivery Request"))
        || (filter.contains("Religious Requests") && icon.hasRequestType("Religious Request"))
        || (filter.contains("Sanitation Requests") && icon.hasRequestType("Sanitation Request"))
        || (filter.contains("Internal Patient Transport Requests")
            && icon.hasRequestType("Internal Patient Transport Request"))) {
      mapPane.getChildren().add(icon.getImage());
    }
  }

  private boolean filterByType(String type, LocationIcon icon) {
    Boolean hasActive = true;
    if (filterCheckBox.getCheckModel().getCheckedItems().contains("Active Requests")) {
      hasActive = icon.hasActiveRequestType(type);
    }
    return filterCheckBox.getCheckModel().getCheckedItems().contains(type + "s") && hasActive;
  }*/

  /** Filter by location type */
  /*public void filterByLocation(LocationIcon icon) {
    ObservableList<String> filter = filterCheckBox.getCheckModel().getCheckedItems();
    if (!filter.contains("Department")
        && !filter.contains("Hallway")
        && !filter.contains("Service")
        && !filter.contains("Elevator")
        && !filter.contains("Stairway")
        && !filter.contains("Bathroom")
        && !filter.contains("Labs")) {
      if (!mapPane.getChildren().contains(icon.getImage())) {
        mapPane.getChildren().add(icon.getImage());
      }
    } else if ((filter.contains("Department") && icon.getLocation().getNodeType().equals("DEPT"))
        || (filter.contains("Hallway") && icon.getLocation().getNodeType().equals("HALL"))
        || (filter.contains("Service") && icon.getLocation().getNodeType().equals("SERV"))
        || (filter.contains("Elevator") && icon.getLocation().getNodeType().equals("ELEV"))
        || (filter.contains("Stairway") && icon.getLocation().getNodeType().equals("STAI"))
        || (filter.contains("Bathroom")
            && (icon.getLocation().getNodeType().equals("BATH")
                || icon.getLocation().getNodeType().equals("REST")))
        || (filter.contains("Labs") && icon.getLocation().getNodeType().equals("LABS"))) {
      if (!mapPane.getChildren().contains(icon.getImage())) {
        mapPane.getChildren().add(icon.getImage());
      }
    }
  }*/

  @FXML
  private void addToMapPane(Icon icon) {
    if (!mapPane.getChildren().contains(icon.getImage())) {
      mapPane.getChildren().add(icon.getImage());
    } else {
      System.out.println("Icon already on map");
    }

    if (mapPane.getChildren().contains(icon.getImage())) {
      System.out.println("In");
    }
  }

  @FXML
  private void removeFromMapPane(Icon icon) {
    if (mapPane.getChildren().contains(icon.getImage())) {
      mapPane.getChildren().remove(icon.getImage());
    } else {
      System.out.println("Icon not on map");
    }
    // setFloor(floorName);
  }

  /** Adds the given icon to the map */
  @FXML
  public void addIcon(Icon icon) {
    MapManager.getManager().getFloor(floorName).addIcon(icon);
    if (icon.iconType.equals(Icon.IconType.Location)) {
      RequestSystem.getSystem().getLocationDao().addLocation(icon.getLocation());
    } else {
      RequestSystem.getSystem().addEquipment(((EquipmentIcon) icon).getEquipmentList());
      MapDashboardController.getController().updateCounts();
    }
    PopupController.getController().closePopUp();
    addToMapPane(icon);
    setFloor(floorName);
    // populateFloorIconArr();
  }

  /** Removes Icon from map */
  @FXML
  public void deleteIcon(Icon icon) {
    mapPane.getChildren().remove(icon.getImage());
    MapManager.getManager().getFloor(floorName).removeIcon(icon);
    if (PopupController.getController().stage.isShowing()) {
      PopupController.getController().closePopUp();
    }
  }

  public void setSubmitLocation(double xPos, double yPos) {
    PopupController.getController()
        .submitIcon
        .setOnAction(
            event1 -> {
              if (PopupController.getController().checkLocationFields()) {
                addIcon(
                    new LocationIcon(
                        PopupController.getController()
                            .getLocation(xPos + 25, yPos + 15, floorName)));
              } else {
                Text missingFields = new Text("Please fill all fields");
                missingFields.setFill(Color.RED);
                missingFields.setTextAlignment(TextAlignment.CENTER);
                PopupController.getController().sceneVbox.getChildren().add(missingFields);
              }
            });
  }

  // Opens and manages the location adding form
  @FXML
  public void openIconFormWindow(MouseEvent event) {
    if (!event.getTarget().getClass().getTypeName().equals("javafx.scene.image.ImageView")) {
      double xPos = event.getX() - 15;
      double yPos = event.getY() - 25;
      PopupController.getController().iconWindow(event);
      // setSubmitLocation(xPos, yPos);
      addLocationForm(event);
    }
  }

  @FXML
  public void clearForm() {
    for (TextField field : fields) {
      field = new TextField("");
    }
    for (JFXComboBox cb : comboBoxes) {
      cb = new JFXComboBox<>();
      cb.setPromptText("Status");
      cb.setValue(null);
    }
    controlsVBox.getChildren().retainAll(filterCheckBox, refreshButton);
  }

  @FXML
  private void addAllFields() {
    controlsVBox.getChildren().add(submitButton);
    for (TextField field : fields) {
      if (!field.getPromptText().equals("")) {
        Label label = new Label(field.getPromptText() + ": ");
        label.setMinWidth(75);
        HBox hBox = new HBox(label, field);
        hBox.setAlignment(Pos.CENTER);
        controlsVBox.getChildren().add(hBox);
      }
    }
  }

  @FXML
  public void IconForm(MouseEvent event) {
    clearForm();
    Button locationButton = new Button("Location");
    Button equipmentButton = new Button("Equipment");
    Button closeButton = new Button("close");
    locationButton.setOnAction(
        event2 -> {
          addLocationForm(event);
        });
    equipmentButton.setOnAction(
        event2 -> {
          addEquipmentForm(event);
        });
    closeButton.setOnAction(
        event2 -> {
          clearForm();
        });
    controlsVBox.getChildren().addAll(locationButton, equipmentButton, closeButton);
  }

  @FXML
  public void locationForm(MouseEvent event, LocationIcon icon) {
    clearForm();
    Button add;
    Button modify = new Button("Modify Location");
    Button delete = new Button("Delete Location");
    if (icon == null) {
      add = new Button("Add Location");
      add.setOnAction(
          event1 -> {
            addLocationForm(event);
          });
    } else {
      add = new Button("Add Service Request");
      add.setOnAction(
          event1 -> {
            addRequestForm(event);
          });
    }
    modify.setOnAction(event1 -> {});
    delete.setOnAction(event1 -> {});
  }

  private void addRequestForm(MouseEvent event) {}

  @FXML
  public void addLocationForm(MouseEvent event) {
    clearForm();
    double xPos = event.getX();
    double yPos = event.getY();
    fields[0].setPromptText("Location ID");
    fields[1].setPromptText("Type");
    fields[2].setPromptText("Long Name");
    fields[3].setPromptText("Short Name");
    submitButton.setOnAction(
        event1 -> {
          Location newLocation =
              new Location(
                  fields[0].getText(),
                  xPos + 10,
                  yPos - 10,
                  floorName,
                  "Tower",
                  fields[1].getText(),
                  fields[2].getText(),
                  fields[3].getText());
          LocationIcon newIcon = new LocationIcon(newLocation);
          newLocation.setIcon(newIcon);
          addIcon(newIcon);
          clearForm();
        });
    addAllFields();
  }

  @FXML
  public void removeIconForm(Icon icon) {
    clearForm();
    System.out.println("Deleting Icon");
    mapPane.getChildren().remove(icon.getImage());
    MapManager.getManager().getFloor(floorName).removeIcon(icon);
    clearForm();
  }

  @FXML
  public void addEquipmentForm(MouseEvent event) {
    clearForm();
    double xPos = event.getX() - 15;
    double yPos = event.getY() - 25;
    fields[0].setPromptText("Node ID");
    fields[1].setPromptText("Long Name");
    fields[2].setPromptText("Short Name");
    submitButton.setOnAction(
        event1 -> {
          addIcon(
              new LocationIcon(
                  PopupController.getController().getLocation(xPos + 25, yPos + 15, floorName)));
          clearForm();
        });
    addAllFields();
  }
}

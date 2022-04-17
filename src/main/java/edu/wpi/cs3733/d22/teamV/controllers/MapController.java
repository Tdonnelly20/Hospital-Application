package edu.wpi.cs3733.d22.teamV.controllers;

import com.jfoenix.controls.JFXButton;
import edu.wpi.cs3733.d22.teamV.main.RequestSystem;
import edu.wpi.cs3733.d22.teamV.manager.MapManager;
import edu.wpi.cs3733.d22.teamV.map.*;
import edu.wpi.cs3733.d22.teamV.objects.Equipment;
import java.awt.*;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.control.Button;
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

    scrollPane.setPrefSize(550, 550);
    controlsVBox.getChildren().addAll(filterCheckBox, refreshButton);
    mapVBox.getChildren().addAll(scrollPane);
    mapVBox.setAlignment(Pos.CENTER);
    mapVBox.setSpacing(15);
    controlsVBox.setAlignment(Pos.CENTER);
    checkDropDown();
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

    System.out.println(
        "Image width: " + mapImage.getFitWidth() + " height: " + mapImage.getFitHeight());
    System.out.println(
        "Pane width: " + mapPane.getMinWidth() + " height: " + mapPane.getMinHeight());

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

    mapPane.setOnMouseClicked(
        event -> {
          if (event.getClickCount() == 2) {
            System.out.println("CLICK RECEIVED");
            openIconFormWindow(event);
          }
        });
  }

  /** Checks the value of the floor drop down and matches it with the corresponding map png */
  @FXML
  public void checkDropDown() {
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
  }

  // Sets the mapImage to the corresponding floor dropdown and returns the floor string
  public void setFloor(String floor) {
    floorName = floor;
    currFloor = MapManager.getManager().getFloor(floorName);
    mapImage.setImage(currFloor.getMap());
    populateFloorIconArr();
  }

  // Loads the floor's icons in accordance with filter
  @FXML
  public void populateFloorIconArr() {
    mapPane.getChildren().clear();
    ObservableList<String> filter = filterCheckBox.getCheckModel().getCheckedItems();
    currFloor = MapManager.getManager().getFloor(currFloor.getFloorName());
    for (Icon icon : currFloor.getIconList()) {
      if (filter.size() > 0 && !currFloor.getFloorName().equals("SV")) {
        // System.out.println(icon.iconType);
        if (filter.contains("Service Requests") && icon.iconType.equals("Location")) {
          assert icon instanceof LocationIcon;
          if (((LocationIcon) icon).getRequestsArr().size() > 0) {
            if (filter.contains("Active Requests") && ((LocationIcon) icon).hasActiveRequests()) {
              filterByActiveRequestType((LocationIcon) icon);
            } else {
              filterByRequestType((LocationIcon) icon);
            }
          }
        }
        if (filter.contains("Equipment") && icon.iconType.equals("Equipment")) {
          assert icon instanceof EquipmentIcon;
          EquipmentIcon equipmentIcon = (EquipmentIcon) icon;
          if (filter.contains("Clean Equipment")) {
            if (equipmentIcon.hasCleanEquipment()) {
              mapPane.getChildren().add(icon.getImage());
            }
          } else {
            mapPane.getChildren().add(icon.getImage());
          }
        }
        if (filter.contains("Locations") && icon.iconType.equals("Location")) {
          filterByLocation((LocationIcon) icon);
        }
      } else {
        if (!mapPane.getChildren().contains(icon.getImage())) {
          mapPane.getChildren().add(icon.getImage());
        }
      }
    }
  }

  public void filterByActiveRequestType(LocationIcon icon) {
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
  }

  public void filterByRequestType(LocationIcon icon) {
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
      if (!mapPane.getChildren().contains(icon.getImage())) {
        mapPane.getChildren().add(icon.getImage());
      }
    }
  }

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
        && (filter.contains("Hallway") && icon.getLocation().getNodeType().equals("HALL"))
        && (filter.contains("Service") && icon.getLocation().getNodeType().equals("SERV"))
        && (filter.contains("Elevator") && icon.getLocation().getNodeType().equals("ELEV"))
        && (filter.contains("Stairway") && icon.getLocation().getNodeType().equals("STAI"))
        && (filter.contains("Bathroom")
            && (icon.getLocation().getNodeType().equals("BATH")
                || icon.getLocation().getNodeType().equals("REST")))
        && (filter.contains("Labs") && icon.getLocation().getNodeType().equals("LABS"))) {
      if (!mapPane.getChildren().contains(icon.getImage())) {
        mapPane.getChildren().add(icon.getImage());
      }
    }
  }

  // Adds icon to map
  public void addIcon(Icon icon) {
    switch (icon.iconType) {
      case "Location":
        RequestSystem.getSystem().getLocationDao().addLocation(icon.getLocation());
    }
    PopupController.getController().closePopUp();
    MapManager.getManager().getFloor(floorName).addIcon(icon);
    checkDropDown();
  }

  public void addEquipmentIcon(Equipment equipment) {
    PopupController.getController().closePopUp();
    RequestSystem.getSystem().addEquipment(equipment);
    MapManager.getManager().setUpFloors();
    checkDropDown();
  }

  public void deleteIcon(Icon icon) {
    MapManager.getManager().getFloor(icon.getLocation().getFloor()).removeIcon(icon);
    RequestSystem.getSystem().deleteLocation(icon.getLocation().getNodeID());
    MapManager.getManager().setUpFloors();
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
                // System.out.println("MISSING FIELD");
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
      setSubmitLocation(xPos, yPos);
    }
  }
}

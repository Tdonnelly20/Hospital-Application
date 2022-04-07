package edu.wpi.cs3733.d22.teamV.controllers;

import com.jfoenix.controls.JFXComboBox;
import edu.wpi.cs3733.d22.teamV.main.Vdb;
import edu.wpi.cs3733.d22.teamV.manager.MapManager;
import edu.wpi.cs3733.d22.teamV.objects.Floor;
import edu.wpi.cs3733.d22.teamV.objects.Icon;
import edu.wpi.cs3733.d22.teamV.objects.Location;
import java.io.IOException;
import java.util.Objects;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import org.controlsfx.control.CheckComboBox;

public abstract class Controller extends Application {
  private Parent root;
  private Floor currFloor;
  ObservableList<String> filterItems =
      FXCollections.observableArrayList(
          "Equipment",
          "Clean Equipment",
          "Active Requests",
          "Lab Requests",
          "Equipment Delivery",
          "Meal Delivery",
          "Medicine Delivery",
          "Religious Request",
          "Sanitation Request",
          "Internal Patient Transport");
  @FXML CheckComboBox<String> filterCheckBox = new CheckComboBox<>(filterItems);

  @FXML private Pane mapPane;
  @FXML private ImageView mapImage;

  @FXML
  private JFXComboBox floorDropDown =
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
  public void init() {
    floorDropDown.setValue("1st Floor");

    checkDropDown();

    filterCheckBox = new CheckComboBox<>(filterItems);

    System.out.println(filterCheckBox.getCheckModel());

    filterCheckBox.getCheckModel().checkAll();

    /*  for (String str : filterCheckBox.getCheckModel().getCheckedItems()) {
      System.out.println(str);
    }*/
  }

  /** Checks the value of the floor drop down and matches it with the corresponding map png */
  @FXML
  private void checkDropDown() {
    MapManager.getManager().closePopUp();
    String url = floorDropDown.getValue().toString() + ".png";
    System.out.println(floorDropDown.getValue().toString() + ".png");
    mapImage.setImage(new Image(url));
    mapImage.setFitWidth(600);
    mapImage.setFitHeight(600);
    getFloor();
  }

  @FXML
  private void refresh() throws IOException {
    // Matt's csv
    Vdb.saveToLocationDB();
    getFloor();
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
      if (filter.size() == 0) {
        if (filter.contains("Equipment") && icon.isEquipment()) {
          mapPane.getChildren().add(icon.getImage());
        }
      } else {
        mapPane.getChildren().add(icon.getImage());
      }
    }
    /*if (filter.size() > 0) {
      if (filter.contains(equipment) && !filter.contains(requests)) {
        filterEquipment();
      } else if (!filter.contains(equipment) && filter.contains(requests)) {
        filterRequests();
      } else {
        filterRequests();
        filterEquipment();
      }
    } else {
      for (Icon icon : currFloor.getIconList()) {
        mapPane.getChildren().add(icon.getImage());
      }
    }*/
  }

  @FXML
  private void filterRequests() {
    for (Icon icon : currFloor.getIconList()) {
      if (!icon.isEquipment()) {
        /* if (filter.contains(requests)) {
          if (filter.contains(activeRequests)) {
            if (icon.getRequestsArr().size() > 0) {
              mapPane.getChildren().add(icon.getImage());
            }
          } else {
            mapPane.getChildren().add(icon.getImage());
          }
        }*/
      }
    }
  }

  @FXML
  private void filterEquipment() {
    for (Icon icon : currFloor.getIconList()) {
      if (icon.isEquipment()) {
        mapPane.getChildren().add(icon.getImage());
      }
    }
  }

  // Opens and manages the location adding form
  @FXML
  public void openIconFormWindow(MouseEvent event) {
    if (!event.getTarget().getClass().getTypeName().equals("javafx.scene.image.ImageView")) {
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
                  /*addIcon(mapManager.getLocation(xPos, yPos, getFloor()));

                  Vdb.locationDao.addLocation(mapManager.getLocation(xPos, yPos, getFloor()));
                  populateFloorIconArr();*/
                } else {
                  Text missingFields = new Text("Please fill all fields");
                  missingFields.setFill(Color.RED);
                  missingFields.setTextAlignment(TextAlignment.CENTER);
                  MapManager.getManager().getContent().getChildren().add(missingFields);
                  System.out.println("MISSING FIELD");
                }
              });
      // borderPane.centerProperty().setValue(content);
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
  private void addIcon(Location location) {
    MapManager.getManager().closePopUp();
    mapPane.getChildren().remove(MapManager.getManager().getTempIcon());
    MapManager.getManager().getFloor(getFloor()).addIcon(new Icon(location, false));
    MapManager.getManager().getTempIcon().setVisible(false);
    checkDropDown();
  }

  /* @FXML
  public void onlyActiveRequests() {
    if (filter.contains(activeRequests)) {
      filter.remove(activeRequests);
    } else {
      filter.add(activeRequests);
    }
    populateFloorIconArr();
  }*/

  /**
   * Determines if a String is an integer or not
   *
   * @param input is a string
   * @return true if the string is an integer, false if not
   */
  public boolean isInteger(String input) {
    try {
      Integer.parseInt(input);
      return true;
    } catch (NumberFormatException e) {
      return false;
    }
  }

  /**
   * Determines if a String is an double or not
   *
   * @param input is a string
   * @return true if the string is an double, false if not
   */
  public boolean isDouble(String input) {
    try {
      Double.parseDouble(input);
      return true;
    } catch (NumberFormatException e) {
      return false;
    }
  }

  // Closes the program
  @Override
  public void stop() {
    System.out.println("Shutting Down");
    Platform.exit();
  }

  // Switches scene to the home page
  @FXML
  public void switchToHome(ActionEvent event) throws IOException {
    root =
        FXMLLoader.load(
            Objects.requireNonNull(getClass().getClassLoader().getResource("FXML/home.fxml")));
    switchScene(event);
  }

  // Switches scene to the location database
  @FXML
  public void switchToLocationDB(ActionEvent event) throws IOException {
    FXMLLoader loader = new FXMLLoader();
    loader.setLocation(getClass().getClassLoader().getResource("FXML/LocationDatabase.fxml"));
    root = loader.load();
    LocationController lc = loader.getController();
    lc.init();
    lc.populateFloorIconArr();
    lc.setElements();
    lc.resetPage();
    switchScene(event);
  }

  // Switches scene to the service request page
  @FXML
  public void switchToServiceRequest(ActionEvent event) throws IOException {
    root =
        FXMLLoader.load(
            Objects.requireNonNull(getClass().getClassLoader().getResource("FXML/Test.fxml")));
    switchScene(event);
  }

  // Switches scene to the lab request page
  @FXML
  public void switchToLabRequest(ActionEvent event) throws IOException {
    root =
        FXMLLoader.load(
            Objects.requireNonNull(
                getClass().getClassLoader().getResource("FXML/LabRequest.fxml")));
    switchScene(event);
  }

  // Switches scene to the medicine delivery page
  @FXML
  public void switchToMedicineDelivery(ActionEvent event) throws IOException {
    root =
        FXMLLoader.load(
            Objects.requireNonNull(
                getClass().getClassLoader().getResource("FXML/MedicineDelivery.fxml")));
    switchScene(event);
  }

  // Switches scene to the medical equipment page
  @FXML
  public void switchToMedEquipDelivery(ActionEvent event) throws IOException {
    root =
        FXMLLoader.load(
            Objects.requireNonNull(
                getClass().getClassLoader().getResource("FXML/MedicalEquipmentDelivery.fxml")));
    switchScene(event);
  }

  // Switches scene to the sanitation page
  @FXML
  public void switchToSanitationRequests(ActionEvent event) throws IOException {
    root =
        FXMLLoader.load(
            Objects.requireNonNull(
                getClass().getClassLoader().getResource("FXML/SanitationRequest.fxml")));
    switchScene(event);
  }

  // Switches scene to the meal delivery page
  @FXML
  public void switchToMealDelivery(ActionEvent event) throws IOException {
    root =
        FXMLLoader.load(
            Objects.requireNonNull(
                getClass().getClassLoader().getResource("FXML/MealDelivery.fxml")));
    switchScene(event);
  }

  // Switches scene to the laundry request page
  @FXML
  public void switchToLaundryRequest(ActionEvent event) throws IOException {
    root =
        FXMLLoader.load(
            Objects.requireNonNull(
                getClass().getClassLoader().getResource("FXML/LaundryRequest.fxml")));
    switchScene(event);
  }

  // Switches scene to the religious request page
  @FXML
  public void switchToReligiousRequest(ActionEvent event) throws IOException {
    root =
        FXMLLoader.load(
            Objects.requireNonNull(
                getClass().getClassLoader().getResource("FXML/ReligiousRequest.fxml")));
    switchScene(event);
  }

  // Switches scene to the religious request page
  @FXML
  public void switchToInternalPatientTransport(ActionEvent event) throws IOException {

    root =
        FXMLLoader.load(
            Objects.requireNonNull(
                getClass()
                    .getClassLoader()
                    .getResource("FXML/InternalPatientTransportation.fxml")));
    switchScene(event);
  }

  // Switches scene to the rootW
  @FXML
  void switchScene(ActionEvent event) {
    MapManager.getManager().closePopUp();
    Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
    Scene scene = new Scene(root);
    stage.setScene(scene);
    stage.show();
  }
}

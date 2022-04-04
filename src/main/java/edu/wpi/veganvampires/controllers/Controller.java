package edu.wpi.veganvampires.controllers;

import com.jfoenix.controls.JFXComboBox;
import edu.wpi.veganvampires.main.Vdb;
import edu.wpi.veganvampires.manager.MapManager;
import edu.wpi.veganvampires.objects.Floor;
import edu.wpi.veganvampires.objects.Icon;
import edu.wpi.veganvampires.objects.Location;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

public abstract class Controller extends Application {
  private Parent root;
  ArrayList<Icon> currentIconArr;
  private Floor currFloor;

  @FXML private Pane mapPane;
  @FXML private ImageView mapImage;

  @FXML
  private JFXComboBox floorDropDown =
      new JFXComboBox<>(
          FXCollections.observableArrayList(
              "Ground Floor",
              "Lower Level 2",
              "Lower Level 1",
              "1st Floor",
              "2nd Floor",
              "3rd Floor"));

  /** Checks the value of the floor drop down and matches it with the corresponding map png */
  @FXML
  private void checkDropDown() {
    String url = floorDropDown.getValue().toString() + ".png";
    mapImage.setImage(new Image(url));
    System.out.println(floorDropDown.getValue());
    getFloor();
  }

  // Sets the mapImage to the corresponding floor dropdown and returns the floor string
  private String getFloor() {
    String result = "";
    switch (floorDropDown.getValue().toString()) {
      case "Ground Floor":
        currFloor = Vdb.mapManager.getFloor("G");
        result = "G";
        break;
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
    }

    populateFloorIconArr();
    return result;
  }

  // Loads the floor's icons
  @FXML
  public void populateFloorIconArr() {
    mapPane.getChildren().clear();
    System.out.println("Here");
    for (Icon icon : currFloor.getIconList()) {
      mapPane.getChildren().add(icon.getImage());
    }
  }

  @FXML
  public void openIconFormWindow(MouseEvent event) {
    if (!MapManager.getManager().getStage().isShowing()) {
      MapManager.getManager().getContent().getChildren().clear();
      // X and Y coordinates
      double xPos = event.getX() - 15;
      double yPos = event.getY() - 25;

      // Form
      Label header = new Label("Add an icon");
      header.setPrefWidth(300);
      header.setTextAlignment(TextAlignment.CENTER);
      header.setFont(new Font(18));
      TextField nodeIDField = new TextField();
      TextField nodeTypeField = new TextField();
      TextField shortNameField = new TextField();
      TextField longNameField = new TextField();
      Button submitIcon = new Button("Add icon");
      Button clearResponse = new Button("Clear Text");
      Button closeButton = new Button("Close");
      HBox hBox = new HBox(submitIcon, clearResponse, closeButton);
      hBox.setSpacing(15);

      nodeIDField.setPromptText("Node ID");
      nodeTypeField.setPromptText("Node Type");
      shortNameField.setPromptText("Short Name");
      longNameField.setPromptText("Long Name");
      nodeIDField.setMinWidth(250);
      nodeTypeField.setMinWidth(250);
      shortNameField.setMinWidth(250);
      longNameField.setMinWidth(250);
      submitIcon.setMinWidth(100);
      clearResponse.setMinWidth(100);

      submitIcon.setOnAction(
          event1 -> {
            if (!nodeIDField.getText().isEmpty()
                && !nodeTypeField.getText().isEmpty()
                && !shortNameField.getText().isEmpty()
                && !longNameField.getText().isEmpty()) {
              addIcon(
                  new Location(
                      nodeIDField.getText(),
                      xPos,
                      yPos,
                      getFloor(),
                      "Tower",
                      nodeTypeField.getText(),
                      longNameField.getText(),
                      shortNameField.getText()));
            } else {
              // TODO: Error message and aesthetic shit
              System.out.println("MISSING FIELD");
            }
          });
      clearResponse.setOnAction(
          event1 -> {
            nodeIDField.setText("");
            nodeTypeField.setText("");
            shortNameField.setText("");
            longNameField.setText("");
          });
      closeButton.setOnAction(
          event1 -> {
            MapManager.getManager().closePopUp();
          });
      MapManager.getManager()
          .getContent()
          .getChildren()
          .addAll(header, nodeIDField, nodeTypeField, shortNameField, longNameField, hBox);
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

      // Scene and Stage
      MapManager.getManager().getStage().setAlwaysOnTop(true);
      MapManager.getManager().getStage().setScene(MapManager.getManager().getScene());
      MapManager.getManager().getStage().setTitle("Add New Location");
      MapManager.getManager().getStage().show();
    }
  }

  // Adds icon to map
  private void addIcon(Location location) {
    MapManager.getManager().closePopUp();
    mapPane.getChildren().remove(MapManager.getManager().getTempIcon());
    MapManager.getManager().getFloor(getFloor()).addIcon(new Icon(location));
    // mapManager.getFloor(getFloor()).addIcon(new Icon(location));
    checkDropDown();
  }

  /*
  // If there is a popup window, close it
  @FXML
  public void closePopUp() {
    if (popUpStage.isShowing()) {
      popUpStage.close();
      if (mapPane.getChildren().contains(tempIcon)) {
        mapPane.getChildren().remove(tempIcon);
      }
    }
  }

  /**
   * When a place on the map (without an icon) is clicked it will open a popup window and place an
   * icon where you clicked
   */
  /*@FXML
  private void paneClicked(MouseEvent event) {
    if (!MapManager.getManager().isIconWindowOpen()) {
      addIconForm(event);
    }else{
      MapManager.getManager().closeIconWindow();
    }
  }

  public void addIconForm(MouseEvent event) {
    content.getChildren().clear();
    // X and Y coordinates
    double xPos = event.getX() - 15;
    double yPos = event.getY() - 25;

    // Form
    Label header = new Label("Add an icon");
    header.setPrefWidth(300);
    header.setTextAlignment(TextAlignment.CENTER);
    header.setFont(new Font(18));
    TextField nodeIDField = new TextField();
    TextField nodeTypeField = new TextField();
    TextField shortNameField = new TextField();
    TextField longNameField = new TextField();
    Button submitIcon = new Button("Add icon");
    Button clearResponse = new Button("Clear Text");
    Button closeButton = new Button("Close");
    HBox hBox = new HBox(submitIcon, clearResponse, closeButton);
    hBox.setSpacing(15);

    nodeIDField.setPromptText("Node ID");
    nodeTypeField.setPromptText("Node Type");
    shortNameField.setPromptText("Short Name");
    longNameField.setPromptText("Long Name");
    nodeIDField.setMinWidth(250);
    nodeTypeField.setMinWidth(250);
    shortNameField.setMinWidth(250);
    longNameField.setMinWidth(250);
    submitIcon.setMinWidth(100);
    clearResponse.setMinWidth(100);

    submitIcon.setOnAction(
        event1 -> {
          if (!nodeIDField.getText().isEmpty()
              && !nodeTypeField.getText().isEmpty()
              && !shortNameField.getText().isEmpty()
              && !longNameField.getText().isEmpty()) {
            addIcon(
                new Location(
                    nodeIDField.getText(),
                    xPos,
                    yPos,
                    getFloor(),
                    "Tower",
                    nodeTypeField.getText(),
                    longNameField.getText(),
                    shortNameField.getText()));
          } else {
            // TODO: Error message and aesthetic shit
            System.out.println("MISSING FIELD");
          }
        });
    clearResponse.setOnAction(
        event1 -> {
          nodeIDField.setText("");
          nodeTypeField.setText("");
          shortNameField.setText("");
          longNameField.setText("");
        });
    closeButton.setOnAction(
        event1 -> {
          closePopUp();
        });
    content
        .getChildren()
        .addAll(header, nodeIDField, nodeTypeField, shortNameField, longNameField, hBox);
    // borderPane.centerProperty().setValue(content);

    // Place Icon
    if (!mapPane.getChildren().contains(tempIcon)) {
      tempIcon = new ImageView("icon.png");
      tempIcon.setX(xPos);
      tempIcon.setY(yPos);
      System.out.println("X:" + xPos + " Y:" + yPos);
      tempIcon.setFitWidth(30);
      tempIcon.setFitHeight(30);
      mapPane.getChildren().add(tempIcon);
    } else {;
      tempIcon.setX(xPos);
      tempIcon.setY(yPos);
    }

    // Scene and Stage
    popUpStage.setAlwaysOnTop(true);
    popUpStage.setScene(popUpScene);
    popUpStage.setTitle("Add New Location");
    popUpStage.show();
  }

  // Adds icon to map
  private void addIcon(Location location) {
    popUpStage.close();
    mapPane.getChildren().remove(tempIcon);
    MapManager.getManager().getFloor(getFloor()).addIcon(new Icon(location));
    // mapManager.getFloor(getFloor()).addIcon(new Icon(location));
    checkDropDown();
  }
  */

  public boolean mapPaneContains(Icon icon) {
    return mapPane.getChildren().contains(icon);
  }

  @FXML
  public void removeFromMapPane(Node node) {
    mapPane.getChildren().remove(node);
  }

  @FXML
  public void addToMapPane(Node node) {
    mapPane.getChildren().add(node);
  }

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
    lc.setElements();
    lc.resetPage();
    switchScene(event);
  }

  // Switches scene to the service request page
  @FXML
  public void switchToServiceRequest(ActionEvent event) throws IOException {
    root =
        FXMLLoader.load(
            Objects.requireNonNull(
                getClass().getClassLoader().getResource("FXML/serviceRequest.fxml")));
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
    checkDropDown();
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
    /*
    root =
        FXMLLoader.load(
            Objects.requireNonNull(
                getClass().getClassLoader().getResource("FXML/ReligiousRequest.fxml")));
    switchScene(event);
    * */
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

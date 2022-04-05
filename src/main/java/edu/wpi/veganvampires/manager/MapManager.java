package edu.wpi.veganvampires.manager;

import com.jfoenix.controls.JFXComboBox;
import edu.wpi.veganvampires.main.Vdb;
import edu.wpi.veganvampires.objects.*;
import java.util.ArrayList;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MapManager {
  private ArrayList<Floor> floorList;
  @FXML private ImageView tempIcon = new ImageView("icon.png");
  @FXML VBox content = new VBox(15);
  @FXML Scene scene = new Scene(content, 450, 450);
  @FXML Stage stage = new Stage();
  @FXML Button locationButton = new Button("Add Location");
  @FXML Button equipmentButton = new Button("Add Equipment");
  @FXML Button submitIcon = new Button("Add icon");
  @FXML Button clearResponse = new Button("Clear Text");
  @FXML Button closeButton = new Button("Close");
  @FXML Label title = new Label("Add");
  @FXML HBox titleBox = new HBox(15, title);
  @FXML HBox buttonBox = new HBox(15);
  @FXML Text missingFields = new Text("Please fill all fields");
  @FXML TextField field1 = new TextField();
  @FXML TextField field2 = new TextField();
  @FXML TextField field3 = new TextField();
  @FXML TextField field4 = new TextField();

  private MapManager() {
    setUpPopUp();
    setUpFloors();
  }

  private static class SingletonHelper {
    private static final MapManager manager = new MapManager();
  }

  public static MapManager getManager() {
    return SingletonHelper.manager;
  }

  /** Sets up the popup window */
  @FXML
  private void setUpPopUp() {

    field1.setMaxWidth(250);
    field2.setMaxWidth(250);
    field3.setMaxWidth(250);
    field4.setMaxWidth(250);
    submitIcon.setMinWidth(100);
    clearResponse.setMinWidth(100);

    stage.setMaxHeight(450);
    stage.setMaxWidth(450);
    stage.setMinHeight(450);
    stage.setMinWidth(450);
    stage.setOnCloseRequest(
        event -> {
          tempIcon.setVisible(false);
        });
  }

  /** Sets up floor elements */
  private void setUpFloors() {
    floorList = new ArrayList<>();

    Floor g = new Floor("G");
    Floor l1 = new Floor("L1");
    Floor l2 = new Floor("L2");
    Floor f1 = new Floor("1");
    Floor f2 = new Floor("2");
    Floor f3 = new Floor("3");

    floorList.add(g);
    floorList.add(l1);
    floorList.add(l2);
    floorList.add(f1);
    floorList.add(f2);
    floorList.add(f3);
    System.out.println("SIZE: " + floorList.size());
    // TODO Implement equipment csv
    ArrayList<Location> locations = Vdb.locationDao.getAllLocations();
    ArrayList<EquipmentDelivery> equipment = Vdb.equipmentDeliveryDao.getAllEquipmentDeliveries();

    for (Location l : locations) {
      switch (l.getFloor()) {
        case "G":
          loadRequests(0, l);
          break;
        case "L1":
          loadRequests(1, l);
          break;
        case "L2":
          loadRequests(2, l);
          break;
        case "1":
          loadRequests(3, l);
          break;
        case "2":
          loadRequests(4, l);
          break;
        case "3":
          loadRequests(5, l);
          break;
      }
    }

    for (EquipmentDelivery equipmentDelivery :
        Vdb.equipmentDeliveryDao.getAllEquipmentDeliveries()) {
      for (Floor floor : floorList) {
        if (equipmentDelivery.getLocation().getFloor().equals(floor.getFloorName())) {
          for (Icon icon : floor.getIconList()) {
            if (icon.getLocation().getNodeID().equals(equipmentDelivery.getLocation())) {
              icon.addToRequests(equipmentDelivery);
            }
          }
        }
      }
    }

    /*System.out.println("Size: " + equipment.size());
    for (Equipment e : equipment) {
      System.out.printf(e.getLocation());

      switch (Vdb.locationDao.getLocation(e.getLocation()).getFloor()) {
        case "G":
          floorList.get(0).addIcon(new Icon(Vdb.locationDao.getLocation(e.getLocation()), true));
          break;
        case "L1":
          floorList.get(1).addIcon(new Icon(Vdb.locationDao.getLocation(e.getLocation()), true));
          break;
        case "L2":
          floorList.get(2).addIcon(new Icon(Vdb.locationDao.getLocation(e.getLocation()), true));
          break;
        case "1":
          floorList.get(3).addIcon(new Icon(Vdb.locationDao.getLocation(e.getLocation()), true));
          break;
        case "2":
          floorList.get(4).addIcon(new Icon(Vdb.locationDao.getLocation(e.getLocation()), true));
          break;
        case "3":
          floorList.get(5).addIcon(new Icon(Vdb.locationDao.getLocation(e.getLocation()), true));
          break;
      }
    }*/
  }

  public void loadRequests(int i, Location l) {
    Icon icon;
    if (floorList.size() > 0) {
      if (floorList.get(i).hasIconAt(l)) {
        icon = floorList.get(i).getIconAt(l);
      } else {
        icon = new Icon(l, false);
        floorList.get(i).addIcon(icon);
      }
    } else {
      icon = new Icon(l, false);
      floorList.get(i).addIcon(icon);
    }
    for (EquipmentDelivery equipmentDelivery :
        Vdb.equipmentDeliveryDao.getAllEquipmentDeliveries()) {
      if (equipmentDelivery.getLocation().equals(l)) {
        icon.addToRequests(equipmentDelivery);
      }
    }
  }

  @FXML
  public void addRequest() {
    System.out.println(floorList.get(1).getIconList().size());
    System.out.println(floorList.get(1).getIconList().get(0).getLocation().getLongName());
    floorList
        .get(1)
        .getIconList()
        .get(0)
        .addToRequests(
            new EquipmentDelivery(
                floorList.get(1).getIconList().get(0).getLocation().getNodeID(),
                123,
                "I",
                "stg",
                10,
                "whyyyyyyyyyyyyyyyyy"));
  }

  /**
   * @param str
   * @return Floor that corresponds to str
   */
  public Floor getFloor(String str) {
    switch (str) {
      case "G":
        return floorList.get(0);
      case "L1":
        return floorList.get(1);
      case "L2":
        return floorList.get(2);
      case "1":
        return floorList.get(3);
      case "2":
        return floorList.get(4);
      case "3":
        return floorList.get(5);
    }
    return null;
  }

  /** Closes the popup window */
  @FXML
  public void closePopUp() {
    if (stage.isShowing()) {
      stage.close();
    }
  }

  /** Opens the popup window */
  @FXML
  public void showPopUp() {
    stage.setScene(scene);
    if (!stage.isShowing()) {
      stage.show();
    }
  }

  /** Opens the corresponding icon's request window */
  @FXML
  public void openIconRequestWindow(Icon icon) {
    // Display requests/info
    content.getChildren().clear();
    tempIcon.setVisible(false);
    Label title = new Label(icon.getLocation().getShortName());
    title.setTextFill(Color.WHITE);
    title.setFont(new Font("System Bold", 28));
    HBox titleBox = new HBox(25, title);
    titleBox.setAlignment(Pos.CENTER);
    titleBox.setStyle("-fx-background-color: #012D5Aff;");
    content.getChildren().add(titleBox);
    VBox vbox = new VBox();
    vbox.setAlignment(Pos.TOP_CENTER);
    ObservableList<String> statusStrings =
        FXCollections.observableArrayList("Not Started", "Processing", "Done");
    if (icon.getRequestsArr().size() > 0) {
      for (ServiceRequest request : icon.getRequestsArr()) {
        Label idLabel = new Label("Employee: " + request.getHospitalEmployee().getHospitalID());
        Label locationLabel =
            new Label(
                "X: " + icon.getLocation().getXCoord() + " Y: " + icon.getLocation().getYCoord());

        JFXComboBox<String> updateStatus = new JFXComboBox<>(statusStrings);
        updateStatus.setValue(request.getStatus());
        updateStatus.setOnAction(
            event -> {
              request.setStatus(updateStatus.getValue().toString());
              // TODO: Update request CSV
            });
        Accordion accordion =
            new Accordion(
                new TitledPane(
                    request.getRequestName() + ": " + request.getStatus(),
                    new VBox(15, idLabel, locationLabel, updateStatus)));
        vbox.getChildren().add(accordion);
      }
    } else {
      Text noRequests = new Text("There are no requests in this area");
      noRequests.setTextAlignment(TextAlignment.CENTER);
      vbox.getChildren().add(noRequests);
    }
    content.getChildren().add(vbox);
    showPopUp();
  }

  @FXML
  public void formSetup() {
    title.setText("Add a Location");
    title.setTextFill(Color.WHITE);
    title.setAlignment(Pos.CENTER);
    title.setFont(new Font("System Bold", 38));
    title.setWrapText(true);
    HBox titleBox = new HBox(15, title);
    titleBox.setStyle("-fx-background-color: #012D5Aff;");
    titleBox.setAlignment(Pos.CENTER);
    HBox buttonBox = new HBox(submitIcon, clearResponse, closeButton);
    buttonBox.setAlignment(Pos.CENTER);
    buttonBox.setSpacing(15);
    content.getChildren().addAll(titleBox, buttonBox);

    clearResponse.setOnAction(
        event1 -> {
          clearPopupForm();
        });
    closeButton.setOnAction(
        event1 -> {
          closePopUp();
          tempIcon.setVisible(false);
        });
    VBox vBox = new VBox(15, field1, field2, field3, field4);
    vBox.setAlignment(Pos.TOP_CENTER);
    content.getChildren().addAll(vBox);
  }

  @FXML
  public void locationForm(MouseEvent event, boolean isEquipment) {
    tempIcon.setVisible(true);
    content.getChildren().clear();
    // Form
    formSetup();

    field1.setPromptText("Node ID");
    field2.setPromptText("Node Type");
    field3.setPromptText("Short Name");
    field4.setPromptText("Long Name");

    // Scene and Stage
    stage.setTitle("Add New Location");
    showPopUp();
  }

  @FXML
  public void equipmentForm(MouseEvent event) {
    tempIcon.setVisible(true);
    content.getChildren().clear();
    // Form
    formSetup();

    field1.setPromptText("Node ID");
    field2.setPromptText("Node Type");
    field3.setPromptText("Short Name");
    field4.setPromptText("Long Name");

    // Scene and Stage
    MapManager.getManager().getStage().setTitle("Add New Location");
    MapManager.getManager().showPopUp();
  }

  public boolean checkFields() {
    return !field1.getText().isEmpty()
        && !field2.getText().isEmpty()
        && !field3.getText().isEmpty()
        && !field4.getText().isEmpty();
  }

  public Location getLocation(double xPos, double yPos, String floor) {
    return new Location(
        field1.getText(),
        xPos,
        yPos,
        floor,
        "Tower",
        field2.getText(),
        field3.getText(),
        field4.getText());
  }

  @FXML
  private void clearPopupForm() {
    field1.setText("");
    field2.setText("");
    field3.setText("");
    field4.setText("");
    buttonBox.getChildren().clear();
    titleBox.getChildren().clear();
  }
  /*


  @FXML
  private void setUpPopupButtons(MouseEvent event, double xPos, double yPos) {
    MapManager mapManager = MapManager.getManager();
    buttonBox.getChildren().addAll(locationButton, equipmentButton, closeButton);
    buttonBox.setAlignment(Pos.CENTER);
    locationButton.setOnAction(
        event1 -> {
          mapManager.getTempIcon().setImage(new Image("icon.png"));
          addLocationForm(event, xPos, yPos, false);
        });
    equipmentButton.setOnAction(
        event1 -> {
          mapManager.getTempIcon().setImage(new Image("Equipment.png"));
          addLocationForm(event, xPos, yPos, true);
        });
    closeButton.setOnAction(
        event1 -> {
          mapManager.closePopUp();
          mapManager.getTempIcon().setVisible(false);
          clearPopupForm();
        });
  }


  @FXML
  private void setUpPopup(MouseEvent event) {
    MapManager mapManager = MapManager.getManager();
    mapManager.getContent().getChildren().clear();
    // Setup
    double xPos = event.getX() - 15;
    double yPos = event.getY() - 25;
    setUpPopupButtons(event, xPos, yPos);
    setUpPopupText();
    mapManager.getContent().getChildren().addAll(titleBox, buttonBox);
    // Place Icon
    mapManager.getTempIcon().setVisible(true);
    mapManager.getTempIcon().setX(xPos);
    mapManager.getTempIcon().setY(yPos);
    if (!mapPane.getChildren().contains(MapManager.getManager().getTempIcon())) {
      System.out.println("X:" + xPos + " Y:" + yPos);
      mapManager.getTempIcon().setFitWidth(30);
      mapManager.getTempIcon().setFitHeight(30);
      mapPane.getChildren().add(MapManager.getManager().getTempIcon());
    }
  }*/
  /*
  @FXML
  public void openIconFormWindow(MouseEvent event) {
    MapManager mapManager = MapManager.getManager();
    System.out.printf(event.getTarget().getClass().getTypeName());
    if (!event.getTarget().getClass().getTypeName().equals("javafx.scene.image.ImageView")) {
      setUpPopup(event);
      // Scene and Stage
      mapManager.getStage().setTitle("Add New Location");
      mapManager.showPopUp();
    }
  }

  @FXML
  private void addLocationForm(MouseEvent event, double xPos, double yPos, boolean isEquipment) {
    MapManager mapManager = MapManager.getManager();
    clearPopupForm();
    // Form
    field1.setPromptText("Node ID");
    field2.setPromptText("Node Type");
    field3.setPromptText("Short Name");
    field4.setPromptText("Long Name");

    mapManager.getContent().getChildren().addAll(field1, field2, field3, field4);
    // Place Icon
    if (isEquipment) {
      mapManager.getStage().setTitle("Add Equipment");
    } else {
      mapManager.getStage().setTitle("Add New Location");
    }

    if (isEquipment) {
      submitButton = new Button("Continue");
    } else {
      submitButton = new Button("Add icon");
    }
    submitButton.setOnAction(
            event1 -> {
              if (!field1.getText().isEmpty()
                      && !field2.getText().isEmpty()
                      && !field3.getText().isEmpty()
                      && !field4.getText().isEmpty()) {
                Location location =
                        new Location(
                                field1.getText(),
                                xPos,
                                yPos,
                                getFloor(),
                                "Tower",
                                field2.getText(),
                                field4.getText(),
                                field3.getText());
                if (isEquipment) {
                  addEquipmentForm(location);
                } else {
                  addIcon(location, isEquipment);
                }
              } else {
                mapManager.getContent().getChildren().add(missingFields);
              }
            });

    title.setText("Please add a location");
    titleBox.getChildren().add(title);
    buttonBox.getChildren().clear();
    buttonBox.getChildren().addAll(submitButton, clearForm, closeButton);
    mapManager.getContent().getChildren().addAll(titleBox, buttonBox);

    mapPane.getChildren().add(MapManager.getManager().getTempIcon());
    mapManager.showPopUp();
  }

  @FXML
  private void addEquipmentForm(Location location) {
    MapManager mapManager = MapManager.getManager();
    clearPopupForm();
    // Form
    field1.setPromptText("Node ID");
    field2.setPromptText("Node Type");
    field3.setPromptText("Short Name");
    field4.setPromptText("Long Name");

    mapManager.getContent().getChildren().addAll(field1, field2, field3, field4);
    // Place Icon
    mapManager.getStage().setTitle("Add Equipment");
    submitButton = new Button("Add icon");

    submitButton.setOnAction(
            event1 -> {
              if (!field1.getText().isEmpty()
                      && !field2.getText().isEmpty()
                      && !field3.getText().isEmpty()
                      && !field4.getText().isEmpty()) {
                addIcon(location, true);
              } else {
                mapManager.getContent().getChildren().add(missingFields);
              }
            });

    title.setText("Please add equipment");
    titleBox.getChildren().add(title);
    buttonBox.getChildren().clear();
    buttonBox.getChildren().addAll(submitButton, clearForm, closeButton);
    mapManager.getContent().getChildren().addAll(titleBox, buttonBox);

    mapPane.getChildren().add(MapManager.getManager().getTempIcon());
    mapManager.showPopUp();
  }

  // Adds icon to map
  private void addIcon(Location location, Boolean isEquipment) {
    MapManager.getManager().closePopUp();
    mapPane.getChildren().remove(MapManager.getManager().getTempIcon());
    MapManager.getManager().getFloor(getFloor()).addIcon(new Icon(location, isEquipment));
    MapManager.getManager().getTempIcon().setVisible(false);
    checkDropDown();
  }
   */
}

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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
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

    field1.setMinWidth(250);
    field2.setMinWidth(250);
    field3.setMinWidth(250);
    field4.setMinWidth(250);
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

    // TODO Implement equipment csv
    ArrayList<Location> locations = Vdb.locationDao.getAllLocations();
    ArrayList<EquipmentDelivery> equipment = Vdb.equipmentDeliveryDao.getAllEquipmentDeliveries();
    for (Location l : locations) {
      switch (l.getFloor()) {
        case "G":
          floorList.get(0).addIcon(new Icon(l, false));
          break;
        case "L1":
          floorList.get(1).addIcon(new Icon(l, false));
          break;
        case "L2":
          floorList.get(2).addIcon(new Icon(l, false));
          break;
        case "1":
          floorList.get(3).addIcon(new Icon(l, false));
          break;
        case "2":
          floorList.get(4).addIcon(new Icon(l, false));
          break;
        case "3":
          floorList.get(5).addIcon(new Icon(l, false));
          break;
      }
    }
    /*for (EquipmentDelivery e : equipment) {
      System.out.printf(e.getLocation().getNodeID());
      switch (e.getLocation().getFloor()) {
        case "G":
          floorList.get(0).addIcon(new Icon(e.getLocation(), true));
          break;
        case "L1":
          floorList.get(1).addIcon(new Icon(e.getLocation(), true));
          break;
        case "L2":
          floorList.get(2).addIcon(new Icon(e.getLocation(), true));
          break;
        case "1":
          floorList.get(3).addIcon(new Icon(e.getLocation(), true));
          break;
        case "2":
          floorList.get(4).addIcon(new Icon(e.getLocation(), true));
          break;
        case "3":
          floorList.get(5).addIcon(new Icon(e.getLocation(), true));
          break;
      }
    }*/
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
    HBox titleBox = new HBox(15, title);
    titleBox.setAlignment(Pos.CENTER);
    titleBox.setStyle("-fx-background-color: #012D5Aff;");
    content.getChildren().add(titleBox);
    VBox vbox = new VBox();
    vbox.setAlignment(Pos.TOP_LEFT);
    ObservableList<String> statusStrings = FXCollections.observableArrayList("Processing", "Done");
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
    content.getChildren().add(vbox);
    showPopUp();
  }

  @FXML
  public void formSetup() {
    title.setTextFill(Color.WHITE);
    title.setAlignment(Pos.CENTER);
    // title.setAlignment(TextAlignment.CENTER);
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

    content.getChildren().addAll(field1, field2, field3, field4);
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
}

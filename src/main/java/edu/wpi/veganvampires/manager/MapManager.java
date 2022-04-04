package edu.wpi.veganvampires.manager;

import edu.wpi.veganvampires.main.Vdb;
import edu.wpi.veganvampires.objects.Floor;
import edu.wpi.veganvampires.objects.Icon;
import edu.wpi.veganvampires.objects.Location;
import edu.wpi.veganvampires.objects.ServiceRequest;
import java.util.ArrayList;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MapManager {
  private ArrayList<Floor> floorList;
  private boolean isRequestWindowOpen = false;
  @FXML private ImageView tempIcon = new ImageView("icon.png");
  @FXML VBox content = new VBox(15);
  @FXML Scene scene = new Scene(content, 450, 450);
  @FXML Stage stage = new Stage();

  private MapManager() {
    stage.setMaxHeight(450);
    stage.setMaxWidth(450);
    stage.setMinHeight(450);
    stage.setMinWidth(450);
    stage.setAlwaysOnTop(true);
    stage.setOnCloseRequest(
        event -> {
          tempIcon.setVisible(false);
          isRequestWindowOpen = false;
        });
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

    ArrayList<Location> locations = Vdb.locationDao.getAllLocations();
    System.out.println("Iterating through locations of size: " + locations.size());
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
  }

  private static class SingletonHelper {
    private static final MapManager manager = new MapManager();
  }

  public static MapManager getManager() {
    return SingletonHelper.manager;
  }

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

  public void closePopUp() {
    if (stage.isShowing()) {
      isRequestWindowOpen = false;
      stage.close();
    }
  }

  // Opens the corresponding icon's request window
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
    for (ServiceRequest request : icon.getRequestsArr()) {
      Label idLabel = new Label("Employee: " + request.getHospitalEmployee().getHospitalID());
      Label locationLabel =
          new Label(
              "X: " + icon.getLocation().getXCoord() + " Y: " + icon.getLocation().getYCoord());

      Accordion accordion =
          new Accordion(
              new TitledPane(
                  request.getRequestName() + ": " + request.getStatus(),
                  new VBox(15, idLabel, locationLabel)));
      vbox.getChildren().add(accordion);
    }
    content.getChildren().add(vbox);
    stage.setTitle(icon.getLocation().getLongName());
    showPopUp();
    isRequestWindowOpen = true;
  }

  @FXML
  public void showPopUp() {

    stage.setScene(scene);
    if (!stage.isShowing()) {
      stage.show();
    }
  }
}

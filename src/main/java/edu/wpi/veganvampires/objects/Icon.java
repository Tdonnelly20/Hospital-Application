package edu.wpi.veganvampires.objects;

import java.util.ArrayList;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Accordion;
import javafx.scene.control.TitledPane;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Icon {
  private double xCoord;
  private double yCoord;
  @FXML private ImageView image;
  private ArrayList<ServiceRequest> requestsArr;
  private boolean isEquipment;

  public Icon(Location location, boolean isEquipment) {
    // TODO: Remove test requests
    ServiceRequest l1 = new LabRequest(123, 456, "first", "last", "Blood", "Processing");
    ServiceRequest l2 = new LabRequest(789, 0, "firstName", "surname", "urine", "done");
    System.out.println(l1.getPatient().getFirstName() + " " + l1.getPatient().getLastName());
    this.xCoord = location.getXCoord();
    this.yCoord = location.getYCoord();
    this.requestsArr = new ArrayList<>();
    requestsArr.add(l1);
    requestsArr.add(l2);
    if (isEquipment) {
      image = new ImageView("Equipment.png");
    } else {
      image = new ImageView("icon.png");
    }
    image.setFitWidth(30);
    image.setFitHeight(30);
    image.setX(xCoord / 1.5);
    image.setY(yCoord / 1.5);
    image.setOnMouseClicked(
        event -> {
          // Display requests/info
          BorderPane borderPane = new BorderPane();
          Scene scene = new Scene(borderPane, 300, 300);
          Stage stage = new Stage();
          stage.setScene(scene);
          stage.setTitle(location.getShortName());
          if (!stage.isShowing()) {
            stage.show();
          }
          VBox vBox = new VBox();
          Text locationName = new Text(location.getLongName());
          vBox.getChildren().add(locationName);
          for (ServiceRequest request : requestsArr) {
            Accordion accordion =
                new Accordion(
                    new TitledPane(request.getRequestName() + ": " + request.getStatus(), null));
            vBox.getChildren().add(accordion);
          }
          borderPane.getChildren().add(vBox);
        });
  }

  public Icon(Location location, ArrayList<ServiceRequest> requestsArr) {
    System.out.println("ICON");
    setXY(location);
    this.requestsArr = requestsArr;
    if (requestsArr.isEmpty()) {
      System.out.println(1);
      image = new ImageView("icon.png");
    } else {
      System.out.println(2);
      image = new ImageView("markedIcon.png");
    }
    image.setFitWidth(30);
    image.setFitHeight(30);
    image.setX(xCoord / 1.5);
    image.setY(yCoord / 1.5);
    // rectangle = new Rectangle(xCoord, yCoord, image.getFitWidth(), image.getFitHeight());
    image.setOnMouseClicked(
        event -> {
          // Display requests/info
          BorderPane borderPane = new BorderPane();
          Scene scene = new Scene(borderPane, 300, 300);
          Stage stage = new Stage();
          stage.setScene(scene);
          stage.setTitle(location.getShortName());
          if (!stage.isShowing()) {
            stage.show();
          }
          VBox vBox = new VBox();
          Text locationName = new Text(location.getLongName());
          vBox.getChildren().add(locationName);
          for (ServiceRequest request : requestsArr) {
            Accordion accordion =
                new Accordion(
                    new TitledPane(request.getRequestName() + ": " + request.getStatus(), null));
            vBox.getChildren().add(accordion);
          }
          borderPane.getChildren().add(vBox);
        });
  }

  // Calculates where X and Y should be
  private void setXY(Location location) {
    this.xCoord = location.getXCoord();
    this.yCoord = location.getYCoord();
  }
}

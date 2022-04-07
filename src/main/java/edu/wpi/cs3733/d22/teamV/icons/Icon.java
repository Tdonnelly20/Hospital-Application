package edu.wpi.cs3733.d22.teamV.icons;

import edu.wpi.cs3733.d22.teamV.manager.MapManager;
import edu.wpi.cs3733.d22.teamV.objects.Location;
import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class Icon {
  public String iconType;
  protected Location location;
  protected double xCoord;
  protected double yCoord;
  @FXML protected ImageView image;

  public Icon(Location location) {
    this.xCoord = location.getXCoord();
    this.yCoord = location.getYCoord();
    this.location = location;
    this.image = new ImageView();
    image.setFitWidth(30);
    image.setFitHeight(30);
    image.setTranslateX((xCoord) - 25);
    image.setTranslateY((yCoord) - 15);
    image.setOnMouseClicked(
        event -> {
          MapManager.getManager().openIconRequestWindow(this);
        });
  }

  /*
  public Icon(Location location, boolean isEquipment) {
    this.xCoord = location.getXCoord();
    this.yCoord = location.getYCoord();
    image.setFitWidth(30);
    image.setFitHeight(30);
    image.setTranslateX((xCoord) - 25);
    image.setTranslateY((yCoord) - 15);
    this.location = location;
    image.setOnMouseClicked(
        event -> {
          MapManager.getManager().openIconRequestWindow(this);
        });
  }

  public Icon(Location location, boolean isEquipment, ArrayList<ServiceRequest> requestsArr) {
    this.xCoord = location.getXCoord();
    this.yCoord = location.getYCoord();

    image.setFitWidth(30);
    image.setFitHeight(30);
    image.setTranslateX((xCoord) - 25);
    image.setTranslateY((yCoord) - 15);
    this.location = location;
    image.setOnMouseClicked(
        event -> {
          MapManager.getManager().openIconRequestWindow(this);
        });
  }

  public Icon(Location location, Equipment equipment) {
    this.xCoord = location.getXCoord();
    this.yCoord = location.getYCoord();
    this.requestsArr = requestsArr;
    image = new ImageView("Equipment.png");
    this.equipment = equipment;
    isEquipment = true;
    image.setFitWidth(30);
    image.setFitHeight(30);
    image.setTranslateX((xCoord) - 25);
    image.setTranslateY((yCoord) - 15);
    this.location = location;
    image.setOnMouseClicked(
        event -> {
          MapManager.getManager().openIconRequestWindow(this);
        });
  }*/
}

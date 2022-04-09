package edu.wpi.cs3733.d22.teamV.map;

import edu.wpi.cs3733.d22.teamV.manager.MapManager;
import edu.wpi.cs3733.d22.teamV.objects.Location;
import javafx.scene.image.Image;

public class LocationIcon extends Icon {

  public LocationIcon(Location location) {
    super(location);
    this.iconType = "Location";
    image.setImage(new Image("icon.png"));
    image.setFitWidth(30);
    image.setFitHeight(30);
    image.setTranslateX((xCoord) - 25);
    image.setTranslateY((yCoord) - 15);
    image.setOnMouseClicked(
        event -> {
          MapManager.getManager().openIconRequestWindow(this);
        });
  }
}

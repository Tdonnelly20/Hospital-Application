package edu.wpi.cs3733.d22.teamV.icons;

import edu.wpi.cs3733.d22.teamV.manager.MapManager;
import edu.wpi.cs3733.d22.teamV.objects.Location;
import javafx.scene.image.Image;

public class EquipmentIcon extends Icon {

  public EquipmentIcon(Location location) {
    super(location);
    this.iconType = "Equipment";
    image.setImage(new Image("Equipment.png"));
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

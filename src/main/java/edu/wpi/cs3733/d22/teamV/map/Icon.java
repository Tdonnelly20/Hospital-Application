package edu.wpi.cs3733.d22.teamV.map;

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
    image.setTranslateX(xCoord);
    image.setTranslateY(yCoord);
    /*image.setOnMouseClicked(
    event -> {
      if (event.getClickCount() == 2) {
        PopupController.getController().openIconRequestWindow(this);
      }
    });*/
  }
}

package edu.wpi.veganvampires.icons;

import edu.wpi.veganvampires.objects.Location;
import edu.wpi.veganvampires.objects.ServiceRequest;
import java.util.ArrayList;
import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Rectangle;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Icon {
  private double xCoord;
  private double yCoord;
  @FXML private ImageView image;
  @FXML private Rectangle rectangle;
  private ArrayList<ServiceRequest> requestsArr;

  public Icon(Location location) {
    new Icon(location, new ArrayList<ServiceRequest>());
  }

  public Icon(Location location, ArrayList<ServiceRequest> requestsArr) {
    System.out.println("ICON");
    setXY(location);
    this.requestsArr = requestsArr;
    if (requestsArr.isEmpty()) {
      image = new ImageView("icon.png");
    } else {
      image = new ImageView("markedIcon.png");
    }
    image.setX(xCoord);
    image.setY(yCoord);
    rectangle = new Rectangle(xCoord, yCoord, image.getFitWidth(), image.getFitHeight());
  }

  // Calculates where X and Y should be
  private void setXY(Location location) {
    this.xCoord = location.getXCoord() - 1464;
    this.yCoord = location.getYCoord() - 759;
  }
}

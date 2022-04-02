package edu.wpi.veganvampires.objects;

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
      System.out.println(1);
      image = new ImageView("icon.png");
    } else {
      System.out.println(2);
      image = new ImageView("markedIcon.png");
    }
    image.setX(xCoord);
    image.setY(yCoord);
    rectangle = new Rectangle(xCoord, yCoord, image.getFitWidth(), image.getFitHeight());
  }

  // Calculates where X and Y should be
  private void setXY(Location location) {
    this.xCoord = location.getXCoord();
    this.yCoord = location.getYCoord();
  }
}

package edu.wpi.veganvampires.objects;

import java.util.ArrayList;
import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Icon {
  private double xCoord;
  private double yCoord;
  @FXML private ImageView image;
  // @FXML private Rectangle rectangle;
  private ArrayList<ServiceRequest> requestsArr;

  public Icon(Location location) {
    this.xCoord = location.getXCoord();
    this.yCoord = location.getYCoord();
    this.requestsArr = new ArrayList<>();
    image = new ImageView("icon.png");
    image.setFitWidth(60);
    image.setFitHeight(60);
    image.setX(xCoord);
    image.setY(yCoord);
    // rectangle = new Rectangle(xCoord, yCoord, image.getFitWidth(), image.getFitHeight());
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
    image.setFitWidth(60);
    image.setFitHeight(60);
    image.setX(xCoord);
    image.setY(yCoord);
    // rectangle = new Rectangle(xCoord, yCoord, image.getFitWidth(), image.getFitHeight());
  }

  // Calculates where X and Y should be
  private void setXY(Location location) {
    this.xCoord = location.getXCoord();
    this.yCoord = location.getYCoord();
  }
}

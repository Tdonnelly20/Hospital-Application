package edu.wpi.cs3733.d22.teamV.map;

import edu.wpi.cs3733.d22.teamV.objects.Location;
import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.ImageView;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class Icon {

  public enum IconType {
    Location,
    Equipment
  }

  public IconType iconType;
  protected Location location;
  protected double xCoord;
  protected double yCoord;
  @FXML protected ImageView image;

  public Icon(Location location) {
    this.xCoord = location.getXCoord();
    this.yCoord = location.getYCoord();
    this.location = location;
    this.image = new ImageView();
    image.setFitWidth(15);
    image.setFitHeight(15);
    image.setTranslateX(xCoord);
    image.setTranslateY(yCoord);
    image.setOnMousePressed(
        e -> {
          // prevent pannable ScrollPane from changing cursor on drag-detected (implementation
          // detail)
          e.setDragDetect(false);
          Point2D offset = new Point2D(e.getX() - image.getX() - 15, e.getY() - image.getY() - 20);
          image.setUserData(offset);
          e.consume(); // prevents MouseEvent from reaching ScrollPane
        });
    image.setOnMouseDragged(
        e -> {
          // prevent pannable ScrollPane from changing cursor on drag-detected (implementation
          // detail)
          e.setDragDetect(false);
          Point2D offset = (Point2D) image.getUserData();
          image.setX(e.getX() - offset.getX() - 15);
          image.setY(e.getY() - offset.getY() - 20);
          e.consume(); // prevents MouseEvent from reaching ScrollPane
        });
    image.setOnMouseEntered(
        event -> {
          image.setFitWidth(50);
          image.setFitHeight(50);
        });
    image.setOnMouseExited(
        event -> {
          image.setFitWidth(15);
          image.setFitHeight(15);
        });
  }

  @FXML
  public abstract ScrollPane compileList();
}

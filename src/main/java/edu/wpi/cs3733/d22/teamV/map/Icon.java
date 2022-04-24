package edu.wpi.cs3733.d22.teamV.map;

import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.scene.image.ImageView;
import javafx.scene.input.*;
import javafx.scene.layout.VBox;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class Icon {

  public enum IconType {
    Location,
    Equipment
  }

  public IconType iconType; // Type of icon
  @FXML protected ImageView image; // Icon's image
  protected boolean isDrag = false; // If the icon is being dragged
  protected Floor floor; // The floor the icon is on

  public Icon() {
    this.image = new ImageView();
    image.setOnMousePressed(
        event -> {
          // ScrollPane prevention (Allows you to only drag icon)
          event.setDragDetect(false);
          Point2D offset =
              new Point2D(event.getX() - image.getX() - 15, event.getY() - image.getY() - 20);
          image.setUserData(offset);
          event.consume();
        });
    image.setOnMouseDragged(
        event -> {
          // ScrollPane prevention (Allows you to only drag icon)
          isDrag = true;
          event.setDragDetect(false);
          Point2D offset = (Point2D) image.getUserData();
          image.setX(event.getX() - offset.getX() - 15);
          image.setY(event.getY() - offset.getY() - 20);
          event.consume();
        });
  }

  /** Returns a VBox with infomation regarding the contents of the icon */
  @FXML
  public abstract VBox compileList();

  /** Sets the icon's image depending on it's contents */
  @FXML
  public abstract void setImage();
}

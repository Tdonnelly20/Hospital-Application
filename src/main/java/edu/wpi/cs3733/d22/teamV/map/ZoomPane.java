package edu.wpi.cs3733.d22.teamV.map;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

public class ZoomPane extends Pane {

  public static final double defaultDelta = 1.25d;
  public DoubleProperty scale = new SimpleDoubleProperty(1.0);
  public DoubleProperty deltaY = new SimpleDoubleProperty(0.0);
  private Timeline timeline;

  public ZoomPane() {

    this.timeline = new Timeline(30); // 60

    // add scale transform
    scaleXProperty().bind(scale);
    scaleYProperty().bind(scale);
  }

  public double getScale() {
    return scale.get();
  }

  public void setPivot(double x, double y, double scale) {
    // note: pivot value must be untransformed, i. e. without scaling
    // timeline that scales and moves the node
    timeline.getKeyFrames().clear();
    timeline
        .getKeyFrames()
        .addAll(
            new KeyFrame(
                Duration.millis(100),
                new KeyValue(translateXProperty(), getTranslateX() - x)), // 200
            new KeyFrame(
                Duration.millis(100),
                new KeyValue(translateYProperty(), getTranslateY() - y)), // 200
            new KeyFrame(Duration.millis(100), new KeyValue(this.scale, scale)) // 200
            );
    timeline.play();
  }

  public void setDeltaY(double dY) {
    deltaY.set(dY);
  }
}

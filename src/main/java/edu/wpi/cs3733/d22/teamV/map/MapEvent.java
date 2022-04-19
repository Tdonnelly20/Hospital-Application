package edu.wpi.cs3733.d22.teamV.map;

import javafx.event.EventHandler;
import javafx.scene.input.ScrollEvent;

public class MapEvent {

  ZoomPane zoomPane;

  public MapEvent(ZoomPane canvas) {
    this.zoomPane = canvas;
  }

  public EventHandler<ScrollEvent> getOnZoomEventHandler() {
    return onZoomEventHandler;
  }

  private EventHandler<ScrollEvent> onZoomEventHandler =
      new EventHandler<>() {

        @Override
        public void handle(ScrollEvent event) {

          double delta = ZoomPane.defaultDelta;

          double scale = zoomPane.getScale(); // currently we only use Y, same value is used for X
          double oldScale = scale;

          zoomPane.setDeltaY(event.getDeltaY());
          if (zoomPane.deltaY.get() < 0) {
            scale /= delta;
          } else {
            scale *= delta;
          }

          double f = (scale / oldScale) - 1;

          double dx =
              (event.getX()
                  - (zoomPane.getBoundsInParent().getWidth() / 2
                      + zoomPane.getBoundsInParent().getMinX()));
          double dy =
              (event.getY()
                  - (zoomPane.getBoundsInParent().getHeight() / 2
                      + zoomPane.getBoundsInParent().getMinY()));

          zoomPane.setPivot(f * dx, f * dy, scale);

          event.consume();
        }
      };
}

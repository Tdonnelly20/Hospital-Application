package edu.wpi.cs3733.d22.teamV.map;

import edu.wpi.cs3733.d22.teamV.controllers.PopupController;
import edu.wpi.cs3733.d22.teamV.manager.MapManager;
import edu.wpi.cs3733.d22.teamV.objects.Location;
import edu.wpi.cs3733.d22.teamV.servicerequests.ServiceRequest;
import java.util.ArrayList;
import javafx.scene.image.Image;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LocationIcon extends Icon {
  private ArrayList<ServiceRequest> requestsArr = new ArrayList<>();

  public LocationIcon(Location location) {
    super(location);
    this.iconType = "Location";
    image.setImage(new Image("locationMarker.png"));
    image.setFitWidth(30);
    image.setFitHeight(30);
    image.setTranslateX((xCoord) - 25);
    image.setTranslateY((yCoord) - 15);
    image.setOnMouseClicked(
        event -> {
          if (event.getClickCount() == 2) {
            PopupController.getController().locationForm(event, this);
          }
        });
  }

  @Override
  public void checkBounds() {}

  public void addToRequests(ServiceRequest request) {
    requestsArr.add(request);
    if (location.getRequests().contains(request)) {
      location.getRequests().add(request);
    }
    image.setImage(new Image("requestMarker.png"));
  }

  public void removeRequests(ServiceRequest request) {
    requestsArr.remove(request);
    location.getRequests().remove(request);
    if (requestsArr.size() == 0) {
      image.setImage(MapManager.getManager().locationMarker);
    }
  }

  public void changeImages() {
    if (hasActiveRequests()) {
      image.setImage(MapManager.getManager().requestMarker);
    } else {
      image.setImage(MapManager.getManager().locationMarker);
    }
  }

  public boolean hasActiveRequests() {
    for (ServiceRequest serviceRequest : requestsArr) {
      if (serviceRequest.getStatus().equals("Not Started")
          || serviceRequest.getStatus().equals("Processing")) {

        return true;
      }
    }
    return false;
  }

  public boolean hasRequestType(String type) {
    for (ServiceRequest serviceRequest : requestsArr) {
      if (serviceRequest.getType().equals(type)) {
        return true;
      }
    }
    return false;
  }

  public boolean hasActiveRequestType(String type) {
    for (ServiceRequest serviceRequest : requestsArr) {
      if ((serviceRequest.getStatus().equals("Not Started")
              || serviceRequest.getStatus().equals("Processing"))
          && serviceRequest.getType().equals(type)) {
        return true;
      }
    }
    return false;
  }
}

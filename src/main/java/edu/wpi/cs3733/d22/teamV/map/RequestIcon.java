package edu.wpi.cs3733.d22.teamV.map;

import edu.wpi.cs3733.d22.teamV.ServiceRequests.ServiceRequest;
import edu.wpi.cs3733.d22.teamV.objects.Location;
import java.util.ArrayList;
import javafx.scene.image.Image;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RequestIcon extends Icon {
  private ArrayList<ServiceRequest> requestsArr;

  public RequestIcon(Location location) {
    super(location);
    this.iconType = "Request";
    image.setImage(new Image("markedIcon.png"));
    requestsArr = new ArrayList<ServiceRequest>();
  }

  public void addToRequests(ServiceRequest request) {
    requestsArr.add(request);
    image.setImage(new Image("markedIcon.png"));
  }

  public void removeRequests(ServiceRequest request) {
    requestsArr.remove(request);
    if (requestsArr.size() == 0) {
      image.setImage(new Image("icon.png"));
    }
  }

  public void sort() {
    // TODO Sort list based on status (active first)
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

package edu.wpi.cs3733.d22.teamV.map;

import com.jfoenix.controls.JFXComboBox;
import edu.wpi.cs3733.d22.teamV.controllers.MapController;
import edu.wpi.cs3733.d22.teamV.controllers.PopupController;
import edu.wpi.cs3733.d22.teamV.main.RequestSystem;
import edu.wpi.cs3733.d22.teamV.objects.Location;
import edu.wpi.cs3733.d22.teamV.servicerequests.ServiceRequest;
import java.util.ArrayList;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LocationIcon extends Icon {
  private ArrayList<ServiceRequest> requestsArr = new ArrayList<>(); // Requests at this location
  private Location location;

  public LocationIcon(Location location) {
    super();
    this.location = location;
    this.iconType = IconType.Location;
    image.setImage(MapManager.getManager().getLocationMarker());
    image.setFitWidth(15);
    image.setFitHeight(15);
    image.setTranslateX((location.getXCoord()) - image.getFitWidth());
    image.setTranslateY((location.getYCoord()) - image.getFitHeight());
    image.setOnMouseClicked(
        event -> {
          // Opens the location form in the popup
          if (event.isShiftDown()) {
            if (event.getClickCount() == 2) {
              if (MapController.getController().getStartLocationID() == null) {
                MapController.getController().setStartLocationID(location.getNodeID());
              } else {
                MapController.getController().setEndLocationID(location.getNodeID());
                MapController.getController().drawPath();
              }
            }
          } else {
            if (event.getClickCount() == 2) {
              PopupController.getController().locationForm(event, this);
            }
          }
        });
    image.setOnMouseReleased(
        event -> {
          // If released from drag, update the xy coors
          if (isDrag) {
            isDrag = false;
            location.setXCoord(location.getXCoord() + event.getX() - 7.5);
            location.setYCoord(location.getYCoord() + event.getY() - 7.5);
            RequestSystem.getSystem().updateLocations(this);
          }
        });
  }

  @Override
  @FXML
  /** Populates a location icon's popup window with its service requests */
  public VBox compileList() {
    ObservableList<String> statusStrings =
        FXCollections.observableArrayList("Not Started", "Processing", "Done");
    if (requestsArr.size() > 0) {
      VBox vBox = new VBox();
      for (ServiceRequest request : requestsArr) {
        Label label = new Label(request.toString());
        label.setWrapText(true);
        label.setMinHeight((label.getText().lines().count() + 1) * 16);
        JFXComboBox<String> updateStatus = new JFXComboBox<>(statusStrings);
        updateStatus.setValue(request.getStatus());
        updateStatus.setPromptText(request.getStatus());
        updateStatus.setOnAction(
            event1 -> {
              RequestSystem.getSystem().removeServiceRequest(request);
              request.setStatus(updateStatus.getValue());
              RequestSystem.getSystem().addServiceRequest(request);
            });
        Button deleteRequest = new Button("Delete");
        deleteRequest.setStyle("-fx-background-color: #5C7B9F; -fx-text-fill: white;");
        deleteRequest.setOnAction(
            event -> {
              removeRequests(request);
              PopupController.getController().closePopUp();
            });
        HBox requestUpdates = new HBox(15, updateStatus, deleteRequest);
        requestUpdates.setAlignment(Pos.CENTER_LEFT);
        Accordion accordion =
            new Accordion(
                new TitledPane(
                    request.getRequestName() + ": " + request.getStatus(),
                    new VBox(15, label, requestUpdates)));
        accordion.setPrefWidth(450);
        vBox.getChildren().add(accordion);
      }
      return vBox;
    }
    return null;
  }

  /** Sets the icon image depending on the amount of requests */
  @Override
  public void setImage() {
    if (requestsArr.size() == 0) {
      image.setImage(MapManager.getManager().getLocationMarker());
    } else {
      image.setImage(MapManager.getManager().getRequestMarker());
    }
  }

  /** Adds a request to requestArr and updates the image */
  public void addToRequests(ServiceRequest request) {
    requestsArr.add(request);
    if (location.getRequests().contains(request)) {
      location.getRequests().add(request);
    }
    RequestSystem.getSystem().addServiceRequest(request);
    setImage();
  }

  /** Removes a request to requestArr and updates the image */
  public void removeRequests(ServiceRequest request) {
    requestsArr.remove(request);
    location.getRequests().remove(request);
    RequestSystem.getSystem().removeServiceRequest(request);
    setImage();
  }

  /** Returns true if a request in requestArr is active, otherwise return false */
  public boolean hasActiveRequests() {
    for (ServiceRequest serviceRequest : requestsArr) {
      if (serviceRequest.getStatus() != null) {
        if (serviceRequest.getStatus().equals("Not Started")
            || serviceRequest.getStatus().equals("Processing")) {
          return true;
        }
      }
    }
    return false;
  }

  /** Returns true if icon has a request with the given type */
  public boolean hasRequestType(String type) {
    for (ServiceRequest serviceRequest : requestsArr) {
      if (serviceRequest.getType().equals(type)) {
        return true;
      }
    }
    return false;
  }

  /** Returns true if icon has an active request with the given type */
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

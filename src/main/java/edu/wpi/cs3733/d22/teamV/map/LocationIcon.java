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
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
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
    if (location.getNodeType().equalsIgnoreCase("node")) {
      image.setOpacity(0);
      image.setImage(MapManager.getManager().nodeMarker);
      image.setOnMouseClicked(
          event -> {
            // Opens the location form in the popup
            if (event.getClickCount() == 2) {
              if (event.isShiftDown() || event.isAltDown() || event.isControlDown()) {
                setPathfinder(event);
              } else {
                PopupController.getController().iconWindow(event);
              }
            }
          });
    } else {
      image.setOpacity(100);
      image.setImage(MapManager.getManager().getLocationMarker());
      image.setOnMouseClicked(
          event -> {
            // Opens the location form in the popup
            if (event.getClickCount() == 2) {
              if (event.isShiftDown() || event.isAltDown() || event.isControlDown()) {
                setPathfinder(event);
              } else {
                PopupController.getController().locationForm(event, this);
              }
            }
          });
    }
    image.setFitWidth(15);
    image.setFitHeight(15);
    image.setTranslateX((location.getXCoord()) - image.getFitWidth());
    image.setTranslateY((location.getYCoord()) - image.getFitHeight());
    image.setOnMouseReleased(
        event -> {
          // If released from drag, update the xy coors
          if (isDrag) {
            isDrag = false;
            Point2D offset = (Point2D) image.getUserData();
            location.setXCoord(location.getXCoord() + event.getX() - offset.getX() - 15);
            location.setYCoord(location.getYCoord() + event.getY() - offset.getY() - 20);
            RequestSystem.getSystem().updateLocations(this);
          }
        });
  }

  /**
   * Sets the MapController's startLocationID and endLocationID and calls makePaths() if the Alt key
   * is down or drawPaths() if the Shift key is down
   */
  @FXML
  private void setPathfinder(MouseEvent event) {
    if (MapController.getController().getStartLocationID().isEmpty()
        || !MapController.getController().getEndLocationID().isEmpty()) {
      MapController.getController().setStartLocationID(location.getNodeID());
      MapController.getController().setEndLocationID("");
      MapController.getController()
          .getStartLocationLabel()
          .setText("Starting Location: " + location.getNodeID());
      MapController.getController().getEndLocationLabel().setText("End Location: ");
    } else if (MapController.getController().getEndLocationID().isEmpty()) {
      MapController.getController().setEndLocationID(location.getNodeID());
      MapController.getController()
          .getEndLocationLabel()
          .setText("End Location: " + location.getNodeID());
      // Call relevant functions
      if (event.isShiftDown() && !event.isAltDown() && !event.isControlDown()) {
        MapController.getController().drawPath();
      } else if (event.isAltDown() && !event.isShiftDown() && !event.isControlDown()) {
        MapController.getController().makePath();
      } else if (event.isControlDown() && !event.isShiftDown() && !event.isAltDown()) {
        removeLink();
      }
    }
  }

  /** Removes link between 2 nodes */
  @FXML
  private void removeLink() {
    if (!MapController.getController().getStartLocationID().equals(location.getNodeID())) {
      RequestSystem.getSystem()
          .getPathfinderDao()
          .removeLink(location.getNodeID(), MapController.getController().getStartLocationID());
    } else {
      RequestSystem.getSystem()
          .getPathfinderDao()
          .removeLink(location.getNodeID(), MapController.getController().getEndLocationID());
    }
    MapController.getController().refreshMap();
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
    if (!location.getNodeType().equalsIgnoreCase("node")) {
      if (requestsArr.size() == 0) {
        image.setImage(MapManager.getManager().getLocationMarker());
      } else {
        image.setImage(MapManager.getManager().getRequestMarker());
      }
    }
  }

  /** Adds a request to requestArr and updates the image */
  public void addToRequests(ServiceRequest request) {
    if (!location.getNodeType().equalsIgnoreCase("node")) {
      requestsArr.add(request);
      if (!location.getRequests().contains(request)) {
        location.getRequests().add(request);
      }
      RequestSystem.getSystem().addServiceRequest(request);
      setImage();
    }
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

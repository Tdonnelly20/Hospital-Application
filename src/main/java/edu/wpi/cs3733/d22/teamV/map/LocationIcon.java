package edu.wpi.cs3733.d22.teamV.map;

import com.jfoenix.controls.JFXComboBox;
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
  private ArrayList<ServiceRequest> requestsArr = new ArrayList<>();

  public LocationIcon(Location location) {
    super(location);
    this.iconType = IconType.Location;
    image.setImage(MapManager.getManager().getLocationMarker());
    image.setFitWidth(15);
    image.setFitHeight(15);
    image.setTranslateX((xCoord) - 25);
    image.setTranslateY((yCoord) - 15);
    image.setOnMouseClicked(
        event -> {
          if (event.getClickCount() == 2) {
            PopupController.getController().locationForm(event, this);
            // System.out.println("Clicks received");
            // MapController.getController().locationForm(event, this);
          }
        });
    image.setOnMouseReleased(
        event -> {
          if (isDrag) {
            isDrag = false;
            xCoord += event.getX();
            yCoord += event.getY();
            RequestSystem.getSystem().updateLocations(this);
          }
        });
  }

  @Override
  @FXML
  /** Populates a location icon's popup window with its service requests */
  public ScrollPane compileList() {
    ObservableList<String> statusStrings =
        FXCollections.observableArrayList("Not Started", "Processing", "Done");
    if (requestsArr.size() > 0) {
      VBox vBox = new VBox();
      ScrollPane scrollPane = new ScrollPane(vBox);
      scrollPane.setFitToHeight(true);
      scrollPane.setPannable(false);
      scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
      vBox.setPrefWidth(450);
      vBox.setPrefHeight(400);
      for (ServiceRequest request : requestsArr) {
        System.out.println(request.toString());
        Label idLabel = new Label("Employee: " + request.getEmployee().getEmployeeID());
        Label locationLabel = new Label("X: " + xCoord + " Y: " + yCoord);

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
                    new VBox(15, idLabel, locationLabel, requestUpdates)));
        accordion.setPrefWidth(450);
        vBox.getChildren().add(accordion);
      }
      return scrollPane;
    }
    return null;
  }

  @Override
  public void setImage() {
    if (requestsArr.size() == 0) {
      image.setImage(MapManager.getManager().getLocationMarker());
    } else {
      image.setImage(MapManager.getManager().getRequestMarker());
    }
  }

  public void addToRequests(ServiceRequest request) {
    requestsArr.add(request);
    if (location.getRequests().contains(request)) {
      location.getRequests().add(request);
    }
    RequestSystem.getSystem().addServiceRequest(request);
    setImage();
  }

  public void removeRequests(ServiceRequest request) {
    requestsArr.remove(request);
    location.getRequests().remove(request);
    RequestSystem.getSystem().removeServiceRequest(request);
    setImage();
  }

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

  public void updateLocation() {
    for (ServiceRequest request : requestsArr) {
      request.setLocation(location);
      RequestSystem.getSystem().getLocation(location.getNodeID()).setXCoord(location.getXCoord());
      RequestSystem.getSystem().getLocation(location.getNodeID()).setYCoord(location.getYCoord());
    }
  }
}

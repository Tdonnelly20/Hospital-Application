package edu.wpi.cs3733.d22.teamV.map;

import com.jfoenix.controls.JFXComboBox;
import edu.wpi.cs3733.d22.teamV.controllers.PopupController;
import edu.wpi.cs3733.d22.teamV.manager.MapManager;
import edu.wpi.cs3733.d22.teamV.objects.Location;
import edu.wpi.cs3733.d22.teamV.servicerequests.ServiceRequest;
import java.util.ArrayList;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
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
    image.setImage(new Image("locationMarker.png"));
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
        updateStatus.setOnAction(event1 -> request.setStatus(updateStatus.getValue()));
        Button deleteRequest = new Button("Delete");
        deleteRequest.setOnAction(event -> removeRequests(request));

        Accordion accordion =
            new Accordion(
                new TitledPane(
                    request.getRequestName() + ": " + request.getStatus(),
                    new VBox(15, idLabel, locationLabel, updateStatus)));
        accordion.setPrefWidth(450);
        vBox.getChildren().add(accordion);
      }
      return scrollPane;
    }
    return null;
  }

  public void addToRequests(ServiceRequest request) {
    requestsArr.add(request);
    if (location.getRequests().contains(request)) {
      location.getRequests().add(request);
    }
    image.setImage(MapManager.getManager().requestMarker);
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
}

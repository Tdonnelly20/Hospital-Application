package edu.wpi.cs3733.d22.teamV.map;

import com.jfoenix.controls.JFXComboBox;
import edu.wpi.cs3733.d22.teamV.controllers.MapController;
import edu.wpi.cs3733.d22.teamV.controllers.MapDashboardController;
import edu.wpi.cs3733.d22.teamV.controllers.PopupController;
import edu.wpi.cs3733.d22.teamV.main.RequestSystem;
import edu.wpi.cs3733.d22.teamV.manager.MapManager;
import edu.wpi.cs3733.d22.teamV.objects.Equipment;
import edu.wpi.cs3733.d22.teamV.objects.Location;
import java.util.ArrayList;

import edu.wpi.cs3733.d22.teamV.servicerequests.EquipmentDelivery;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EquipmentIcon extends Icon {

  ArrayList<Equipment> equipmentList;
  int dirtyBeds = 0;

  public EquipmentIcon(Location location) {
    super(location);
    this.iconType = IconType.Equipment;
    equipmentList = new ArrayList<>();
    image.setFitWidth(20);
    image.setFitHeight(20);
    image.setTranslateX((xCoord) - 25);
    image.setTranslateY((yCoord) - 15);
    image.setOnMouseClicked(
        event -> {
          if (event.getClickCount() == 2) {
            PopupController.getController().equipmentForm(event, this);
            // MapController.getController().ewuipmentForm(event, this);
          }
        });
    image.setOnMouseReleased(
        event -> {
          location.setXCoord(location.getXCoord() + event.getX());
          location.setYCoord(location.getYCoord() + event.getY());
          updateLocation();
          checkBounds();
        });
  }

  @Override
  public ScrollPane compileList() {
    if (equipmentList.size() > 0) {
      ObservableList<String> statusStrings = FXCollections.observableArrayList("Clean", "Dirty");
      VBox vBox = new VBox();
      ScrollPane scrollPane = new ScrollPane(vBox);
      scrollPane.setFitToHeight(true);
      scrollPane.setPannable(false);
      scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
      vBox.setPrefWidth(450);
      vBox.setPrefHeight(400);
      for (Equipment equipment : equipmentList) {
        Label idLabel = new Label("ID: " + equipment.getID());
        Button deleteEquipment = new Button("Delete");
        deleteEquipment.setOnAction(
            event -> {
              removeEquipment(equipment);
              if (getEquipmentList().size() == 0) {
                MapController.getController().removeIconForm(this);
              }
            });
        Label locationLabel = new Label("X: " + xCoord + " Y: " + yCoord);

        JFXComboBox<String> updateStatus = new JFXComboBox<>(statusStrings);
        updateStatus.setPromptText(equipment.getIsDirtyString());
        updateStatus.setValue(equipment.getIsDirtyString());
        updateStatus.setOnAction(
            event1 -> equipment.setIsDirty(updateStatus.getValue().equals("Dirty")));
        HBox hbox = new HBox(15, updateStatus, deleteEquipment);
        Accordion accordion =
            new Accordion(
                new TitledPane(
                    equipment.getName()
                        + " ("
                        + equipment.getIsDirtyString()
                        + "): "
                        + equipment.getDescription(),
                    new VBox(15, idLabel, locationLabel, hbox)));
        accordion.setPrefWidth(450);
        vBox.getChildren().add(accordion);
      }
      return scrollPane;
    }
    return null;
  }

  public void addToEquipmentList(Equipment equipment) {
    if (equipment.getIsDirty()) {
      equipmentList.add(equipment);
      if (equipmentList.size() == 1) {
        image.setImage(MapManager.getManager().dirtyEquipment);
      } else {
        setImage();
      }
    } else {
      image.setImage(MapManager.getManager().cleanEquipment);
      equipmentList.add(0, equipment);
    }
    alertSixBeds(equipment, true);
    MapDashboardController.getController().updateCounts();
  }

  public void removeEquipment(Equipment equipment) {
    // RequestSystem.getSystem().deleteEquipment(equipment);
    equipmentList.remove(equipment);
    if (equipmentList.size() == 0) {
      MapController.getController().deleteIcon(this);
    }
    alertSixBeds(equipment, false);
  }

  public void setImage() {
    if (hasCleanEquipment()) {
      image.setImage(MapManager.getManager().cleanEquipment);
    } else {
      image.setImage(MapManager.getManager().dirtyEquipment);
    }
  }

  public boolean hasCleanEquipment() {
    for (Equipment equipment : equipmentList) {
      if (!equipment.getIsDirty()) {
        return true;
      }
    }
    return false;
  }

  public void checkBounds() {
    if (MapController.getController().getCurrFloor().getEquipmentIcons().size() > 0) {
      for (EquipmentIcon icon : MapController.getController().getCurrFloor().getEquipmentIcons()) {
        if (icon != this && iconType.equals(IconType.Equipment)) {
          if (icon.getImage().getBoundsInParent().intersects(this.image.getBoundsInParent())) {
            System.out.println("Intersection");
            equipmentList.addAll(icon.getEquipmentList());
            updateLocation();
            icon.getEquipmentList().clear();
            MapController.getController().deleteIcon(icon);
            setImage();
          }
        }
      }
    }
  }

  public void updateLocation() {
    for (Equipment equipment : equipmentList) {
      equipment.updateLocation(location.getXCoord(), location.getYCoord());
    }
  }

  //checks if isAdding is true, if so finds beds that are dirty in the same place.
//when counter > 5, dirtyBeds increases by 1 and RequestSystem is called (EquipmentDelivery).
//else, dirtyBeds decreases by 1.
  public void alertSixBeds(Equipment e, boolean isAdding) {
    if (isAdding) {
      if (e.getIsDirty() && e.getName() == "Bed") {
        dirtyBeds += 1;
      }
      if (dirtyBeds > 5) {
        EquipmentDelivery request = new EquipmentDelivery(-1, -1, "OR", e.getID(), e.getID(), 1, "Not Started");
        RequestSystem.getSystem().addServiceRequest(request, RequestSystem.Dao.EquipmentDelivery);
      }
    }
    else { dirtyBeds--; }
  }


}

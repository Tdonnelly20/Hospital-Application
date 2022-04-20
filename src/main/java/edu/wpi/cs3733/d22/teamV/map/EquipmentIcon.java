package edu.wpi.cs3733.d22.teamV.map;

import com.jfoenix.controls.JFXComboBox;
import edu.wpi.cs3733.d22.teamV.controllers.MapController;
import edu.wpi.cs3733.d22.teamV.controllers.MapDashboardController;
import edu.wpi.cs3733.d22.teamV.controllers.PopupController;
import edu.wpi.cs3733.d22.teamV.manager.MapManager;
import edu.wpi.cs3733.d22.teamV.objects.Equipment;
import edu.wpi.cs3733.d22.teamV.objects.Location;
import java.util.ArrayList;
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
    MapDashboardController.getController().updateCounts();
    alertSixBeds();
  }

  public void removeEquipment(Equipment equipment) {
    // RequestSystem.getSystem().deleteEquipment(equipment);
    equipmentList.remove(equipment);
    if (equipmentList.size() == 0) {
      MapController.getController().deleteIcon(this);
    }
    alertSixBeds();
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

  public void alertSixBeds() {
    int alertCounter = 1;
    boolean alert = false;
    ArrayList<String> dirtyBeds = new ArrayList<String>();

    for (Equipment equipment : equipmentList) {
      double one = equipment.getX();
      for (Equipment equipmentTwo : equipmentList) {
        double two = equipment.getX();
        if (one == two) {
          boolean d1 = equipment.getIsDirty();
          boolean d2 = equipment.getIsDirty();
          int i =
              MapDashboardController.getController()
                  .checkAlertSixBeds(equipment.getName(), d1, equipmentTwo.getName(), d2);
          dirtyBeds.add(String.valueOf(equipment.getX()));
          alertCounter += i;
          dirtyBeds.add(String.valueOf(d1));
          dirtyBeds.add(String.valueOf(d2));
        }
      }
    }

    // this deletes any duplicate locations
    for (int x = 0; x < dirtyBeds.size(); x++) {
      for (int y = 0; x < dirtyBeds.size(); y++) {
        if (dirtyBeds.get(x) == dirtyBeds.get(y) && x != y) {
          dirtyBeds.remove(x);
        }
      }
    }

    if (alertCounter > 5) {
      alert = true;
    }

    MapDashboardController.getController().addBedAlertToArray(alert, dirtyBeds);
  }

  public int[] pumpAlert() {
    int dirty = 0;
    int clean = 0;
    for (Equipment equipment : equipmentList) {
      // System.out.println(equipment.getName());
      if (equipment.getName().equals("Infusion Pump")) {
        if (equipment.getIsDirty()) dirty++;
        else clean++;
      }
    }
    return new int[] {clean, dirty};
  }
}

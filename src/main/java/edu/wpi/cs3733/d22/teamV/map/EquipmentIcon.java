package edu.wpi.cs3733.d22.teamV.map;

import com.jfoenix.controls.JFXComboBox;
import edu.wpi.cs3733.d22.teamV.controllers.MapController;
import edu.wpi.cs3733.d22.teamV.controllers.MapDashboardController;
import edu.wpi.cs3733.d22.teamV.controllers.PopupController;
import edu.wpi.cs3733.d22.teamV.main.RequestSystem;
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
          if (isDrag) {
            isDrag = false;
            xCoord += event.getX();
            yCoord += event.getY();
            RequestSystem.getSystem().updateLocations(this);
            checkBounds();
            MapManager.getManager().setUpFloors();
          }
          // MapController.getController().setFloor(getLocation().getFloor());
        });
  }

  @Override
  public VBox compileList() {
    if (equipmentList.size() > 0) {
      ObservableList<String> statusStrings = FXCollections.observableArrayList("Clean", "Dirty");
      VBox vBox = new VBox();
      for (Equipment equipment : equipmentList) {
        Label idLabel = new Label("ID: " + equipment.getID());
        Button deleteEquipment = new Button("Delete");
        deleteEquipment.setStyle("-fx-background-color: #5C7B9F; -fx-text-fill: white;");
        deleteEquipment.setOnAction(
            event -> {
              removeEquipment(equipment);
              if (getEquipmentList().size() == 0) {
                RequestSystem.getSystem().removeEquipment(equipment);
                MapController.getController().populateFloorIconArr();
              }
            });
        Label locationLabel = new Label("X: " + xCoord + " Y: " + yCoord);

        JFXComboBox<String> updateStatus = new JFXComboBox<>(statusStrings);
        updateStatus.setPromptText(equipment.getIsDirtyString());
        updateStatus.setValue(equipment.getIsDirtyString());
        updateStatus.setOnAction(
            event1 -> {
              equipment.setIsDirty(updateStatus.getValue().equals("Dirty"));
              RequestSystem.getSystem()
                  .getEquipmentDao()
                  .updateEquipment(equipment, equipment.getID());
            });
        HBox hbox = new HBox(15, updateStatus, deleteEquipment);
        Label description = new Label("Description: " + equipment.getDescription());
        Accordion accordion =
            new Accordion(
                new TitledPane(
                    equipment.getName()
                        + " ("
                        + equipment.getIsDirtyString()
                        + "): "
                        + equipment.getID(),
                    new VBox(15, idLabel, description, hbox)));

        // accordion.width
        vBox.getChildren().add(accordion);
      }
      return vBox;
    }
    return null;
  }

  public void addToEquipmentList(Equipment equipment) {
    if (equipment.getIsDirty()) {
      equipmentList.add(equipment);
      setImage();
    } else {
      equipmentList.add(0, equipment);
    }
    setImage();
    // RequestSystem.getSystem().addEquipment(equipment);
    MapDashboardController.getController().updateCounts();
    alertSixBeds();
  }

  public void removeEquipment(Equipment equipment) {
    equipmentList.remove(equipment);
    RequestSystem.getSystem().removeEquipment(equipment);
    alertSixBeds();
    PopupController.getController().closePopUp();
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
    if (MapManager.getManager().getFloor(this.getLocation().getFloor()).getEquipmentIcons().size()
        > 0) {
      for (EquipmentIcon icon :
          MapManager.getManager().getFloor(location.getFloor()).getEquipmentIcons()) {
        if (icon != this && iconType.equals(IconType.Equipment)) {
          if (icon.getImage().getBoundsInParent().intersects(this.image.getBoundsInParent())) {
            System.out.println("Intersection");
            ArrayList<Equipment> tempEquipmentList = new ArrayList<>(icon.getEquipmentList());
            tempEquipmentList.addAll(equipmentList);
            equipmentList.clear();
            equipmentList.addAll(tempEquipmentList);
            RequestSystem.getSystem().updateLocations(this);
            setImage();
          }
        }
      }
    }
  }

  public void alertSixBeds() {

    int alertCounter = 0;
    boolean alert = false;

    ArrayList<Equipment> equip = new ArrayList<Equipment>();
    equip = this.getEquipmentList();
    ArrayList<String> dirtyBedsFloor = new ArrayList<String>();

    for (int i = 0; equip.size() > i; i++) {
      if (equip.get(i).getName().equals("Bed") && equip.get(i).getIsDirty()) {
        dirtyBedsFloor.add(String.valueOf(equip.get(i).getFloor()));
        alertCounter = +1;
      }
    }
    if (alertCounter > 5) {
      alert = true;
    }

    MapDashboardController.getController().addBedAlertToArray(alert, dirtyBedsFloor);
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

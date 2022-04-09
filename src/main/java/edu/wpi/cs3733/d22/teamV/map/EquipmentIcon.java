package edu.wpi.cs3733.d22.teamV.map;

import edu.wpi.cs3733.d22.teamV.manager.MapManager;
import edu.wpi.cs3733.d22.teamV.objects.Equipment;
import edu.wpi.cs3733.d22.teamV.objects.Location;
import java.util.ArrayList;
import javafx.scene.image.Image;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EquipmentIcon extends Icon {
  ArrayList<Equipment> equipmentList = new ArrayList<>();

  public EquipmentIcon(Location location) {
    super(location);
    this.iconType = "Equipment";
    image.setFitWidth(30);
    image.setFitHeight(30);
    image.setTranslateX((xCoord) - 25);
    image.setTranslateY((yCoord) - 15);
    image.setOnMouseClicked(
        event -> {
          MapManager.getManager().openIconRequestWindow(this);
        });
  }

  public void addToEquipmentList(Equipment equipment) {
    equipmentList.add(equipment);
    setImage();
  }

  public void removeEquipment(Equipment equipment) {
    equipmentList.remove(equipment);
    if (equipmentList.size() == 0) {
      setImage();
    }
  }

  public void setImage() {
    if (hasCleanEquipment()) {
      image.setImage(new Image("cleanEquipment.png"));
    } else {
      image.setImage(new Image("Equipment.png"));
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
}

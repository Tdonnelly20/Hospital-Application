package edu.wpi.veganvampires.interfaces;

import edu.wpi.veganvampires.objects.EquipmentDelivery;
import java.util.List;

public interface EquipmentDeliveryImpl {

  List<EquipmentDelivery> getAllEquipmentDeliveries();

  void addEquipmentDelivery(String location, String equipment, String notes, int quantity);

  void removeEquipmentDelivery(String equipment);

  void removeEquipmentDelivery() // TODO
      ;
}

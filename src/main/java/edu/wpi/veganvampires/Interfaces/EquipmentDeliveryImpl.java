package edu.wpi.veganvampires.Interfaces;

import edu.wpi.veganvampires.EquipmentDelivery;
import java.util.List;
 
public interface EquipmentDeliveryImpl {

  List<EquipmentDelivery> getAllEquipmentDeliveries();

  void addEquipmentDelivery(String location, String equipment, String notes, int quantity);

  void removeEquipmentDelivery(String equipment);
}

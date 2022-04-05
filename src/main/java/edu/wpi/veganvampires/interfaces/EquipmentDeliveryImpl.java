package edu.wpi.veganvampires.interfaces;

import edu.wpi.veganvampires.objects.EquipmentDelivery;
import java.sql.SQLException;
import java.util.ArrayList;

public interface EquipmentDeliveryImpl {

  ArrayList<EquipmentDelivery> getAllEquipmentDeliveries();

  void addEquipmentDelivery(
      String location, int userID, String equipment, String notes, int quantity, String status)
      throws SQLException;

  void removeEquipmentDelivery(String equipment) throws SQLException;
}

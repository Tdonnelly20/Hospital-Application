package edu.wpi.veganvampires.interfaces;

import edu.wpi.veganvampires.objects.EquipmentDelivery;
import java.sql.SQLException;
import java.util.ArrayList;

public interface EquipmentDeliveryDAO {

  ArrayList<EquipmentDelivery> getAllEquipmentDeliveries();

  void addEquipmentDelivery(String location, String equipment, String notes, int quantity)
      throws SQLException;

  void removeEquipmentDelivery(String equipment) throws SQLException;
}

package edu.wpi.cs3733.d22.teamV.interfaces;

import edu.wpi.cs3733.d22.teamV.ServiceRequests.EquipmentDelivery;
import java.sql.SQLException;
import java.util.ArrayList;

public interface EquipmentDeliveryImpl {

  ArrayList<EquipmentDelivery> getAllEquipmentDeliveries();

  void addEquipmentDelivery(
      int userID,
      String nodeID,
      String equipment,
      String notes,
      int quantity,
      String status,
      int pID,
      String fname,
      String lname)
      throws SQLException;

  void removeEquipmentDelivery(String equipment) throws SQLException;
}

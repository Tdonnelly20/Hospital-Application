package edu.wpi.cs3733.d22.teamV.interfaces;

import edu.wpi.cs3733.d22.teamV.objects.Equipment;
import java.sql.SQLException;
import java.util.ArrayList;

public interface EquipmentImpl {
  // ID	Name	X	Y	Description	isDirty
  ArrayList<Equipment> getAllEquipment();

  void addEquipment(
      String ID, String name, String floor, int x, int y, String desc, Boolean isDirty)
      throws SQLException;

  // void removeEquipment(String equipment) throws SQLException;
}

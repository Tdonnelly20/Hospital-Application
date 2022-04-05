package edu.wpi.veganvampires.interfaces;

import edu.wpi.veganvampires.objects.Equipment;

import java.sql.SQLException;
import java.util.ArrayList;

public interface EquipmentImpl {
//ID	Name	X	Y	Description	isDirty
  ArrayList<Equipment> getAllEquipment();

  void addEquipment(int ID, String name, String floor,int x, int y,String desc, Boolean isDirty)
      throws SQLException;

  //void removeEquipment(String equipment) throws SQLException;
}

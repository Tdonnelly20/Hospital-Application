package edu.wpi.veganvampires.dao;

import edu.wpi.veganvampires.interfaces.EquipmentImpl;
import edu.wpi.veganvampires.objects.Equipment;
import java.sql.SQLException;
import java.util.ArrayList;

public class EquipmentDao implements EquipmentImpl {

  private static ArrayList<Equipment> allEquipment;

  /** Initialize the array list */
  public EquipmentDao() {
    allEquipment = new ArrayList<Equipment>();
  }

  public EquipmentDao(ArrayList<Equipment> allEquipment) {
    this.allEquipment = allEquipment;
  }

  public void setAllEquipment(ArrayList<Equipment> equipmentArrayList) {
    allEquipment = equipmentArrayList;
  }

  /**
   * Getter
   *
   * @return
   */
  @Override
  public ArrayList<Equipment> getAllEquipment() {
    return allEquipment;
  }

  /**
   * Adds equipment to the CSV
   *
   * @param ID
   * @param name
   * @param floor
   * @param x
   * @param y
   * @param desc
   * @param isDirty
   * @throws SQLException
   */
  @Override
  public void addEquipment(
      String ID, String name, String floor, int x, int y, String desc, Boolean isDirty)
      throws SQLException {
    Equipment newEquipment = new Equipment(ID, name, floor, x, y, desc, isDirty);

    System.out.println("Adding to local arraylist...");
    allEquipment.add(newEquipment);

    System.out.println("Adding to database");
  }

  /**
   * TODO: Make sure that this doesn't remove someone else's equipment Remove the equipment by
   * finding the string of the equipment and using is to remove it from the arraylist
   *
   * @param equipment a string of the desired equipment to remove
   * @throws SQLException
   */
  /*
  @Override
  public void removeEquipmentString equipment) throws SQLException {

    System.out.println("Removing from arraylist...");
    allEquipmentDeliveries.removeIf(e -> e.getEquipment().equals(equipment));

    try {
      System.out.println("Removing from database...");
      Connection connection;
      connection = DriverManager.getConnection("jdbc:derby:VDB;create=true", "admin", "admin");
      Statement exampleStatement = connection.createStatement();
      for (Equipment e : allEquipmentDeliveries)
        exampleStatement.execute("DELETE FROM LOCATIONS WHERE equipment.equals(e.getEquipment())");

      Vdb.saveToFile(Vdb.Database.Equipment);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
  */
}

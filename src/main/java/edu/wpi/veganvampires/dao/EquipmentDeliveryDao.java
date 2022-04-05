package edu.wpi.veganvampires.dao;

import edu.wpi.veganvampires.interfaces.EquipmentDeliveryImpl;
import edu.wpi.veganvampires.main.Vdb;
import edu.wpi.veganvampires.objects.EquipmentDelivery;
import java.sql.*;
import java.util.ArrayList;

public class EquipmentDeliveryDao implements EquipmentDeliveryImpl {

  private static ArrayList<EquipmentDelivery> allEquipmentDeliveries;

  /** Initialize the array list */
  public EquipmentDeliveryDao() {
    allEquipmentDeliveries = new ArrayList<EquipmentDelivery>();
  }

  public EquipmentDeliveryDao(ArrayList<EquipmentDelivery> allEquipmentDeliveries) {
    this.allEquipmentDeliveries = allEquipmentDeliveries;
  }

  public void setAllEquipmentDeliveries(ArrayList<EquipmentDelivery> equipmentDeliveryArrayList) {
    allEquipmentDeliveries = equipmentDeliveryArrayList;
  }

  /**
   * Getter
   *
   * @return
   */
  @Override
  public ArrayList<EquipmentDelivery> getAllEquipmentDeliveries() {
    return allEquipmentDeliveries;
  }

  /**
   * Adds equipment to the CSV
   *
   * @param location
   * @param equipment
   * @param notes
   * @param quantity
   * @throws SQLException
   */
  @Override
  public void addEquipmentDelivery(String location, String equipment, String notes, int quantity)
      throws SQLException {
    EquipmentDelivery newEquipmentDelivery =
        new EquipmentDelivery(location, 123, equipment, notes, quantity, "no");

    System.out.println("Adding to local arraylist...");
    allEquipmentDeliveries.add(newEquipmentDelivery);
    try {
      Vdb.saveToFile(Vdb.Database.EquipmentDelivery);
    } catch (Exception e) {

    }

    System.out.println("Adding to database");
    Connection connection = Vdb.Connect();
    PreparedStatement pSTMT =
        connection.prepareStatement("INSERT INTO EQUIPMENT VALUES (?, ?, ?, ?)");
    pSTMT.setString(1, newEquipmentDelivery.getLocation());
    pSTMT.setString(2, newEquipmentDelivery.getEquipment());
    pSTMT.setString(3, newEquipmentDelivery.getNotes());
    pSTMT.setInt(4, newEquipmentDelivery.getQuantity());
    pSTMT.executeUpdate();
  }

  /**
   * TODO: Make sure that this doesn't remove someone else's equipment Remove the equipment by
   * finding the string of the equipment and using is to remove it from the arraylist
   *
   * @param equipment a string of the desired equipment to remove
   * @throws SQLException
   */
  @Override
  public void removeEquipmentDelivery(String equipment) throws SQLException {

    System.out.println("Removing from arraylist...");
    allEquipmentDeliveries.removeIf(e -> e.getEquipment().equals(equipment));

    try {
      System.out.println("Removing from database...");
      Connection connection;
      connection = DriverManager.getConnection("jdbc:derby:VDB;create=true", "admin", "admin");
      PreparedStatement pSTMT =
          connection.prepareStatement("DELETE FROM EQUIPMENTDELIVERY WHERE equipment.equals (?)");
      pSTMT.setString(1, equipment);
      pSTMT.executeUpdate();
      Vdb.saveToFile(Vdb.Database.EquipmentDelivery);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}

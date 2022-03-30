package edu.wpi.veganvampires.Dao;

import edu.wpi.veganvampires.Features.EquipmentDelivery;
import edu.wpi.veganvampires.Interfaces.EquipmentDeliveryDAO;
import java.sql.*;
import java.util.ArrayList;

public class EquipmentDeliveryDAOImpl implements EquipmentDeliveryDAO {
  private static ArrayList<EquipmentDelivery> allEquipmentDeliveries;

  /** Initialize the array list */
  public EquipmentDeliveryDAOImpl() {
    /*allEquipmentDeliveries = new ArrayList<EquipmentDelivery>();
    try {
      Vdb.CreateDB();
      allEquipmentDeliveries = Vdb.equipment;
    } catch (Exception e) {
      e.printStackTrace();
    }*/
  }

  @Override
  public ArrayList<EquipmentDelivery> getAllEquipmentDeliveries() {
    return allEquipmentDeliveries;
  }

  @Override
  public void addEquipmentDelivery(String location, String equipment, String notes, int quantity)
      throws SQLException {
    EquipmentDelivery newEquipmentDelivery =
        new EquipmentDelivery(location, equipment, notes, quantity);

    System.out.println("Adding to local arraylist...");
    allEquipmentDeliveries.add(newEquipmentDelivery);
    updateEquipmentDeliveryDB(newEquipmentDelivery);
  }

  private void updateEquipmentDeliveryDB(String equipment) throws SQLException {
    try {
      System.out.println("Sending to database...");
      Connection connection;
      connection = DriverManager.getConnection("jdbc:derby:VDB;create=true", "admin", "admin");
      Statement exampleStatement = connection.createStatement();
      for (EquipmentDelivery e : allEquipmentDeliveries)
        exampleStatement.execute("DELETE FROM LOCATIONS WHERE equipment.equals(e.getEquipment())");
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  private void updateEquipmentDeliveryDB(EquipmentDelivery newEquipmentDelivery)
      throws SQLException {
    try {
      System.out.println("Sending to database...");
      Connection connection;
      connection = DriverManager.getConnection("jdbc:derby:VDB;create=true", "admin", "admin");
      Statement exampleStatement = connection.createStatement();
      exampleStatement.execute(
          "INSERT INTO LOCATIONS VALUES (newEquipmentDelivery.getEquipment(), newEquipmentDelivery.getNotes(), newEquipmentDelivery.getLocation(), newEqipmentDelivery.getQuantity()) ");
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  @Override
  public void removeEquipmentDelivery(String equipment) throws SQLException {
    allEquipmentDeliveries.removeIf(e -> e.getEquipment().equals(equipment));
    updateEquipmentDeliveryDB(equipment);
  }
}

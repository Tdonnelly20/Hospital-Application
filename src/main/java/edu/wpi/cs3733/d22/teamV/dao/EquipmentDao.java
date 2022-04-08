package edu.wpi.cs3733.d22.teamV.dao;

import edu.wpi.cs3733.d22.teamV.main.Vdb;
import edu.wpi.cs3733.d22.teamV.objects.Equipment;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;

public class EquipmentDao {

  private static ArrayList<Equipment> allEquipment;

  /** Initialize the array list */
  public EquipmentDao() {
    allEquipment = new ArrayList<Equipment>();
    try {
      loadFromCSV();
      createSQLTable();
    } catch (Exception e) {
      e.printStackTrace();
    }
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
  public void addEquipment(
      String ID, String name, String floor, int x, int y, String desc, Boolean isDirty)
      throws SQLException {
    Equipment newEquipment = new Equipment(ID, name, floor, x, y, desc, isDirty);

    allEquipment.add(newEquipment);
  }

  public void loadFromCSV() throws IOException {
    String line = "";
    FileReader fr = new FileReader(Vdb.currentPath + "\\ListofEquipment.CSV");
    BufferedReader br = new BufferedReader(fr);
    String headerLine = br.readLine();
    String splitToken = ",";
    ArrayList<Equipment> equipmentList = new ArrayList<>();
    // int ID, String name, floor,double x, double y, String description, Boolean isDirty) {
    while ((line = br.readLine()) != null) // should create a database based on csv file
    {
      String[] data;
      data = line.split(splitToken);
      Equipment equipment =
          new Equipment(
              data[0],
              data[1],
              data[2],
              Double.parseDouble(data[3]),
              Double.parseDouble(data[4]),
              data[5],
              Boolean.parseBoolean(data[6]));
      equipmentList.add(equipment);
    }

    allEquipment = equipmentList;
  }

  public int getTotalEquipment() {
    return allEquipment.size();
  }

  public ArrayList<Equipment> getAllEquipment() {
    return allEquipment;
  }

  public void createSQLTable() throws SQLException {
    String query = "";
    Connection connection = Vdb.Connect();
    Statement statement = connection.createStatement();
    DatabaseMetaData meta = connection.getMetaData();
    ResultSet set = meta.getTables(null, null, "EQUIPMENT", new String[] {"TABLE"});
    if (!set.next()) {
      statement.execute(
          "CREATE TABLE EQUIPMENT(ID char(15),name char(40), floor char(2),x int, y int,description char(100), isDirty boolean)");
    } else {
      statement.execute("DROP TABLE EQUIPMENT");
      createSQLTable();
      return;
    }
  }
}

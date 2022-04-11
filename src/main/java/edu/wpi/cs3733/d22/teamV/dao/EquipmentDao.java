package edu.wpi.cs3733.d22.teamV.dao;

import edu.wpi.cs3733.d22.teamV.main.Vdb;
import edu.wpi.cs3733.d22.teamV.objects.Equipment;
import java.io.*;
import java.sql.*;
import java.util.ArrayList;

public class EquipmentDao {

  private static ArrayList<Equipment> allEquipment;

  /** Initialize the array list */
  public EquipmentDao() {
    allEquipment = new ArrayList<Equipment>();
    try {
      createSQLTable();
      loadFromCSV();
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

  public void loadFromCSV() throws IOException, SQLException {
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
              Integer.parseInt(data[6]) == 1);
      addEquipment(equipment);
    }
  }


  public void saveToCSV() throws IOException {
    FileWriter fw = new FileWriter(Vdb.currentPath + "\\ListofEquipment.csv");
    BufferedWriter bw = new BufferedWriter(fw);
    bw.append("ID,Name,Floor,X,Y,Description,isDirty");

    for (Equipment equipment : getAllEquipment()) {

      String[] outputData = {
        equipment.getID(),
        equipment.getName(),
        equipment.getFloor(),
        String.valueOf(equipment.getX()),
        String.valueOf(equipment.getY()),
        equipment.getDescription(),
        String.valueOf(equipment.getIsDirty())
      };
      bw.append("\n");
      for (String s : outputData) {
        bw.append(s);
        bw.append(',');
      }
    }

    bw.close();
    fw.close();
  }

  public ArrayList<Equipment> getAllServiceRequests() {
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
          "CREATE TABLE EQUIPMENT(ID char(15),name char(40), floor char(2),x int, y int,description char(254), isDirty boolean)");
    } else {
      statement.execute("DROP TABLE EQUIPMENT");
      createSQLTable();
      return;
    }
  }

  public void addToSQLTable(Equipment equipment) throws SQLException {
    String query = "";
    Connection connection = Vdb.Connect();
    assert connection != null;
    Statement statement = connection.createStatement();

    query =
        "INSERT INTO EQUIPMENT("
            + "ID,Floor,X,Y,Description,isDirty) VALUES "
            + "('"
            + equipment.getID()
            + "', '"
            + equipment.getFloor()
            + "', "
            + equipment.getX()
            + ", "
            + equipment.getY()
            + ", '"
            + equipment.getDescription()
            + "', "
            + equipment.getIsDirty()
            + ")";
    statement.execute(query);
  }

  public void removeFromSQLTable(Equipment equipment) throws IOException, SQLException {
    String query = "";
    Connection connection = Vdb.Connect();
    assert connection != null;
    Statement statement = connection.createStatement();

    query = "DELETE FROM EQUIPMENT WHERE ID = '" + equipment.getID() + "'";
    statement.execute(query);
  }

  public void addEquipment(Equipment equipment) throws IOException, SQLException {
    allEquipment.add(equipment);
    addToSQLTable(equipment);
  }

  public void removeEquipment(Equipment equipment) throws IOException, SQLException {
    allEquipment.removeIf(equipment1 -> equipment1.getID().equals(equipment.getID()));
    removeFromSQLTable(equipment);
  }

  public ArrayList<Equipment> getAllEquipment() {
    return allEquipment;
  }

  public void setAllEquipment(ArrayList<Equipment> allEquipment) throws SQLException {
    this.allEquipment = allEquipment;
    createSQLTable();
  }
}

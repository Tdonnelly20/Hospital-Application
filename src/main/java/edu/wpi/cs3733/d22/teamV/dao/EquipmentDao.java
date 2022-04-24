package edu.wpi.cs3733.d22.teamV.dao;

import edu.wpi.cs3733.d22.teamV.main.VApp;
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
    createSQLTable();
    loadFromCSV();
  }

  public void loadFromCSV() {
    try {

      String line = "";
      FileReader fr = new FileReader(VApp.currentPath + "/ListofEquipment.CSV");
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
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public void saveToCSV() {
    try {

      FileWriter fw = new FileWriter(VApp.currentPath + "/ListofEquipment.csv");
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
          String.valueOf(equipment.getIsDirty() ? 1 : 0)
        };
        bw.append("\n");
        for (String s : outputData) {
          bw.append(s);
          bw.append(',');
        }
      }

      bw.close();
      fw.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public ArrayList<Equipment> getAllServiceRequests() {
    return allEquipment;
  }

  public void createSQLTable() {
    try {

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
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  public void addToSQLTable(Equipment equipment) {
    try {

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
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  public void removeFromSQLTable(Equipment equipment) {
    try {

      String query = "";
      Connection connection = Vdb.Connect();
      assert connection != null;
      Statement statement = connection.createStatement();

      query = "DELETE FROM EQUIPMENT WHERE ID = '" + equipment.getID() + "'";
      statement.execute(query);
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  public void removeFromSQLTable(String equipmentID) {
    try {

      String query = "";
      Connection connection = Vdb.Connect();
      assert connection != null;
      Statement statement = connection.createStatement();

      query = "DELETE FROM EQUIPMENT WHERE ID = '" + equipmentID + "'";
      statement.execute(query);
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  public void addEquipment(Equipment equipment) {
    if (getEquipment(equipment.getID()) != null) {
      System.out.println("Equipment with ID " + equipment.getID() + " already exists!");
      removeEquipment(getEquipment(equipment.getID()));
      removeFromSQLTable(equipment);
    }
    allEquipment.add(equipment);
    addToSQLTable(equipment);
    saveToCSV();
  }

  public void updateEquipment(Equipment newEquipment, String equipmentID) {
    newEquipment.setID(equipmentID);
    removeEquipment(equipmentID);
    addEquipment(newEquipment);
  }

  public void addEquipment(ArrayList<Equipment> equipmentList) {
    for (Equipment equipment : equipmentList) {
      addEquipment(equipment);
    }
  }

  public Equipment getEquipment(String id) {
    for (Equipment equipment : allEquipment) {
      if (equipment.getID().equals(id)) {
        return equipment;
      }
    }
    return null;
  }

  public void removeEquipment(Equipment equipment) {
    allEquipment.removeIf(currEquipment -> equipment.getID().equals(currEquipment.getID()));
    removeFromSQLTable(equipment);
    saveToCSV();
  }

  public void removeEquipment(String equipmentID) {
    allEquipment.removeIf(currEquipment -> equipmentID.equals(currEquipment.getID()));
    removeFromSQLTable(equipmentID);
    saveToCSV();
  }

  public ArrayList<Equipment> getAllEquipment() {
    return allEquipment;
  }

  public void setAllEquipment(ArrayList<Equipment> allEquipment) {
    this.allEquipment = allEquipment;
    createSQLTable();
  }
}

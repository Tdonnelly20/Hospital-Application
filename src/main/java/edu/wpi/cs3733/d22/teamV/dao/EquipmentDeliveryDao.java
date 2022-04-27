package edu.wpi.cs3733.d22.teamV.dao;

import edu.wpi.cs3733.d22.teamV.interfaces.DaoInterface;
import edu.wpi.cs3733.d22.teamV.main.RequestSystem;
import edu.wpi.cs3733.d22.teamV.main.VApp;
import edu.wpi.cs3733.d22.teamV.main.Vdb;
import edu.wpi.cs3733.d22.teamV.servicerequests.EquipmentDelivery;
import edu.wpi.cs3733.d22.teamV.servicerequests.ServiceRequest;
import java.io.*;
import java.sql.*;
import java.util.ArrayList;

public class EquipmentDeliveryDao extends DaoInterface {

  // All equipment deliveries
  private static ArrayList<EquipmentDelivery> allEquipmentDeliveries;

  /** Create SQL table, load from CSV */
  public EquipmentDeliveryDao() {
    allEquipmentDeliveries = new ArrayList<>();
    createSQLTable();
    loadFromCSV();
  }

  /** Load all the deliveries on the CSV into the arraylist */
  public void loadFromCSV() {
    try {

      String line = "";
      FileReader fr = new FileReader(VApp.currentPath + "/MedEquipReq.CSV");
      BufferedReader br = new BufferedReader(fr);
      String headerLine = br.readLine();
      String splitToken = ",";
      ArrayList<EquipmentDelivery> deliveries = new ArrayList<>();
      while ((line = br.readLine()) != null) // should create a database based on csv file
      {
        String[] data;
        data = line.split(splitToken);
        EquipmentDelivery equipmentDelivery =
            new EquipmentDelivery(
                Integer.parseInt(data[0]),
                Integer.parseInt(data[1]),
                data[2],
                data[3],
                data[4],
                Integer.parseInt(data[5]),
                data[6],
                Integer.parseInt(data[7]),
                data[8]);
        equipmentDelivery.setServiceID(Integer.parseInt(data[7]));
        deliveries.add(equipmentDelivery);
      }
      allEquipmentDeliveries = deliveries;
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /** Save all the equipment requests in the arraylist to the CSV */
  @Override
  public void saveToCSV() {
    try {

      FileWriter fw = new FileWriter(VApp.currentPath + "/MedEquipReq.csv");
      BufferedWriter bw = new BufferedWriter(fw);
      bw.append("employeeID,patientID,location,equipment,notes,quantity,status,serviceID");

      for (ServiceRequest request : getAllServiceRequests()) {

        EquipmentDelivery equipmentDelivery = (EquipmentDelivery) request;

        String[] outputData = {
          String.valueOf(equipmentDelivery.getEmployeeID()),
          String.valueOf(equipmentDelivery.getPatientID()),
          equipmentDelivery.getLocation().getNodeID(),
          equipmentDelivery.getEquipment(),
          equipmentDelivery.getDetails(),
          String.valueOf(equipmentDelivery.getQuantity()),
          equipmentDelivery.getStatus(),
          String.valueOf(equipmentDelivery.getServiceID()),
          equipmentDelivery.getTimeMade().toString()
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

  /** Create a SQL table for all the service requests */
  @Override
  public void createSQLTable() {
    try {

      Connection connection = Vdb.Connect();
      Statement exampleStatement = connection.createStatement();
      DatabaseMetaData meta = connection.getMetaData();

      ResultSet set = meta.getTables(null, null, "EQUIPMENTDELIVERY", new String[] {"TABLE"});
      String query = "";

      if (!set.next()) {
        query =
            "CREATE TABLE EQUIPMENTDELIVERY(employeeID int, patientID int, location char(50), equipment char(50), notes char(254), quantity int, status char(20), serviceID int,date_time timestamp)";
        exampleStatement.execute(query);
      } else {
        query = "DROP TABLE EQUIPMENTDELIVERY";
        exampleStatement.execute(query);
        createSQLTable();
        return;
      }
      for (EquipmentDelivery equipmentDelivery : allEquipmentDeliveries) {
        addToSQLTable(equipmentDelivery);
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  /**
   * Add a specific request into SQL
   *
   * @param request
   */
  @Override
  public void addToSQLTable(ServiceRequest request) {
    try {

      EquipmentDelivery equipmentDelivery = (EquipmentDelivery) request;

      String query = "";
      Connection connection = Vdb.Connect();
      assert connection != null;
      Statement statement = connection.createStatement();

      query =
          "INSERT INTO EQUIPMENTDELIVERY("
              + "employeeID,patientID,location,equipment,notes,quantity,status,serviceID,date_time) VALUES "
              + "("
              + equipmentDelivery.getEmployeeID()
              + ", "
              + equipmentDelivery.getPatientID()
              + ", '"
              + equipmentDelivery.getNodeID()
              + "', '"
              + equipmentDelivery.getEquipment()
              + "','"
              + equipmentDelivery.getDetails()
              + "',"
              + equipmentDelivery.getQuantity()
              + ",'"
              + equipmentDelivery.getStatus()
              + "',"
              + equipmentDelivery.getServiceID()
              + ",'"
              + equipmentDelivery.getTimeMade()
              + "')";
      System.out.println(query);
      statement.execute(query);

    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  /**
   * Replace a service request with another, or update a specific request with new values
   *
   * @param request
   * @param serviceID
   */
  @Override
  public void updateServiceRequest(ServiceRequest request, int serviceID) {
    EquipmentDelivery delivery = (EquipmentDelivery) request;
    delivery.setServiceID(serviceID);
    removeServiceRequest(delivery);
    allEquipmentDeliveries.add(delivery);
    addToSQLTable(delivery);
    saveToCSV();
  }

  /**
   * Remove a request from the SQL table
   *
   * @param request
   */
  @Override
  public void removeFromSQLTable(ServiceRequest request) {
    try {

      EquipmentDelivery equipmentDelivery = (EquipmentDelivery) request;
      String query = "";
      Connection connection = Vdb.Connect();
      assert connection != null;
      Statement statement = connection.createStatement();

      query = "DELETE FROM EQUIPMENTDELIVERY WHERE serviceID = " + request.getServiceID();
      statement.execute(query);
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  /**
   * Add a service request into the arraylist, then the SQL table, then save to a CSV
   *
   * @param request
   */
  @Override
  public void addServiceRequest(ServiceRequest request) {
    EquipmentDelivery equipmentDelivery = (EquipmentDelivery) request;
    request.setServiceID(RequestSystem.getServiceID());
    allEquipmentDeliveries.add(equipmentDelivery);

    addToSQLTable(request);
    saveToCSV();
  }

  /**
   * Remove a service request from the arraylist, then the SQL, then the CSV
   *
   * @param request
   */
  @Override
  public void removeServiceRequest(ServiceRequest request) {
    allEquipmentDeliveries.removeIf(value -> value.getServiceID() == request.getServiceID());
    request.detachAll();
    removeFromSQLTable(request);
    saveToCSV();
  }

  /**
   * Get all service requests in the arraylist
   *
   * @return
   */
  @Override
  public ArrayList<? extends ServiceRequest> getAllServiceRequests() {
    return allEquipmentDeliveries;
  }

  /**
   * Set all service requests in the arraylist
   *
   * @param serviceRequests
   */
  @Override
  public void setAllServiceRequests(ArrayList<? extends ServiceRequest> serviceRequests) {
    allEquipmentDeliveries = new ArrayList<>();

    // Convert to subtype
    for (ServiceRequest request : serviceRequests) {
      // Cast to subtype
      EquipmentDelivery delivery = (EquipmentDelivery) request;
      allEquipmentDeliveries.add(delivery);
      createSQLTable();
    }
  }
}

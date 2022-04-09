package edu.wpi.cs3733.d22.teamV.dao;

import edu.wpi.cs3733.d22.teamV.ServiceRequests.EquipmentDelivery;
import edu.wpi.cs3733.d22.teamV.ServiceRequests.ServiceRequest;
import edu.wpi.cs3733.d22.teamV.interfaces.DaoInterface;
import edu.wpi.cs3733.d22.teamV.main.Vdb;
import java.io.*;
import java.sql.*;
import java.util.ArrayList;

public class EquipmentDeliveryDao implements DaoInterface {

  private static ArrayList<EquipmentDelivery> allEquipmentDeliveries;

  /** Initialize the array list */
  public EquipmentDeliveryDao() {
    allEquipmentDeliveries = new ArrayList<EquipmentDelivery>();
    try {
      loadFromCSV();
      createSQLTable();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Override
  public void loadFromCSV() throws IOException, SQLException {
    String line = "";
    FileReader fr = new FileReader(Vdb.currentPath + "\\MedEquipReq.CSV");
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
              data[5],
              data[6],
              Integer.parseInt(data[7]),
              data[8],
              Integer.parseInt(data[9]));
      deliveries.add(equipmentDelivery);
    }
    allEquipmentDeliveries = deliveries;
  }

  @Override
  public void saveToCSV() throws IOException {
    FileWriter fw = new FileWriter(Vdb.currentPath + "\\MedEquipReq.csv");
    BufferedWriter bw = new BufferedWriter(fw);
    bw.append(
        "employeeID,patientID,patientFirstName,patientLastName,location,equipment,notes,quantity,status,serviceID");

    for (ServiceRequest request : getAllServiceRequests()) {

      EquipmentDelivery equipmentDelivery = (EquipmentDelivery) request;

      String[] outputData = {
        String.valueOf(equipmentDelivery.getEmployeeID()),
        String.valueOf(equipmentDelivery.getPatientID()),
        equipmentDelivery.getPatientFirstName(),
        equipmentDelivery.getPatientLastName(),
        equipmentDelivery.getEquipment(),
        equipmentDelivery.getNotes(),
        String.valueOf(equipmentDelivery.getQuantity()),
        equipmentDelivery.getStatus(),
        String.valueOf(equipmentDelivery.getServiceID())
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

  @Override
  public void createSQLTable() throws SQLException {
    Connection connection = Vdb.Connect();
    Statement exampleStatement = connection.createStatement();
    DatabaseMetaData meta = connection.getMetaData();

    ResultSet set = meta.getTables(null, null, "EQUIPMENTDELIVERY", new String[] {"TABLE"});
    String query = "";

    if (!set.next()) {
      query =
          "CREATE TABLE EQUIPMENTDELIVERY(employeeID int,patientID int, patientFirstName char(50), patientLastName char(50), location char(50), equipment char(50), notes char(254), quantity int, status char(20), serviceID int)";
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
  }

  @Override
  public void addToSQLTable(ServiceRequest request) throws SQLException {
    EquipmentDelivery equipmentDelivery = (EquipmentDelivery) request;

    String query = "";
    Connection connection = Vdb.Connect();
    assert connection != null;
    Statement statement = connection.createStatement();

    query =
        "INSERT INTO EQUIPMENTDELIVERY("
            + "employeeID,patientID,patientFirstName,patientLastName,location,equipment,notes,quantity,status,serviceID) VALUES "
            + "("
            + equipmentDelivery.getEmployeeID()
            + ", "
            + equipmentDelivery.getPatientID()
            + ", '"
            + equipmentDelivery.getPatientFirstName()
            + "', '"
            + equipmentDelivery.getPatientLastName()
            + "', '"
            + equipmentDelivery.getLocationName()
            + "', '"
            + equipmentDelivery.getEquipment()
            + "','"
            + equipmentDelivery.getNotes()
            + "',"
            + equipmentDelivery.getQuantity()
            + ",'"
            + equipmentDelivery.getStatus()
            + "',"
            + equipmentDelivery.getServiceID()
            + ")";

    statement.execute(query);
  }

  @Override
  public void removeFromSQLTable(ServiceRequest request) throws IOException {
    EquipmentDelivery equipmentDelivery = (EquipmentDelivery) request;
    allEquipmentDeliveries.removeIf(
        value -> value.getServiceID() == equipmentDelivery.getServiceID());
    removeFromSQLTable(request);
    saveToCSV();
  }

  @Override
  public void addServiceRequest(ServiceRequest request) throws IOException, SQLException {
    EquipmentDelivery equipmentDelivery = (EquipmentDelivery) request;
    request.setServiceID(Vdb.getServiceID());
    allEquipmentDeliveries.add(equipmentDelivery);

    addToSQLTable(request);
    saveToCSV();
  }

  @Override
  public void removeServiceRequest(ServiceRequest request) throws IOException {
    allEquipmentDeliveries.removeIf(value -> value.getServiceID() == request.getServiceID());
    removeFromSQLTable(request);
    saveToCSV();
  }

  @Override
  public ArrayList<? extends ServiceRequest> getAllServiceRequests() {
    return allEquipmentDeliveries;
  }

  @Override
  public void setAllServiceRequests(ArrayList<? extends ServiceRequest> serviceRequests)
      throws SQLException {
    allEquipmentDeliveries = new ArrayList<>();

    // Convert to subtype
    for (ServiceRequest request : serviceRequests) {
      // Cast to subtype
      EquipmentDelivery delivery = (EquipmentDelivery) request;
      allEquipmentDeliveries.add(delivery);
      createSQLTable();
    }
  }

  @Override
  public void updateRequest(ServiceRequest request) throws SQLException {}
}

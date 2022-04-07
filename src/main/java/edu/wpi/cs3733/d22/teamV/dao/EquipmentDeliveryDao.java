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
      // loadFromCSV();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Override
  public void loadFromCSV() throws IOException {
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
      for (String s : data) {
        EquipmentDelivery equipmentDelivery =
            new EquipmentDelivery(
                Integer.parseInt(data[0]),
                data[1],
                data[2],
                data[3],
                Integer.parseInt(data[4]),
                data[5],
                Integer.parseInt(data[6]),
                data[7],
                data[8]);
        deliveries.add(equipmentDelivery);
      }
    }
    setAllServiceRequests(deliveries);
    System.out.println("Equipment Delivery CSV Loaded");
  }

  @Override
  public void saveToCSV() throws IOException {
    FileWriter fw = new FileWriter(Vdb.currentPath + "\\MedEquipReq.csv");
    BufferedWriter bw = new BufferedWriter(fw);
    bw.append(
        "employeeID,patientID,patientFirstName,patientLastName,location,equipment,notes,quantity,status");

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
        equipmentDelivery.getStatus()
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
          "CREATE TABLE EQUIPMENTDELIVERY(employeeID int,patientID int, patientFirstName char(50), patientLastName char(50), location char(50), equipment char(50), notes char(254), quantity int, status char(20))";
      exampleStatement.execute(query);
    } else {
      query = "DROP TABLE EQUIPMENTDELIVERY";
      exampleStatement.execute(query);
      createSQLTable();
      return;
    }
  }

  @Override
  public void addToSQLTable(ServiceRequest request) throws SQLException {}

  @Override
  public void removeFromSQLTable(ServiceRequest request) {}

  @Override
  public void addServiceRequest(ServiceRequest request) throws IOException, SQLException {
    EquipmentDelivery equipmentDelivery = (EquipmentDelivery) request;
    allEquipmentDeliveries.add(equipmentDelivery);

    System.out.println("Adding to database");

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
  public void setAllServiceRequests(ArrayList<? extends ServiceRequest> serviceRequests) {
    allEquipmentDeliveries = new ArrayList<>();

    // Convert to subtype
    for (ServiceRequest request : serviceRequests) {
      // Cast to subtype
      EquipmentDelivery delivery = (EquipmentDelivery) request;
      allEquipmentDeliveries.add(delivery);
    }
  }
}

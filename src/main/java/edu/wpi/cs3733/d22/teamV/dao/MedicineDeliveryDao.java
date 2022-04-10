package edu.wpi.cs3733.d22.teamV.dao;

import edu.wpi.cs3733.d22.teamV.interfaces.DaoInterface;
import edu.wpi.cs3733.d22.teamV.main.Vdb;
import edu.wpi.cs3733.d22.teamV.servicerequests.MedicineDelivery;
import edu.wpi.cs3733.d22.teamV.servicerequests.ServiceRequest;
import java.io.*;
import java.sql.*;
import java.util.ArrayList;

public class MedicineDeliveryDao extends DaoInterface {
  // A local list of all medicine deliveries, updated via Vdb
  private static ArrayList<MedicineDelivery> allMedicineDeliveries;
  /** Initialize the arraylist */
  public MedicineDeliveryDao() {
    allMedicineDeliveries = new ArrayList<MedicineDelivery>();

    try {
      loadFromCSV();
      createSQLTable();
    } catch (IOException e) {
      e.printStackTrace();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  public void loadFromCSV() throws IOException {

    FileReader fr = new FileReader(Vdb.currentPath + "\\MedicineDelivery.csv");
    BufferedReader br = new BufferedReader(fr);
    String splitToken = ","; // what we split the csv file with
    ArrayList<MedicineDelivery> medicineDeliveries = new ArrayList<>();

    String headerLine = br.readLine();
    String line;

    while ((line = br.readLine()) != null) // should create a database based on csv file
    {
      String[] data = line.split(splitToken);
      MedicineDelivery newDelivery =
          new MedicineDelivery(
              data[0],
              data[1],
              data[2],
              Integer.parseInt(data[3]),
              Integer.parseInt(data[4]),
              data[5],
              data[6],
              data[7]);

      newDelivery.setServiceID(Integer.parseInt(data[8]));
      medicineDeliveries.add(newDelivery);
    }

    setAllServiceRequests(medicineDeliveries);
  }

  @Override
  public void saveToCSV() throws IOException {
    FileWriter fw = new FileWriter(Vdb.currentPath + "\\MedicineDelivery.csv");
    BufferedWriter bw = new BufferedWriter(fw);
    bw.append(
        "patientFirstName,patientLastName,nodeID,patientID,employeeID,medicineName,dosage,requestDetails,serviceID");

    for (ServiceRequest request : getAllServiceRequests()) {

      MedicineDelivery medicineDelivery = (MedicineDelivery) request;

      String[] outputData = {
        medicineDelivery.getPatientFirstName(),
        medicineDelivery.getPatientLastName(),
        medicineDelivery.getNodeID(),
        String.valueOf(medicineDelivery.getPatientID()),
        String.valueOf(medicineDelivery.getEmployeeID()),
        medicineDelivery.getMedicineName(),
        medicineDelivery.getDosage(),
        medicineDelivery.getRequestDetails(),
        String.valueOf(medicineDelivery.getServiceID())
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
    assert connection != null;
    Statement statement = connection.createStatement();
    DatabaseMetaData meta = connection.getMetaData();
    ResultSet set = meta.getTables(null, null, "MEDICINES", new String[] {"TABLE"});
    String query = "";

    if (!set.next()) {
      query =
          "CREATE TABLE MEDICINES(patientFirstName char(50), patientLastName char(50), nodeID char(50), patientID int, employeeID int, medicineName char(50), dosage char(50), requestDetails char(254), serviceID int)";
      statement.execute(query);

    } else {
      query = "DROP TABLE MEDICINES";
      statement.execute(query);
      createSQLTable(); // rerun the method to generate new tables
      return;
    }

    for (MedicineDelivery medicineDelivery : allMedicineDeliveries) {
      addToSQLTable(medicineDelivery);
    }
  }

  @Override
  public void addToSQLTable(ServiceRequest request) throws SQLException {
    MedicineDelivery medicineDelivery = (MedicineDelivery) request;

    String query = "";
    Connection connection = Vdb.Connect();
    assert connection != null;
    Statement statement = connection.createStatement();
    query =
        "INSERT INTO MEDICINES("
            + "patientFirstName,patientLastName,nodeID,patientID,employeeID,medicineName,dosage,requestDetails,serviceID) VALUES "
            + "('"
            + medicineDelivery.getPatientFirstName()
            + "', '"
            + medicineDelivery.getPatientLastName()
            + "', '"
            + medicineDelivery.getNodeID()
            + "', "
            + medicineDelivery.getPatientID()
            + ", "
            + medicineDelivery.getEmployeeID()
            + ", '"
            + medicineDelivery.getMedicineName()
            + "','"
            + medicineDelivery.getDosage()
            + "','"
            + medicineDelivery.getRequestDetails()
            + "',"
            + medicineDelivery.getServiceID()
            + ")";

    statement.execute(query);
  }

  @Override
  public void removeFromSQLTable(ServiceRequest request) throws SQLException {
    String query = "";
    Connection connection = Vdb.Connect();
    assert connection != null;
    Statement statement = connection.createStatement();

    query = "DELETE FROM MEDICINES WHERE serviceID = " + request.getServiceID();
    statement.execute(query);
  }

  @Override
  public void addServiceRequest(ServiceRequest request) throws IOException, SQLException {
    int serviceID = Vdb.getServiceID();
    MedicineDelivery medicineDelivery = (MedicineDelivery) request;
    medicineDelivery.setServiceID(serviceID);
    allMedicineDeliveries.add(medicineDelivery); // Store a local copy

    addToSQLTable(request);
    saveToCSV();
  }

  @Override
  public void removeServiceRequest(ServiceRequest request) throws IOException, SQLException {
    MedicineDelivery medicineDelivery = (MedicineDelivery) request;
    allMedicineDeliveries.removeIf(
        value -> value.getServiceID() == medicineDelivery.getServiceID());
    removeFromSQLTable(request);
    saveToCSV();
  }

  @Override
  public ArrayList<? extends ServiceRequest> getAllServiceRequests() {

    return allMedicineDeliveries;
  }

  @Override
  public void setAllServiceRequests(ArrayList<? extends ServiceRequest> serviceRequests) {
    // Set all medicine deliveries
    allMedicineDeliveries = new ArrayList<>();

    // Convert to subtype
    for (ServiceRequest request : serviceRequests) {
      // Cast to subtype
      MedicineDelivery delivery = (MedicineDelivery) request;
      allMedicineDeliveries.add(delivery);
    }
  }
}
